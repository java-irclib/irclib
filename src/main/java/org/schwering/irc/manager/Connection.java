/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCConnection
 * Copyright (C) 2002 - 2006 Christoph Schwering <schwering@gmail.com>
 * 
 * This library and the accompanying materials are made available under the
 * terms of the
 * 	- GNU Lesser General Public License,
 * 	- Apache License, Version 2.0 and
 * 	- Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */
package org.schwering.irc.manager;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCConstants;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.ssl.SSLIRCConnection;
import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.ConnectionListener;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpClientinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpClientinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpDccChatEvent;
import org.schwering.irc.manager.event.CtcpDccSendEvent;
import org.schwering.irc.manager.event.CtcpErrmsgReplyEvent;
import org.schwering.irc.manager.event.CtcpErrmsgRequestEvent;
import org.schwering.irc.manager.event.CtcpFingerReplyEvent;
import org.schwering.irc.manager.event.CtcpFingerRequestEvent;
import org.schwering.irc.manager.event.CtcpListener;
import org.schwering.irc.manager.event.CtcpPingReplyEvent;
import org.schwering.irc.manager.event.CtcpPingRequestEvent;
import org.schwering.irc.manager.event.CtcpSedEvent;
import org.schwering.irc.manager.event.CtcpSourceReplyEvent;
import org.schwering.irc.manager.event.CtcpSourceRequestEvent;
import org.schwering.irc.manager.event.CtcpTimeReplyEvent;
import org.schwering.irc.manager.event.CtcpTimeRequestEvent;
import org.schwering.irc.manager.event.CtcpUnknownReplyEvent;
import org.schwering.irc.manager.event.CtcpUnknownRequestEvent;
import org.schwering.irc.manager.event.CtcpUserinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpUserinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpVersionReplyEvent;
import org.schwering.irc.manager.event.CtcpVersionRequestEvent;
import org.schwering.irc.manager.event.ErrorEvent;
import org.schwering.irc.manager.event.InfoEvent;
import org.schwering.irc.manager.event.InvitationEvent;
import org.schwering.irc.manager.event.LinksEvent;
import org.schwering.irc.manager.event.ListEvent;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.MotdEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.NumericEvent;
import org.schwering.irc.manager.event.PingEvent;
import org.schwering.irc.manager.event.PrivateMessageListener;
import org.schwering.irc.manager.event.StatsEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UnexpectedEvent;
import org.schwering.irc.manager.event.UnexpectedEventListener;
import org.schwering.irc.manager.event.UserModeEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.WhoEvent;
import org.schwering.irc.manager.event.WhoisEvent;
import org.schwering.irc.manager.event.WhowasEvent;

/**
 * A wrapper for <code>IRCConnection</code> and interface to various administration tasks of an IRC connection.
 * <p/>
 * This class manages the wrapped <code>IRCConnection</code> object and a set of the joined channels. Additionally, a
 * connection is the point where <code>ConnectionListener</code>s, <code>CtcpListener</code>s,
 * <code>PrivateMessageListener</code>s, and <code>UnexpectedEventListener</code>s are registered.
 *
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.00
 * @since 2.00
 */
public class Connection {
    private IRCConnection conn;
    private final Map<String, Channel> channels = new TreeMap<String, Channel>();
    private boolean requestModes = true;
    private boolean colorsEnabled = true;
    private boolean ctcpEnabled = true;
    private NickGenerator nickGenerator = new DefaultNickGenerator();
    private final List<ConnectionListener> connectionListeners = new LinkedList<ConnectionListener>();
    private final List<CtcpListener> ctcpListeners = new LinkedList<CtcpListener>();
    private final List<CtcpListener> privateCtcpListeners = new LinkedList<CtcpListener>();
    private final List<PrivateMessageListener> privateMessageListeners = new LinkedList<PrivateMessageListener>();
    private final List<UnexpectedEventListener> unexpectedEventListeners = new LinkedList<UnexpectedEventListener>();

