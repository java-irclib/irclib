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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.schwering.irc.lib.IRCConfig;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCExceptionHandler;
import org.schwering.irc.lib.IRCRuntimeConfig;
import org.schwering.irc.lib.IRCServerConfig;
import org.schwering.irc.lib.IRCTrafficLogger;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.util.IRCModeParser;
import org.schwering.irc.lib.util.IRCParser;
import org.schwering.irc.lib.util.IRCUtil;
import org.schwering.irc.lib.util.LoggingReader;
import org.schwering.irc.lib.util.LoggingWriter;

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
 * @see IRCEventListener
 * @see IRCParser
 * @see IRCUtil
 * @see SSLIRCConnection
 */
public class DefaultIRCConnection extends Thread implements IRCConnection {
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
    private final IRCExceptionHandler exceptionHandler;

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
        /* we trust only our own DefaultIRCConfig that it is immutable */
        this.config = config instanceof DefaultIRCConfig ? config : new DefaultIRCConfig(config);
        this.nick = config.getNick();
        this.trafficLogger = config.getTrafficLogger();
        this.exceptionHandler = config.getExceptionHandler();
    }

    /**
     * @param serverConfig
     * @param runtimeConfig
     */
    public DefaultIRCConnection(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
        this(new DefaultIRCConfig(serverConfig, runtimeConfig));
    }

    /**
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @see org.schwering.irc.lib.IRCConnection#connect()
     */
    @Override
    public void connect() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        if (level != 0) // otherwise disconnected or connect
            throw new SocketException("Socket closed or already open (" + level + ")");
        SocketFactory socketFactory = new SocketFactory(config.getTimeout(), config.getProxy(), config.getSSLSupport());

        IOException exception = null;
        Socket socket = null;
        final String host = config.getHost();
        for (int i = 0; i < config.getPortsCount() && socket == null; i++) {
            try {
                int port = config.getPortAt(i);
                socket = socketFactory.createSocket(host, port);
                exception = null;
            } catch (IOException exc) {
                if (socket != null) {
                    socket.close();
                }
                socket = null;
                exception = exc;
            }
        }
        if (exception != null) {
            /* we could not connect on any port */
            throw exception;
        }

        prepare(socket);
    }

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
            handleException(exc);
            close();
        } finally {
            close();
        }
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#send(java.lang.String)
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
            handleException(exc);
            throw new RuntimeException(exc);
        }
    }

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

    /**
     * @see org.schwering.irc.lib.IRCConnection#close()
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
        if (exceptionHandler != null) {
            exceptionHandler.exception(exc);
        }
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#addIRCEventListener(org.schwering.irc.lib.IRCEventListener)
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

    /**
     * @see org.schwering.irc.lib.IRCConnection#removeIRCEventListener(org.schwering.irc.lib.IRCEventListener)
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
     * @see org.schwering.irc.lib.IRCConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        return level >= 1;
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#getNick()
     */
    @Override
    public String getNick() {
        return nick;
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#getPort()
     */
    @Override
    public int getPort() {
        return (socket != null) ? socket.getPort() : 0;
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#getTimeout()
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

    /**
     * @see org.schwering.irc.lib.IRCConnection#getLocalAddress()
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
     * @see org.schwering.irc.lib.IRCConnection#doAway()
     */
    @Override
    public void doAway() {
        send("AWAY");
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doAway(java.lang.String)
     */
    @Override
    public void doAway(String msg) {
        send("AWAY :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doInvite(java.lang.String, java.lang.String)
     */
    @Override
    public void doInvite(String nick, String chan) {
        send("INVITE " + nick + " " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doIson(java.lang.String)
     */
    @Override
    public void doIson(String nick) {
        send("ISON " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doJoin(java.lang.String)
     */
    @Override
    public void doJoin(String chan) {
        send("JOIN " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doJoin(java.lang.String, java.lang.String)
     */
    @Override
    public void doJoin(String chan, String key) {
        send("JOIN " + chan + " " + key);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doKick(java.lang.String, java.lang.String)
     */
    @Override
    public void doKick(String chan, String nick) {
        send("KICK " + chan + " " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doKick(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void doKick(String chan, String nick, String msg) {
        send("KICK " + chan + " " + nick + " :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doList()
     */
    @Override
    public void doList() {
        send("LIST");
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doList(java.lang.String)
     */
    @Override
    public void doList(String chan) {
        send("LIST " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doNames()
     */
    @Override
    public void doNames() {
        send("NAMES");
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doNames(java.lang.String)
     */
    @Override
    public void doNames(String chan) {
        send("NAMES " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doPrivmsg(java.lang.String, java.lang.String)
     */
    @Override
    public void doPrivmsg(String target, String msg) {
        send("PRIVMSG " + target + " :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doMode(java.lang.String)
     */
    @Override
    public void doMode(String chan) {
        send("MODE " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doMode(java.lang.String, java.lang.String)
     */
    @Override
    public void doMode(String target, String mode) {
        send("MODE " + target + " " + mode);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doNick(java.lang.String)
     */
    @Override
    public void doNick(String nick) {
        send("NICK " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doNotice(java.lang.String, java.lang.String)
     */
    @Override
    public void doNotice(String target, String msg) {
        send("NOTICE " + target + " :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doPart(java.lang.String)
     */
    @Override
    public void doPart(String chan) {
        send("PART " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doPart(java.lang.String, java.lang.String)
     */
    @Override
    public void doPart(String chan, String msg) {
        send("PART " + chan + " :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doPong(java.lang.String)
     */
    @Override
    public void doPong(String ping) {
        send("PONG :" + ping);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doQuit()
     */
    @Override
    public void doQuit() {
        send("QUIT");
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doQuit(java.lang.String)
     */
    @Override
    public void doQuit(String msg) {
        send("QUIT :" + msg);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doTopic(java.lang.String)
     */
    @Override
    public void doTopic(String chan) {
        send("TOPIC " + chan);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doTopic(java.lang.String, java.lang.String)
     */
    @Override
    public void doTopic(String chan, String topic) {
        send("TOPIC " + chan + " :" + topic);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doWho(java.lang.String)
     */
    @Override
    public void doWho(String criteric) {
        send("WHO " + criteric);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doWhois(java.lang.String)
     */
    @Override
    public void doWhois(String nick) {
        send("WHOIS " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doWhowas(java.lang.String)
     */
    @Override
    public void doWhowas(String nick) {
        send("WHOWAS " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#doUserhost(java.lang.String)
     */
    @Override
    public void doUserhost(String nick) {
        send("USERHOST " + nick);
    }

    /**
     * @see org.schwering.irc.lib.IRCConnection#isSSL()
     */
    @Override
    public boolean isSSL() {
        return config.getSSLSupport() != null;
    }

}
