/**
 * IRClib - A Java Internet Relay Chat library
 * Copyright (C) 2006-2015 Christoph Schwering <schwering@gmail.com>
 * and/or other contributors as indicated by the @author tags.
 *
 * This library and the accompanying materials are made available under the
 * terms of the
 *  - GNU Lesser General Public License,
 *  - Apache License, Version 2.0 and
 *  - Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */
package org.schwering.irc.lib.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;

import org.schwering.irc.lib.IRCConfig;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCTrafficLogger;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.impl.ssl.SSLIRCConnection;
import org.schwering.irc.lib.util.IRCModeParser;
import org.schwering.irc.lib.util.IRCParser;
import org.schwering.irc.lib.util.IRCUtil;

/**
 * Creates a new connection to an IRC server. It's the main class of the IRClib,
 * the point everything starts.
 * <p>
 * The following code of a class which imports org.schwering.irc.lib.* prepares
 * an IRC connection and then tries to establish the connection. The server is
 * &quot;irc.somenetwork.com&quot;, the default portrange (6667 and 6669) is set, no
 * password is used (null). The nickname is &quot;Foo&quot; and the realname is
 * &quot;Mr. Foobar&quot;. The username &quot;foobar&quot;. Because of setDaemon(true), the JVM
 * exits even if this thread is running. An instance of the class MyListener
 * which must implement IRCActionListener is added as only event-listener for
 * the connection. The connection is told to parse out mIRC color codes and to
 * enable automatic PING? PONG! replies.
 * <pre>
 * IRCConnection conn = new IRCConnection(&quot;irc.somenetwork.com&quot;, 6667, 6669, null, &quot;Foo&quot;,
 *      &quot;Mr. Foobar&quot;, &quot;foo@bar.com&quot;);
 *
 * conn.addIRCEventListener(new MyListener());
 * conn.setDaemon(true);
 * conn.setColors(false);
 * conn.setPong(true);
 *
 * try {
 *     conn.connect(); // Try to connect!!! Don't forget this!!!
 * } catch (IOException ioexc) {
 *     ioexc.printStackTrace();
 * }
 * </pre>
 * <p>
 * The serverpassword isn't needed in most cases. You can give <code>null</code>
 * or <code>""</code> instead as done in this example.
 *
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 3.05
 * @see IRCEventListener
 * @see IRCParser
 * @see IRCUtil
 * @see SSLIRCConnection
 */
public class DefaultIRCConnection extends Thread implements IRCConnection {
    /**
     * A {@link BufferedReader} that sends all read character to its {@link #trafficLogger}.
     */
    private static class LoggingReader extends BufferedReader {
        private final IRCTrafficLogger trafficLogger;

        /**
         * @param in the reader to read from.
         * @param trafficLogger a logger to notify about read characters
         */
        public LoggingReader(Reader in, IRCTrafficLogger trafficLogger) {
            super(in);
            this.trafficLogger = trafficLogger;
        }

        /**
         * @see java.io.BufferedReader#readLine()
         */
        @Override
        public String readLine() throws IOException {
            String line = super.readLine();
            trafficLogger.in(line);
            return line;
        }
    }

    /**
     * A {@link PrintWriter} that sends all written character also to its {@link #trafficLogger}.
     */
    private static class LoggingWriter extends PrintWriter {
        private final IRCTrafficLogger trafficLogger;

        /**
         * @param out the {@link Writer} to write to
         * @param trafficLogger the logger to notify about the written characters
         */
        public LoggingWriter(Writer out, IRCTrafficLogger trafficLogger) {
            super(out);
            this.trafficLogger = trafficLogger;
        }

        /**
         * @see java.io.PrintWriter#write(java.lang.String)
         */
        @Override
        public void write(String s) {
            String trimmedLine = s;
            if (s != null && s.endsWith("\r\n")) {
                trimmedLine = s.substring(0, s.length() - 2);
            }
            trafficLogger.out(trimmedLine);
            super.write(s);
        }

    }

    /**
     * This <code>Socket</code> is a connection to the IRC server.
     */
    private Socket socket;