    /**
     * Initializes a new connection. This means that the internal <code>IRCConnection</code> is initialized with the
     * connection data specified as constructor arguments and some other specific values. Further configuration can be done
     * via the <code>Connection</code> class's methods.
     */
    public Connection(String host, int portMin, int portMax, boolean ssl,
        String pass, String nick, String username, String realname) {
        if (ssl) {
            conn = new SSLIRCConnection(host, portMin, portMax, pass, nick,
                username, realname);
        } else {
            conn = new IRCConnection(host, portMin, portMax, pass, nick,
                username, realname);
        }
        conn.setPong(true);
        conn.setColors(true);
        conn.addIRCEventListener(new BasicListener(this));
    }

    /**
     * Initializes a new connection. This means that the internal <code>IRCConnection</code> is initalized with the
     * connection data specified as constructor arguments and some other specific values. Further configuration can be done
     * via the <code>Connection</code> class's methods.
     */
    public Connection(String host, int[] ports, boolean ssl,
        String pass, String nick, String username, String realname) {
        if (ssl) {
            conn = new SSLIRCConnection(host, ports, pass, nick, username,
                realname);
        } else {
            conn = new IRCConnection(host, ports, pass, nick, username,
                realname);
        }
        conn.setPong(true);
        conn.setColors(true);
        conn.addIRCEventListener(new BasicListener(this));
    }

    /**
     * Returns the <code>IRCConnection</code> object.
     */
    IRCConnection getIRCConnection() {
        return conn;
    }

    /**
     * Returns the connected user's current nickname. This method simply calls <code>IRCConnection.getNick()</code>.
     * <p/>
     * Note: The IRC server might implicitly change the originally set nickname. This done when the set nick contains
     * illegal characters or is too long, for example. Therefore one should always use this method to obtain the current
     * nickname instead of storing it once and accessing this stored string which might then be not equal to the real
     * nickname.
     *
     * @see IRCConnection#getNick()
     */
    public String getNick() {
        return conn.getNick();
    }

    /**
     * Returns the IRC server's hostname. This method simply calls <code>IRCConnection.getHost()</code>.
     *
     * @see IRCConnection#getHost()
     */
    public String getServerHostname() {
        return conn.getHost();
    }

    /**
     * Returns the encoding of the connection. This method simply calls <code>IRCConnection.getEncoding()</code>.
     *
     * @see IRCConnection#getEncoding()
     */
    public String getEncoding() {
        return conn.getEncoding();
    }

    /**
     * Sets the encoding of the connection. This method simply calls <code>IRCConnection.setEncoding()</code>.
     *
     * @see IRCConnection#setEncoding(String)
     */
    public void setEncoding(String encoding) {
        conn.setEncoding(encoding);
    }

    /**
     * Returns the timeout of the connection. This method simply calls <code>IRCConnection.getTimeout()</code>.
     *
     * @see IRCConnection#getTimeout()
     */
    public int getTimeout() {
        return conn.getTimeout();
    }

    /**
     * Sets the timeout of the connection. This method simply calls <code>IRCConnection.setTimeout()</code>.
     *
     * @see IRCConnection#setTimeout(int)
     */
    public void setTimeout(int millis) {
        conn.setTimeout(millis);
    }

    /**
     * Returns the local host of the connected socket. This method simply calls <code>IRCConnection.getTimeout()</code>.
     *
     * @see IRCConnection#getLocalAddress()
     */
    public InetAddress getLocalAddress() {
        return conn.getLocalAddress();
    }

    /**
     * Enables or disables debugging. This method simply calls <code>IRCConnection.setDebug()</code>.
     *
     * @see IRCConnection#setDebug(boolean)
     */
    public void setDebug(boolean debug) {
        conn.setDebug(debug);
    }

    /**
     * Sets the debug stream. <code>null</code> (default) means <code>System.out</code>. This method simply calls
     * <code>IRCConnection.setDebugStream()</code>.
     *
     * @see IRCConnection#setDebugStream(PrintStream)
     */
    public void setDebugStream(PrintStream debugStream) {
        conn.setDebugStream(debugStream);
    }

    /**
     * Returns the current nickname generator.
     * <p/>
     * The nickname generator is needed because the server might ask for a new nickname when we try to establish the
     * connection. This question for a new nickname must be answered somehow, and this task is delivered to the nickname
     * generator.
     * <p/>
     * The nick generator is initialized with a default one which behaves as follows: the first invocation returns the
     * originally set nickname plus an underscore (i.e. "Peter_"), the second invocation returns a leading underscore (i.e.
     * "_Peter_") and all subsequent invocations return <code>null</code> which will kill the connection.
     */
    public NickGenerator getNickGenerator() {
        return nickGenerator;
    }

    /**
     * Sets the nickname generator.
     * <p/>
     * The nickname generator is needed because the server might ask for a new nickname when we try to establish the
     * connection. This question for a new nickname must be answered somehow, and this task is delivered to the nickname
     * generator.
     * <p/>
     * The nick generator is initialized with a default one which behaves as follows: the first invocation returns the
     * originally set nickname plus an underscore (i.e. "Peter_"), the second invocation returns a leading underscore (i.e.
     * "_Peter_") and all subsequent invocations return <code>null</code> which will kill the connection.
     */
    public void setNickGenerator(NickGenerator nickGen) {
        this.nickGenerator = nickGen;
    }

    /**
     * Starts the connection. This method simply calls <code>IRCConnection.connect</code>.
     *
     * @throws IOException If an I/O error occurs.
     * @throws SocketException If the connect method was already invoked.
     */
    public void connect() throws IOException {
        conn.connect();
    }

    /**
     * Indicates whether the connection is alive. This is the case if <code>connect</code> has been invoked. Note that
     * <code>isConnected</code> being <code>true</code> does not necessarily require <code>ConnectionListener.connectionEstablished</code>
     * to be fired. This method simply calls <code>IRCConnection.isConnected</code>.
     *
     * @see #connect()
     * @see IRCConnection#isConnected()
     */
    public boolean isConnected() {
        return conn.isConnected();
    }

    /**
     * Disconnects from the IRC server.
     * @param message
     */
    public void quit(final String message) {
        if(isConnected()) {
            conn.doQuit(message);
            conn.close();
        }
    }
    /**
     * Sends a line to the IRC server.
     *
     * @param line The line to be sent.
     */
    public void send(String line) {
        conn.send(line);
    }