    /**
     * This is like a UNIX-runlevel. Its value indicates the level of the
     * <code>IRCConnection</code> object. <code>0</code> means that the object
     * has not yet been connected, <code>1</code> means that it's connected but
     * not registered, <code>2</code> means that it's connected and registered
     * but still waiting to receive the nickname the first time, <code>3</code>
     * means that it's connected and registered, and <code>-1</code> means that
     * it was connected but is disconnected. Therefore the default value is
     * <code>0</code>.
     */
    protected byte level = 0;

    /**
     * The <code>BufferedReader</code> receives Strings from the IRC server.
     */
    private BufferedReader in;

    /**
     * The <code>PrintWriter</code> sends Strings to the IRC server.
     */
    private PrintWriter out;

    /**
     * This array contains <code>IRCEventListener</code> objects.
     */
    private IRCEventListener[] listeners = new IRCEventListener[0];

    private final IRCTrafficLogger trafficLogger;

    protected final IRCConfig config;

    protected String nick;

    /**
     * @param config
     */
    public DefaultIRCConnection(IRCConfig config) {
        int[] ports = config.getPorts();
        if (config.getHost() == null || ports == null || ports.length == 0) {
            throw new IllegalArgumentException("Host and ports may not be null.");
        }
        this.config = config;
        this.nick = config.getNick();
        this.trafficLogger = config.getTrafficLogger();
    }

    /**
     * Establish a connection to the server.
     * This method must be invoked to start a connection; the constructor
     * doesn't do that!
     * It tries all set ports until one is open. If all ports fail it throws an
     * <code>IOException</code>.
     * You can invoke <code>connect</code> only one time.
     *
     * @throws IOException
     *             If an I/O error occurs.
     * @throws SocketException
     *             If the <code>connect</code> method was already invoked.
     * @see #isConnected()
     * @see #doQuit()
     * @see #doQuit(String)
     * @see #close()
     */
    @Override
    public void connect() throws IOException {
        if (level != 0) // otherwise disconnected or connect
            throw new SocketException("Socket closed or already open (" + level + ")");
        IOException exception = null;
        Socket s = null;
        Proxy proxy = config.getProxy();
        final String host = config.getHost();
        for (int i = 0; i < config.getPortsCount() && s == null; i++) {
            try {
                s = proxy != null ? new Socket(proxy) : new Socket();
                int port = config.getPortAt(i);
                s.connect(new InetSocketAddress(host, port));
                exception = null;
            } catch (IOException exc) {
                if (s != null)
                    s.close();
                s = null;
                exception = exc;
            }
        }
        if (exception != null)
            throw exception; // connection wasn't successful at any port

        prepare(s);
    }

    // ------------------------------