    /**
     * Sends a PRIVMSG to a user or a channel.
     * <p/>
     * This is equivalent to <code>send("PRIVMSG "+ dest + " :"+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));</code>.
     *
     * @param dest Either a channel or a nickname.
     * @param msg The message itself.
     */
    public void sendPrivmsg(String dest, String msg) {
        send("PRIVMSG " + dest + " :" +
            CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));
    }

    /**
     * Sends a NOTICE to a user or a channel.
     * <p/>
     * This is equivalent to <code>send("NOTICE "+ dest + " :"+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));</code>.
     *
     * @param dest Either a channel or a nickname.
     * @param msg The message itself.
     */
    public void sendNotice(String dest, String msg) {
        send("NOTICE " + dest + " :" +
            CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));
    }

    /**
     * Sends a CTCP request. A CTCP reply is always sent as PRIVMSG.
     *
     * @param dest Either a channel name or a nickname.
     * @param command The CTCP command (e.g. VERSION).
     */
    public void sendCtcpRequest(String dest, String command) {
        send("PRIVMSG " + dest + " :" +
            IRCConstants.CTCP_DELIMITER
            + CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command))
            + IRCConstants.CTCP_DELIMITER);
    }

    /**
     * Sends a CTCP request. A CTCP reply is always sent as PRIVMSG.
     * <p/>
     * This is equivalent to <code>sendPrivmsg(dest, IRCConstants.CTCP_DELIMITER + command +" "+ args +
     * IRCConstants.CTCP_DELIMITER);</code>.
     *
     * @param dest Either a channel name or a nickname.
     * @param command The CTCP command (e.g. VERSION).
     * @param args The CTCP command's arguments.
     */
    public void sendCtcpRequest(String dest, String command, String args) {
        String tmp;
        if (args != null && args.length() > 0) {
            tmp = " " + args;
        } else {
            tmp = "";
        }
        send("PRIVMSG " + dest + " :" +
            IRCConstants.CTCP_DELIMITER
            + CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command + tmp))
            + IRCConstants.CTCP_DELIMITER);
    }

    /**
     * Sends a CTCP command. A CTCP reply is always sent as PRIVMSG.
     * <p/>
     * This is equivalent to <code>sendCtcpRequest(dest, command, args);</code>.
     *
     * @param dest Either a channel name or a nickname.
     * @param command The CTCP command (e.g. ACTION).
     * @param args The CTCP command's arguments.
     */
    public void sendCtcpCommand(String dest, String command, String args) {
        sendCtcpRequest(dest, command, args);
    }

    public void sendCtcpAction(String dest, String msg) {
        sendCtcpCommand(dest, "ACTION", msg);
    }

    /**
     * Sends a CTCP reply. A CTCP reply is always sent as NOTICE, because as a rule, PRIVMSGs (incoming CTCP requests)
     * should never trigger PRIVMSG replies.
     * <p/>
     * This is equivalent to <code>sendNotice(dest, IRCConstants.CTCP_DELIMITER + command +
     * IRCConstants.CTCP_DELIMITER);</code>.
     *
     * @param dest Either a channel name or a nickname.
     * @param command The CTCP command (e.g. VERSION).
     */
    public void sendCtcpReply(String dest, String command) {
        send("NOTICE " + dest + " :" +
            IRCConstants.CTCP_DELIMITER
            + CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command))
            + IRCConstants.CTCP_DELIMITER);
    }

    /**
     * Sends a CTCP reply. A CTCP reply is always sent as NOTICE, because as a rule, PRIVMSGs (incoming CTCP requests)
     * should never trigger PRIVMSG replies.
     * <p/>
     * This is equivalent to <code>sendNotice(dest, IRCConstants.CTCP_DELIMITER + command +" "+ args +
     * IRCConstants.CTCP_DELIMITER);</code>.
     *
     * @param dest Either a channel name or a nickname.
     * @param command The CTCP command (e.g. VERSION).
     * @param args The CTCP command's arguments.
     */
    public void sendCtcpReply(String dest, String command, String args) {
        String tmp;
        if (args != null && args.length() > 0) {
            tmp = " " + args;
        } else {
            tmp = "";
        }
        send("NOTICE " + dest + " :" +
            IRCConstants.CTCP_DELIMITER
            + CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command + tmp))
            + IRCConstants.CTCP_DELIMITER);
    }

    /**
     * Sends a DCC chat invitation. As address, the value returned by <code>IRCConnection.getLocalAddress()</code> is
     * taken.
     *
     * @param dest The receiver, i.e. a nickname or channel.
     * @param fileName The file's name.
     * @param port The port the DCC chat host is listening to.
     * @param size The file size.
     */
    public void sendDccSend(String dest, String fileName, int port, long size) {
        sendDccSend(dest, fileName, conn.getLocalAddress(), port, size);
    }

    /**
     * Sends a DCC chat invitation.
     *
     * @param dest The receiver, i.e. a nickname or channel.
     * @param fileName The file's name.
     * @param addr The host of the DCC chat (typically the own address).
     * @param port The port the DCC chat host is listening to.
     * @param size The file size.
     */
    public void sendDccSend(String dest, String fileName, InetAddress addr,
        int port, long size) {
        long address = CtcpUtil.convertInetAddressToLong(addr);
        sendCtcpCommand(dest, "DCC", "SEND " + fileName + " " + address + " " + port + " " + size);
    }

    /**
     * Sends a DCC chat invitation. As address, the value returned by <code>IRCConnection.getLocalAddress()</code> is
     * taken.
     *
     * @param dest The receiver, i.e. a nickname or channel.
     * @param port The port the DCC chat host is listening to.
     */
    public void sendDccChat(String dest, int port) {
        sendDccChat(dest, conn.getLocalAddress(), port);
    }

    /**
     * Sends a DCC chat invitation.
     *
     * @param dest The receiver, i.e. a nickname or channel.
     * @param addr The host of the DCC chat (typically the own address).
     * @param port The port the DCC chat host is listening to.
     */
    public void sendDccChat(String dest, InetAddress addr, int port) {
        long address = CtcpUtil.convertInetAddressToLong(addr);
        sendCtcpCommand(dest, "DCC", "CHAT chat " + address + " " + port);
    }

    /**
     * Indicates whether directly after joining a channel, the channel modes are requested automatically. By default, this
     * is <code>true</code>.
     */
    public boolean getRequestModes() {
        return requestModes;
    }

    /**
     * Indicates whether directly after joining a channel, the channel modes are requested automatically. By default, this
     * is <code>true</code>.
     */
    public void setRequestModes(boolean requestModes) {
        this.requestModes = requestModes;
    }

    /**
     * Enables or disables colors. By colors, the mIRC color codes are meant. If they're disabled, these color codes are
     * removed from all incoming messages (NOTICE, PRIVMSG and KICK, PART, QUIT messages). By default,
     * <code>enableColors</code> is <code>true</code>.
     */
    public void setColors(boolean enableColors) {
        this.colorsEnabled = enableColors;
    }

    /**
     * Indicates whether colors are enabled. By colors, the mIRC color codes are meant. By default,
     * <code>isColorsEnabled()</code> is <code>true</code>.
     *
     * @return <code>false</code> means that color codes are removed from all incoming messages (NOTICE, PRIVMSG and KICK,
     *         PART, QUIT messages).
     */
    public boolean isColorsEnabled() {
        return colorsEnabled;
    }

    public void setCtcp(boolean enableCtcp) {
        this.ctcpEnabled = enableCtcp;
    }

    public boolean isCtcpEnabled() {
        return ctcpEnabled;
    }

    /**
     * Returns a set of <code>Channel</code> objects that the connection participates in.
     */
    public Collection getChannels() {
        return Collections.unmodifiableCollection(channels.values());
    }

    /**
     * Returns <code>true</code> if the connection participates in the channel.
     */
    public boolean hasChannel(String channelName) {
        return channels.containsKey(channelName);
    }

    /**
     * Returns <code>true</code> if the connection participates in the channel.
     */
    public boolean hasChannel(Channel channel) {
        return channels.containsKey(channel.getName());
    }

    /**
     * Clears the channel map.
     */
    void clearChannels() {
        synchronized (channels) {
            channels.clear();
        }
    }

    /**
     * Stores a new channel in the map.
     */
    void addChannel(Channel channel) {
        synchronized (channels) {
            channels.put(channel.getName(), channel);
        }
    }

    /**
     * Removes a channel in the map.
     */
    void removeChannel(String channel) {
        synchronized (channels) {
            channels.remove(channel);
        }
    }

    /**
     * Removes a channel in the map.
     */
    void removeChannel(Channel channel) {
        channels.remove(channel.getName());
    }

    /**
     * Returns a <code>Channel</code> object that contains all known information about the channel. This is rather empty if
     * the connection has not joined the respective channel.
     */
    public Channel resolveChannel(String channelName) {
        if (channelName == null) {
            throw new IllegalArgumentException();
        }
        Channel channel = channels.get(channelName);
        return channel != null ? channel : new Channel(channelName);
    }

    public void partChannel(String channel) {
        conn.doPart(channel);
    }
    
    public void joinChannel(String channel) {
        conn.doJoin(channel);
    }
    
    public void joinChannel(String channel, String key) {
        conn.doJoin(channel, key);
    }

    /**
     * Returns a <code>User</code> object that contains all known information about a user. The <code>User</code> object
     * may contain further information like the user's host, username and away status.
     * <p/>
     * First, the method searches a <code>User</code> object in the joined channels. If this fails, a new <code>User</code>
     * is created.
     */
    public User resolveUser(String nick) {
        if (nick == null) {
            throw new IllegalArgumentException();
        }
        for (final Object o : getChannels()) {
            Channel channel = (Channel) o;
            ChannelUser user = channel.getUser(nick);
            if (user != null) {
                return user.getUser();
            }
        }
        return new User(nick);
    }

    /**
     * Returns a <code>User</code> object that contains all known information about a user. The <code>User</code> object
     * may contain further information like the user's host, username and away status.
     * <p/>
     * First, the method searches a <code>User</code> object in the joined channels. If this fails, a new <code>User</code>
     * is created.
     */
    public User resolveUser(IRCUser ircUser) {
        if (ircUser == null) {
            throw new IllegalArgumentException();
        }
        for (final Object o : getChannels()) {
            Channel channel = (Channel) o;
            ChannelUser user = channel.getUser(ircUser.getNick());
            if (user != null) {
                user.update(ircUser);
                return user.getUser();
            }
        }
        return new User(ircUser);
    }

    /**
     * Returns <code>true</code> if the user is the one represented by this connection.
     */
    public boolean isMe(User user) {
        return user.isSame(conn.getNick());
    }

    /**
     * Adds a pure <code>IRCEventListener</code> to the pure <code>IRCConnection</code>.
     */
    public void addIRCEventListener(IRCEventListener listener) {
        conn.addIRCEventListener(listener);
    }

    /**
     * Removes a pure <code>IRCEventListener</code> from the pure <code>IRCConnection</code>.
     */
    public void removeIRCEventListener(IRCEventListener listener) {
        conn.removeIRCEventListener(listener);
    }
    /* ConnectionListener methods */

    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    void fireConnectionEstablished(ConnectionEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.connectionEstablished(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireConnectionLost(ConnectionEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.connectionLost(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireErrorReceived(ErrorEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.errorReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireMotdReceived(MotdEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.motdReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireInfoReceived(InfoEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.infoReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireLinksReceived(LinksEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.linksReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireStatsReceived(StatsEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.statsReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePingReceived(PingEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.pingReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireChannelJoined(UserParticipationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.channelJoined(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireChannelLeft(UserParticipationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.channelLeft(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireInvitationReceived(InvitationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.invitationReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireInvitationDeliveryReceived(InvitationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.invitationDeliveryReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireNumericReplyReceived(NumericEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.numericReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireNumericErrorReceived(NumericEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.numericErrorReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireUserModeReceived(UserModeEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.userModeReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireWhoisReceived(WhoisEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.whoisReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireWhowasReceived(WhowasEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.whowasReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireUserJoined(UserParticipationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.userJoined(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireUserLeft(UserParticipationEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.userLeft(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireTopicReceived(TopicEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.topicReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireListReceived(ListEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.listReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireChannelModeReceived(ChannelModeEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.channelModeReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireNickChanged(NickEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.nickChanged(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireMessageReceived(MessageEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.messageReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireNoticeReceived(MessageEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.noticeReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireNamesReceived(NamesEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.namesReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireWhoReceived(WhoEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.whoReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireBanlistReceived(BanlistEvent event) {
        synchronized (connectionListeners) {
            for (final ConnectionListener connectionListener : connectionListeners) {
                try {
                    connectionListener.banlistReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }
    /* PrivateMessageListener methods */

    public void addPrivateMessageListener(PrivateMessageListener listener) {
        synchronized (privateMessageListeners) {
            privateMessageListeners.add(listener);
        }
    }

    public void removePrivateMessageListener(PrivateMessageListener listener) {
        synchronized (privateMessageListeners) {
            privateMessageListeners.remove(listener);
        }
    }

    void firePrivateMessageReceived(MessageEvent event) {
        synchronized (privateMessageListeners) {
            for (final Object privateMessageListener : privateMessageListeners) {
                try {
                    ((PrivateMessageListener) privateMessageListener).messageReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateNoticeReceived(MessageEvent event) {
        synchronized (privateMessageListeners) {
            for (final Object privateMessageListener : privateMessageListeners) {
                try {
                    ((PrivateMessageListener) privateMessageListener).noticeReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }
    /* Ctcp listener methods */

    public void addCtcpListener(CtcpListener listener) {
        synchronized (ctcpListeners) {
            ctcpListeners.add(listener);
        }
    }

    public void removeCtcpListener(CtcpListener listener) {
        synchronized (ctcpListeners) {
            ctcpListeners.remove(listener);
        }
    }

    void fireCtcpDccChatReceived(CtcpDccChatEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.dccChatReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpDccSendReceived(CtcpDccSendEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.dccSendReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpSedReceived(CtcpSedEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.sedReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpActionReceived(CtcpActionEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.actionReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpPingRequestReceived(CtcpPingRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.pingRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpTimeRequestReceived(CtcpTimeRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.timeRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpVersionRequestReceived(CtcpVersionRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.versionRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpFingerRequestReceived(CtcpFingerRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.fingerRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpSourceRequestReceived(CtcpSourceRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.sourceRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpUserinfoRequestReceived(CtcpUserinfoRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.userinfoRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpClientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.clientinfoRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpErrmsgRequestReceived(CtcpErrmsgRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.errmsgRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpUnknownRequestEventReceived(
        CtcpUnknownRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.unknownRequestEventReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpPingReplyReceived(CtcpPingReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.pingReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpTimeReplyReceived(CtcpTimeReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.timeReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpVersionReplyReceived(CtcpVersionReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.versionReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpFingerReplyReceived(CtcpFingerReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.fingerReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpSourceReplyReceived(CtcpSourceReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.sourceReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpUserinfoReplyReceived(CtcpUserinfoReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.userinfoReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpClientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.clientinfoReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpErrmsgReplyReceived(CtcpErrmsgReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.errmsgReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void fireCtcpUnknownReplyEventReceived(CtcpUnknownReplyEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.unknownReplyEventReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }
    /* private Ctcp listener methods */

    public void addPrivateCtcpListener(CtcpListener listener) {
        synchronized (privateCtcpListeners) {
            privateCtcpListeners.add(listener);
        }
    }

    public void removePrivateCtcpListener(CtcpListener listener) {
        synchronized (privateCtcpListeners) {
            privateCtcpListeners.remove(listener);
        }
    }

    void firePrivateCtcpDccChatReceived(CtcpDccChatEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.dccChatReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpDccSendReceived(CtcpDccSendEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.dccSendReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpSedReceived(CtcpSedEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.sedReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpActionReceived(CtcpActionEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.actionReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpPingRequestReceived(CtcpPingRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.pingRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpTimeRequestReceived(CtcpTimeRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.timeRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpVersionRequestReceived(CtcpVersionRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.versionRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpFingerRequestReceived(CtcpFingerRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.fingerRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpSourceRequestReceived(CtcpSourceRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.sourceRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpUserinfoRequestReceived(CtcpUserinfoRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.userinfoRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpClientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.clientinfoRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpErrmsgRequestReceived(CtcpErrmsgRequestEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.errmsgRequestReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpUnknownRequestEventReceived(
        CtcpUnknownRequestEvent event) {
        synchronized (ctcpListeners) {
            for (final CtcpListener ctcpListener : ctcpListeners) {
                try {
                    ctcpListener.unknownRequestEventReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpPingReplyReceived(CtcpPingReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.pingReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpTimeReplyReceived(CtcpTimeReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.timeReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpVersionReplyReceived(CtcpVersionReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.versionReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpFingerReplyReceived(CtcpFingerReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.fingerReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpSourceReplyReceived(CtcpSourceReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.sourceReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpUserinfoReplyReceived(CtcpUserinfoReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.userinfoReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpClientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.clientinfoReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpErrmsgReplyReceived(CtcpErrmsgReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.errmsgReplyReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    void firePrivateCtcpUnknownReplyEventReceived(
        CtcpUnknownReplyEvent event) {
        synchronized (privateCtcpListeners) {
            for (final CtcpListener privateCtcpListener : privateCtcpListeners) {
                try {
                    privateCtcpListener.unknownReplyEventReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }
    /* UnexpectedEventListener methods */

    public void addUnexpectedEventListener(UnexpectedEventListener listener) {
        synchronized (unexpectedEventListeners) {
            unexpectedEventListeners.add(listener);
        }
    }

    public void removeUnexpectedEventListener(UnexpectedEventListener listener) {
        synchronized (unexpectedEventListeners) {
            unexpectedEventListeners.remove(listener);
        }
    }

    void fireUnexpectedEventReceived(UnexpectedEvent event) {
        synchronized (privateMessageListeners) {
            for (final Object privateMessageListener : privateMessageListeners) {
                try {
                    ((UnexpectedEventListener) privateMessageListener).unexpectedEventReceived(event);
                } catch (Exception exc) {
                    handleException(exc);
                }
            }
        }
    }

    /**
     * Called by the fire-methods when the handler method of a listener throws an exception.
     */
    private void handleException(Exception exc) {
        exc.printStackTrace();
    }
    /* DefaultNickGenerator */

    private class DefaultNickGenerator implements NickGenerator {
        private int cnt = 0;

        public String createNewNick() {
            if (++cnt == 1) {
                return getNick() + "_";
            } else if (cnt == 2) {
                return "_" + getNick();
            } else {
                return null;
            }
        }
    }
}