    /**
     * Invoked by the <code>connect</code> method, this method prepares the
     * connection.
     * It initializes the class-vars for the inputstream and the outputstream of
     * the socket, starts the registration of at the IRC server by calling
     * <code>register()</code> and starts the receiving of lines from the server
     * by starting the thread with the <code>start</code> method.
     *
     * This method must be protected, because it is used by extending classes,
     * which override the <code>connect</code> method.
     *
     * @param s
     *            The socket which is used for the connection.
     * @throws IOException
     *             If an I/O error occurs.
     * @see #connect()
     * @see #run()
     */
    protected void prepare(Socket s) throws IOException {
        if (s == null)
            throw new SocketException("Socket s is null, not connected");
        socket = s;
        level = 1;
        s.setSoTimeout(config.getTimeout());
        String encoding = config.getEncoding();
        if (trafficLogger != null) {
            in = new LoggingReader(new InputStreamReader(s.getInputStream(), encoding), trafficLogger);
            out = new LoggingWriter(new OutputStreamWriter(s.getOutputStream(), encoding), trafficLogger);
        } else {
            in = new BufferedReader(new InputStreamReader(s.getInputStream(), encoding));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), encoding));
        }
        start();
        register();
    }

    // ------------------------------

    /**
     * Registers the connection with the IRC server.
     * In fact, it sends a password (if set, else nothing), the nickname and the
     * user, the realname and the host which we're connecting to.
     * The action synchronizes <code>code> so that no important messages
     * (like the first PING) come in before this registration is finished.
     * The <code>USER</code> command's format is:
     * <code>
     * &lt;username&gt; &lt;localhost&gt; &lt;irchost&gt; &lt;realname&gt;
     * </code>
     */
    private void register() {
        String pass = config.getPassword();
        if (pass != null)
            send("PASS " + pass);

        send("NICK " + config.getNick());
        send("USER " + config.getUsername() + " " + socket.getLocalAddress().getHostAddress() + " "
                + config.getHost() + " :" + config.getRealname());
    }

    // ------------------------------

    /**
     * The <code>Thread</code> is started by the <code>connect</code> method.
     * It's task is to receive strings from the IRC server and hand them over to
     * the <code>get</code> method.
     *
     * Possibly occuring <code>IOException</code>s are handled according to the
     * set exception handling.
     */
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                get(line);
            }
        } catch (IOException exc) {
            if (trafficLogger != null) {
                trafficLogger.exception(exc);
            }
            close();
        } finally {
            close();
        }
    }

    // ------------------------------

    /**
     * Sends a String to the server. You should use this method only, if you
     * must do it. For most purposes, there are <code>do*</code> methods (like
     * <code>doJoin</code>). A carriage return line feed (<code>\r\n</code>) is
     * appended automatically.
     *
     * @param line
     *            The line which should be send to the server without the
     *            trailing carriage return line feed (<code>\r\n</code>).
     */
    @Override
    public void send(String line) {
        try {
            out.write(line + "\r\n");
            out.flush();
            if (level == 1) { // not registered
                IRCParser p = new IRCParser(line);
                if ("NICK".equalsIgnoreCase(p.getCommand()))
                    nick = p.getParameter(1).trim();
            }
        } catch (Exception exc) {
            if (trafficLogger != null) {
                trafficLogger.exception(exc);
            }
            throw new RuntimeException(exc);
        }
    }

    // ------------------------------

    /**
     * Just parses a String given as the only argument with the help of the
     * <code>IRCParser</code> class. Then it controls the command and fires
     * events through the <code>IRCEventListener</code>.
     *
     * @param line
     *            The line which is sent from the server.
     */
    private synchronized void get(String line) {
        IRCParser p;
        try {
            p = new IRCParser(line, config.isStripColorsEnabled());
        } catch (Exception exc) {
            return;
        }
        String command = p.getCommand();
        int reply; // 3-digit reply will be parsed in the later if-condition

        if ("PRIVMSG".equalsIgnoreCase(command)) { // MESSAGE

            IRCUser user = p.getUser();
            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onPrivmsg(middle, user, trailing);

        } else if ("MODE".equalsIgnoreCase(command)) { // MODE

            String chan = p.getParameter(1);
            if (IRCUtil.isChan(chan)) {
                IRCUser user = p.getUser();
                String param2 = p.getParameter(2);
                String paramsFrom3 = p.getParametersFrom(3);
                for (int i = listeners.length - 1; i >= 0; i--)
                    listeners[i].onMode(chan, user, new IRCModeParser(param2, paramsFrom3));
            } else {
                IRCUser user = p.getUser();
                String paramsFrom2 = p.getParametersFrom(2);
                for (int i = listeners.length - 1; i >= 0; i--)
                    listeners[i].onMode(user, chan, paramsFrom2);
            }

        } else if ("PING".equalsIgnoreCase(command)) { // PING

            String ping = p.getTrailing(); // no int cause sometimes it's text
            if (config.isAutoPong())
                doPong(ping);
            else
                for (int i = listeners.length - 1; i >= 0; i--)
                    listeners[i].onPing(ping);

            if (level == 1) { // not registered
                level = 2; // first PING received -> connection
                for (int i = listeners.length - 1; i >= 0; i--)
                    listeners[i].onRegistered();
            }

        } else if ("JOIN".equalsIgnoreCase(command)) { // JOIN

            IRCUser user = p.getUser();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onJoin(trailing, user);

        } else if ("NICK".equalsIgnoreCase(command)) { // NICK

            IRCUser user = p.getUser();
            String changingNick = p.getNick();
            String newNick = p.getTrailing();
            if (changingNick.equalsIgnoreCase(nick))
                nick = newNick;
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onNick(user, newNick);

        } else if ("QUIT".equalsIgnoreCase(command)) { // QUIT

            IRCUser user = p.getUser();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onQuit(user, trailing);

        } else if ("PART".equalsIgnoreCase(command)) { // PART

            IRCUser user = p.getUser();
            String chan = p.getParameter(1);
            String msg = p.getParameterCount() > 1 ? p.getTrailing() : "";
            // not logic: "PART :#zentrum" is without msg,
            // "PART #zentrum :cjo all"
            // is with msg. so we cannot use getMiddle and getTrailing :-/
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onPart(chan, user, msg);

        } else if ("NOTICE".equalsIgnoreCase(command)) { // NOTICE

            IRCUser user = p.getUser();
            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onNotice(middle, user, trailing);

        } else if ((reply = IRCUtil.parseInt(command)) >= 1 && reply < 400) { // RPL

            String potNick = p.getParameter(1);
            if ((level == 1 || level == 2) && nick.length() > potNick.length()
                    && nick.substring(0, potNick.length()).equalsIgnoreCase(potNick)) {
                nick = potNick;
                if (level == 2)
                    level = 3;
            }

            if (level == 1 && nick.equals(potNick)) { // not registered
                level = 2; // if first PING wasn't received, we're
                for (int i = listeners.length - 1; i >= 0; i--)
                    listeners[i].onRegistered(); // connected now for sure
            }

            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onReply(reply, middle, trailing);

        } else if (reply >= 400 && reply < 600) { // ERROR

            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onError(reply, trailing);

        } else if ("KICK".equalsIgnoreCase(command)) { // KICK

            IRCUser user = p.getUser();
            String param1 = p.getParameter(1);
            String param2 = p.getParameter(2);
            String msg = (p.getParameterCount() > 2) ? p.getTrailing() : "";
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onKick(param1, user, param2, msg);

        } else if ("INVITE".equalsIgnoreCase(command)) { // INVITE

            IRCUser user = p.getUser();
            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onInvite(trailing, user, middle);

        } else if ("TOPIC".equalsIgnoreCase(command)) { // TOPIC

            IRCUser user = p.getUser();
            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onTopic(middle, user, trailing);

        } else if ("ERROR".equalsIgnoreCase(command)) { // ERROR

            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onError(trailing);

        } else { // OTHER

            String prefix = p.getPrefix();
            String middle = p.getMiddle();
            String trailing = p.getTrailing();
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].unknown(prefix, command, middle, trailing);

        }
    }

    // ------------------------------

    /**
     * Close down the connection brutally.
     * It does *NOT* send the proper IRC command <code>QUIT</code>. You should
     * always use the <code>doQuit</code> methods or <code>send("QUIT")</code>
     * instead of this method.
     * You should use this method to close down the connection only when the IRC
     * server doesn't react to the <code>QUIT</code> command.
     * Possibly occuring <code>IOException</code>s are handled according to the
     * set exception handling.
     *
     * @see #connect()
     * @see #doQuit
     * @see #doQuit(String)
     */
    @Override
    public synchronized void close() {
        try {
            if (!isInterrupted())
                interrupt();
        } catch (Exception exc) {
            handleException(exc);
        }
        try {
            if (socket != null)
                socket.close();
        } catch (Exception exc) {
            handleException(exc);
        }
        try {
            if (out != null)
                out.close();
        } catch (Exception exc) {
            handleException(exc);
        }
        try {
            if (in != null)
                in.close();
        } catch (Exception exc) {
            handleException(exc);
        }
        if (this.level != -1) {
            this.level = -1;
            for (int i = listeners.length - 1; i >= 0; i--)
                listeners[i].onDisconnected();
        }
        socket = null;
        in = null;
        out = null;
        listeners = new IRCEventListener[0];
    }

    /**
     * Handles the exception according to the current exception handling mode.
     */
    private void handleException(Exception exc) {
        if (trafficLogger != null) {
            trafficLogger.exception(exc);
        } else {
            throw new RuntimeException(exc);
        }
    }

    // ------------------------------

    /**
     * Adds a new {@link IRCEventListener} which listens for actions coming from
     * the IRC server.
     *
     * @param l
     *            An instance of the {@link IRCEventListener} interface.
     * @throws IllegalArgumentException
     *             If <code>listener</code> is <code>null</code>.
     */
    @Override
    public synchronized void addIRCEventListener(IRCEventListener l) {
        if (l == null)
            throw new IllegalArgumentException("Listener is null.");
        int len = listeners.length;
        IRCEventListener[] oldListeners = listeners;
        listeners = new IRCEventListener[len + 1];
        System.arraycopy(oldListeners, 0, listeners, 0, len);
        listeners[len] = l;
    }

    // ------------------------------

    /**
     * Adds a new {@link IRCEventListener} which listens for actions coming from
     * the IRC server at a given index.
     *
     * @param l
     *            An instance of the {@link IRCEventListener} interface.
     * @param i
     *            The designated index of the listener.
     * @throws IllegalArgumentException
     *             If <code>listener</code> is <code>null</code>.
     * @throws IndexOutOfBoundsException
     *             If <code>i</code> is not greater than 0 and less or equal
     *             than <code>listeners.length</code>.
     */
    public synchronized void addIRCEventListener(IRCEventListener l, int i) {
        if (l == null)
            throw new IllegalArgumentException("Listener is null.");
        if (i < 0 || i > listeners.length)
            throw new IndexOutOfBoundsException("i is not in range");
        int len = listeners.length;
        IRCEventListener[] oldListeners = listeners;
        listeners = new IRCEventListener[len + 1];
        if (i > 0)
            System.arraycopy(oldListeners, 0, listeners, 0, i);
        if (i < listeners.length)
            System.arraycopy(oldListeners, i, listeners, i + 1, len - i);
        listeners[i] = l;
    }

    // ------------------------------

    /**
     * Removes the first occurence of the given {@link IRCEventListener} from
     * the listener-vector.
     *
     * @param l
     *            An instance of the {@link IRCEventListener} interface.
     * @return <code>true</code> if the listener was successfully removed;
     *         <code>false</code> if it was not found.
     */
    @Override
    public synchronized boolean removeIRCEventListener(IRCEventListener l) {
        if (l == null)
            return false;
        int index = -1;
        for (int i = 0; i < listeners.length; i++)
            if (listeners[i].equals(l)) {
                index = i;
                break;
            }
        if (index == -1)
            return false;
        listeners[index] = null;
        int len = listeners.length - 1;
        IRCEventListener[] newListeners = new IRCEventListener[len];
        for (int i = 0, j = 0; i < len; j++)
            if (listeners[j] != null)
                newListeners[i++] = listeners[j];
        listeners = newListeners;
        return true;
    }

    /**
     * Tells whether there's a connection to the IRC network or not.
     * If <code>connect</code> wasn't called yet, it returns <code>false</code>.
     *
     * @return The status of the connection; <code>true</code> if it's
     *         connected.
     * @see #connect()
     * @see #doQuit()
     * @see #doQuit(String)
     * @see #close()
     */
    @Override
    public boolean isConnected() {
        return level >= 1;
    }

    // ------------------------------

    /**
     * Returns the nickname of this instance.
     *
     * @return The nickname.
     */
    @Override
    public String getNick() {
        return nick;
    }

    /**
     * Returns the port to which the <code>IRCConnection</code> connected, or
     * <code>0</code> if the connection failed or wasn't tried yet.
     *
     * @return The port to which the <code>IRCConnection</code>, or
     *         <code>0</code> if the connection failed or wasn't tried yet.
     */
    @Override
    public int getPort() {
        return (socket != null) ? socket.getPort() : 0;
    }

    /**
     * Returns the timeout of the socket.
     * If an error occurs, which is never the case, <code>-1</code> is returned.
     * The possibly occuring <code>IOException</code> are handled according to
     * the set exception handling.
     *
     * @return The timeout.
     */
    @Override
    public int getTimeout() {
        if (socket != null)
            try {
                return socket.getSoTimeout();
            } catch (IOException exc) {
                handleException(exc);
                return INVALID_TIMEOUT;
            }
        else
            return INVALID_TIMEOUT;
    }

    // ------------------------------

    /**
     * Returns the local address of the connection socket. If the connection is
     * not yet connected, <code>null</code> is returned.
     *
     * @return the local address
     */
    @Override
    public InetAddress getLocalAddress() {
        return (socket != null) ? socket.getLocalAddress() : null;
    }


    @Override
    public String toString() {
        return "DefaultIRCConnection [nick=" + nick + ", config=" + config + "]";
    }

    /**
     * Removes away message.
     */
    @Override
    public void doAway() {
        send("AWAY");
    }

    // ------------------------------

    /**
     * Sets away message.
     *
     * @param msg
     *            The away message.
     */
    @Override
    public void doAway(String msg) {
        send("AWAY :" + msg);
    }

    // ------------------------------

    /**
     * Invites a user to a channel.
     *
     * @param nick
     *            The nickname of the user who should be invited.
     * @param chan
     *            The channel the user should be invited to.
     */
    @Override
    public void doInvite(String nick, String chan) {
        send("INVITE " + nick + " " + chan);
    }

    // ------------------------------

    /**
     * Checks if one or more nicks are used on the server.
     *
     * @param nick
     *            The nickname of the user we search for.
     */
    @Override
    public void doIson(String nick) {
        send("ISON " + nick);
    }

    // ------------------------------

    /**
     * Joins a channel without a key.
     *
     * @param chan
     *            The channel which is to join.
     */
    @Override
    public void doJoin(String chan) {
        send("JOIN " + chan);
    }

    // ------------------------------

    /**
     * Joins a channel with a key.
     *
     * @param chan
     *            The channel which is to join.
     * @param key
     *            The key of the channel.
     */
    @Override
    public void doJoin(String chan, String key) {
        send("JOIN " + chan + " " + key);
    }

    // ------------------------------

    /**
     * Kicks a user from a channel.
     *
     * @param chan
     *            The channel somebody should be kicked from.
     * @param nick
     *            The nickname of the user who should be kicked.
     */
    @Override
    public void doKick(String chan, String nick) {
        send("KICK " + chan + " " + nick);
    }

    // ------------------------------

    /**
     * Kicks a user from a channel with a comment.
     *
     * @param chan
     *            The channel somebody should be kicked from.
     * @param nick
     *            The nickname of the user who should be kicked.
     * @param msg
     *            The optional kickmessage.
     */
    @Override
    public void doKick(String chan, String nick, String msg) {
        send("KICK " + chan + " " + nick + " :" + msg);
    }

    // ------------------------------

    /**
     * Lists all channels with their topic and status.
     */
    @Override
    public void doList() {
        send("LIST");
    }

    // ------------------------------

    /**
     * Lists channel(s) with their topic and status.
     *
     * @param chan
     *            The channel the <code>LIST</code> refers to.
     */
    @Override
    public void doList(String chan) {
        send("LIST " + chan);
    }

    // ------------------------------

    /**
     * Lists all visible users.
     */
    @Override
    public void doNames() {
        send("NAMES");
    }

    // ------------------------------

    /**
     * Lists all visible users of (a) channel(s).
     *
     * @param chan
     *            The channel the <code>NAMES</code> command is refering to.
     */
    @Override
    public void doNames(String chan) {
        send("NAMES " + chan);
    }

    // ------------------------------

    /**
     * Sends a message to a person or a channel.
     *
     * @param target
     *            The nickname or channel the message should be sent to.
     * @param msg
     *            The message which should be transmitted.
     */
    @Override
    public void doPrivmsg(String target, String msg) {
        send("PRIVMSG " + target + " :" + msg);
    }

    // ------------------------------

    /**
     * Requests a Reply 324 for the modes of a given channel.
     *
     * @param chan
     *            The channel the <code>MODE</code> request is refering to.
     */
    @Override
    public void doMode(String chan) {
        send("MODE " + chan);
    }

    // ------------------------------

    /**
     * Sends a mode to the server.
     * The first argument is a nickname (user-mode) or a channel (channel-mode).
     * <code>String mode</code> must contain the operators (+/-), the modes
     * (o/v/i/k/l/p/s/w) and the possibly values (nicks/banmask/limit/key).
     *
     * @param target
     *            The nickname or channel of the user whose modes will be
     *            changed.
     * @param mode
     *            The new modes.
     */
    @Override
    public void doMode(String target, String mode) {
        send("MODE " + target + " " + mode);
    }

    // ------------------------------

    /**
     * Changes the nickname.
     *
     * @param nick
     *            The new nickname.
     */
    @Override
    public void doNick(String nick) {
        send("NICK " + nick);
    }

    // ------------------------------

    /**
     * Notices a message to a person or a channel.
     *
     * @param target
     *            The nickname or channel (group) the message should be sent to.
     * @param msg
     *            The message which should be transmitted.
     */
    @Override
    public void doNotice(String target, String msg) {
        send("NOTICE " + target + " :" + msg);
    }

    // ------------------------------

    /**
     * Parts from a given channel.
     *
     * @param chan
     *            The channel you want to part from.
     */
    @Override
    public void doPart(String chan) {
        send("PART " + chan);
    }

    // ------------------------------

    /**
     * Parts from a given channel with a given parg-msg.
     *
     * @param chan
     *            The channel you want to part from.
     * @param msg
     *            The optional partmessage.
     */
    @Override
    public void doPart(String chan, String msg) {
        send("PART " + chan + " :" + msg);
    }

    // ------------------------------

    /**
     * Quits from the IRC server with a quit-msg.
     *
     * @param ping
     *            The ping which was received in <code>onPing</code>. It's a
     *            <code>String</code>, because sometimes on some networks the
     *            server-hostname (for example splatterworld.quakenet.org) is
     *            given as parameter which would throw an Exception if we gave
     *            the ping as long.
     */
    @Override
    public void doPong(String ping) {
        send("PONG :" + ping);
    }

    // ------------------------------

    /**
     * Quits from the IRC server. Calls the <code>disconnect</code>-method which
     * does the work actually.
     *
     * @see #isConnected()
     * @see #connect()
     * @see #doQuit(String)
     * @see #close()
     */
    @Override
    public void doQuit() {
        send("QUIT");
    }

    // ------------------------------

    /**
     * Quits from the IRC server with a quit-msg. Calls the
     * <code>disconnect</code>-method which does the work actually.
     *
     * @param msg
     *            The optional quitmessage.
     * @see #isConnected()
     * @see #connect()
     * @see #doQuit()
     * @see #close()
     */
    @Override
    public void doQuit(String msg) {
        send("QUIT :" + msg);
    }

    // ------------------------------

    /**
     * Requests the topic of a chan. The topic is given in a numeric reply.
     *
     * @param chan
     *            The channel which topic should be requested.
     */
    @Override
    public void doTopic(String chan) {
        send("TOPIC " + chan);
    }

    // ------------------------------

    /**
     * Changes the topic of a chan.
     *
     * @param chan
     *            The channel which topic is changed.
     * @param topic
     *            The new topic.
     */
    @Override
    public void doTopic(String chan, String topic) {
        send("TOPIC " + chan + " :" + topic);
    }

    // ------------------------------

    /**
     * Requests information about users matching the given criteric, for example
     * a channel they are on.
     *
     * @param criteric
     *            The criterics of the <code>WHO</code> query.
     */
    @Override
    public void doWho(String criteric) {
        send("WHO " + criteric);
    }

    // ------------------------------

    /**
     * Requires information about an existing user.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    @Override
    public void doWhois(String nick) {
        send("WHOIS " + nick);
    }

    // ------------------------------

    /**
     * Requires host-information about a user, who is not connected anymore.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    @Override
    public void doWhowas(String nick) {
        send("WHOWAS " + nick);
    }

    // ------------------------------

    /**
     * Requires host-information about up to 5 users which must be listed and
     * divided by spaces.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    @Override
    public void doUserhost(String nick) {
        send("USERHOST " + nick);
    }

}
