package org.schwering.irc.manager;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCConstants;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.ssl.SSLIRCConnection;
import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpClientinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpDccChatEvent;
import org.schwering.irc.manager.event.CtcpDccSendEvent;
import org.schwering.irc.manager.event.CtcpErrmsgRequestEvent;
import org.schwering.irc.manager.event.CtcpFingerRequestEvent;
import org.schwering.irc.manager.event.CtcpListener;
import org.schwering.irc.manager.event.CtcpSedEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.ConnectionListener;
import org.schwering.irc.manager.event.CtcpSourceRequestEvent;
import org.schwering.irc.manager.event.CtcpUnknownRequestEvent;
import org.schwering.irc.manager.event.CtcpUnknownReplyEvent;
import org.schwering.irc.manager.event.CtcpVersionRequestEvent;
import org.schwering.irc.manager.event.CtcpUserinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpClientinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpErrmsgReplyEvent;
import org.schwering.irc.manager.event.CtcpFingerReplyEvent;
import org.schwering.irc.manager.event.CtcpPingRequestEvent;
import org.schwering.irc.manager.event.CtcpPingReplyEvent;
import org.schwering.irc.manager.event.CtcpSourceReplyEvent;
import org.schwering.irc.manager.event.CtcpTimeRequestEvent;
import org.schwering.irc.manager.event.CtcpTimeReplyEvent;
import org.schwering.irc.manager.event.CtcpUserinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpVersionReplyEvent;
import org.schwering.irc.manager.event.ErrorEvent;
import org.schwering.irc.manager.event.InfoEvent;
import org.schwering.irc.manager.event.InvitationEvent;
import org.schwering.irc.manager.event.LinksEvent;
import org.schwering.irc.manager.event.ListEvent;
import org.schwering.irc.manager.event.MotdEvent;
import org.schwering.irc.manager.event.MessageEvent;
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
 * A wrapper for <code>IRCConnection</code> and interface to various
 * administration tasks of an IRC connection.
 * <p>
 * This class manages the wrapped <code>IRCConnection</code> object and
 * a set of the joined channels. Additionally, a connection is the point where 
 * <code>ConnectionListener</code>s, <code>CtcpListener</code>s,
 * <code>PrivateMessageListener</code>s, and
 * <code>UnexpectedEventListener</code>s are registered.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Connection {
	private IRCConnection conn;
	private SortedMap channels = new TreeMap();
	private boolean requestModes = true;
	private boolean colorsEnabled = true;
	private boolean ctcpEnabled = true;
	private NickGenerator nickGenerator = new DefaultNickGenerator();
	private List connectionListeners = new LinkedList();
	private List ctcpListeners = new LinkedList();
	private List privateCtcpListeners = new LinkedList();
	private List privateMessageListeners = new LinkedList();
	private List unexpectedEventListeners = new LinkedList();
	
	/**
	 * Initializes a new connection. This means that the internal
	 * <code>IRCConnection</code> is initialized with the connection data
	 * specified as constructor arguments and some other specific values.
	 * Further configuration can be done via the <code>Connection</code>
	 * class's methods.
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
	 * Initializes a new connection. This means that the internal
	 * <code>IRCConnection</code> is initalized with the connection data
	 * specified as constructor arguments and some other specific values.
	 * Further configuration can be done via the <code>Connection</code>
	 * class's methods.
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
	 * Returns the connected user's current nickname.
	 * This method simply calls <code>IRCConnection.getNick()</code>.
	 * <p>
	 * Note: The IRC server might implicitly change the originally set
	 * nickname. This done when the set nick contains illegal characters
	 * or is too long, for example. Therefore one should always use
	 * this method to obtain the current nickname instead of storing it
	 * once and accessing this stored string which might then be not equal
	 * to the real nickname.
	 * @see IRCConnection#getNick()
	 */
	public String getNick() {
		return conn.getNick();
	}
	
	/**
	 * Returns the IRC server's hostname.
	 * This method simply calls <code>IRCConnection.getHost()</code>.
	 * @see IRCConnection#getHost()
	 */
	public String getServerHostname() {
		return conn.getHost();
	}
	
	/**
	 * Returns the encoding of the connection.
	 * This method simply calls <code>IRCConnection.getEncoding()</code>.
	 * @see IRCConnection#getEncoding()
	 */
	public String getEncoding() {
		return conn.getEncoding();
	}
	
	/**
	 * Sets the encoding of the connection.
	 * This method simply calls <code>IRCConnection.setEncoding()</code>.
	 * @see IRCConnection#setEncoding(String)
	 */
	public void setEncoding(String encoding) {
		conn.setEncoding(encoding);
	}

	/**
	 * Returns the timeout of the connection.
	 * This method simply calls <code>IRCConnection.getTimeout()</code>.
	 * @see IRCConnection#getTimeout()
	 */
	public int getTimeout() {
		return conn.getTimeout();
	}
	
	/**
	 * Sets the timeout of the connection.
	 * This method simply calls <code>IRCConnection.setTimeout()</code>.
	 * @see IRCConnection#setTimeout(int)
	 */
	public void setTimeout(int millis) {
		conn.setTimeout(millis);
	}
	
	/**
	 * Returns the local host of the connected socket.
	 * This method simply calls <code>IRCConnection.getTimeout()</code>.
	 * @see IRCConnection#getLocalAddress()
	 */
	public InetAddress getLocalAddress() {
		return conn.getLocalAddress();
	}
	
	/**
	 * Enables or disables debugging.
	 * This method simply calls <code>IRCConnection.setDebug()</code>.
	 * @see IRCConnection#setDebug(boolean)
	 */
	public void setDebug(boolean debug) {
		conn.setDebug(debug);
	}
	
	/**
	 * Sets the debug stream. <code>null</code> (default) means 
	 * <code>System.out</code>.
	 * This method simply calls <code>IRCConnection.setDebugStream()</code>.
	 * @see IRCConnection#setDebugStream(PrintStream)
	 */
	public void setDebugStream(PrintStream debugStream) {
		conn.setDebugStream(debugStream);
	}
	
	/**
	 * Returns the current nickname generator.
	 * <p>
	 * The nickname generator is needed because the server might ask for a
	 * new nickname when we try to establish the connection. This question
	 * for a new nickname must be answered somehow, and this task is
	 * delivered to the nickname generator.
	 * <p>
	 * The nick generator is initialized with a default one which behaves
	 * as follows: the first invocation returns the originally set nickname
	 * plus an underscore (i.e. "Peter_"), the second invocation returns
	 * a leading underscore (i.e. "_Peter_") and all subsequent invocations
	 * return <code>null</code> which will kill the connection.
	 */
	public NickGenerator getNickGenerator() {
		return nickGenerator;
	}
	
	/**
	 * Sets the nickname generator.
	 * <p>
	 * The nickname generator is needed because the server might ask for a
	 * new nickname when we try to establish the connection. This question
	 * for a new nickname must be answered somehow, and this task is
	 * delivered to the nickname generator.
	 * <p>
	 * The nick generator is initialized with a default one which behaves
	 * as follows: the first invocation returns the originally set nickname
	 * plus an underscore (i.e. "Peter_"), the second invocation returns
	 * a leading underscore (i.e. "_Peter_") and all subsequent invocations
	 * return <code>null</code> which will kill the connection.
	 */
	public void setNickGenerator(NickGenerator nickGen) {
		this.nickGenerator = nickGen;
	}
	
	/**
	 * Starts the connection.
	 * This method simply calls <code>IRCConnection.connect</code>.
	 * @throws IOException If an I/O error occurs.
	 * @throws SocketException If the connect method was already invoked.
	 */
	public void connect() throws IOException, SocketException {
		conn.connect();
	}
	
	/**
	 * Indicates whether the connection is alive. This is the case if
	 * <code>connect</code> has been invoked. Note that <code>isConnected</code>
	 * being <code>true</code> does not necessarily require 
	 * <code>ConnectionListener.connectionEstablished</code> to be fired.
	 * This method simply calls <code>IRCConnection.isConnected</code>.
	 * @see #connect()
	 * @see IRCConnection#isConnected()
	 */
	public boolean isConnected() {
		return conn.isConnected();
	}
	
	/**
	 * Sends a line to the IRC server.
	 * @param line The line to be sent.
	 */
	public void send(String line) {
		conn.send(line);
	}
	
	/**
	 * Sends a PRIVMSG to a user or a channel.
	 * <p>
	 * This is equivalent to <code>send("PRIVMSG "+ dest + " :"+
	 * CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));</code>.
	 * @param dest Either a channel or a nickname.
	 * @param msg The message itself.
	 */
	public void sendPrivmsg(String dest, String msg) {
		send("PRIVMSG "+ dest + " :"+ 
				CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));
	}

	/**
	 * Sends a NOTICE to a user or a channel.
	 * <p>
	 * This is equivalent to <code>send("NOTICE "+ dest + " :"+
	 * CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));</code>.
	 * @param dest Either a channel or a nickname.
	 * @param msg The message itself.
	 */
	public void sendNotice(String dest, String msg) {
		send("NOTICE "+ dest + " :"+ 
				CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(msg)));
	}

	/**
	 * Sends a CTCP request. A CTCP reply is always sent as PRIVMSG.
	 * @param dest Either a channel name or a nickname.
	 * @param command The CTCP command (e.g. VERSION).
	 */
	public void sendCtcpRequest(String dest, String command) {
		send("PRIVMSG "+ dest + " :"+ 
				IRCConstants.CTCP_DELIMITER 
				+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command))
				+ IRCConstants.CTCP_DELIMITER);
	}
	
	/**
	 * Sends a CTCP request. A CTCP reply is always sent as PRIVMSG.
	 * <p>
	 * This is equivalent to <code>sendPrivmsg(dest, IRCConstants.CTCP_DELIMITER 
	 * + command +" "+ args + IRCConstants.CTCP_DELIMITER);</code>.
	 * @param dest Either a channel name or a nickname.
	 * @param command The CTCP command (e.g. VERSION).
	 * @param args The CTCP command's arguments.
	 */
	public void sendCtcpRequest(String dest, String command, String args) {
		String tmp;
		if (args != null && args.length() > 0) {
			tmp = " "+ args;
		} else {
			tmp = "";
		}
		send("PRIVMSG "+ dest + " :"+ 
				IRCConstants.CTCP_DELIMITER 
				+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command + tmp))
				+ IRCConstants.CTCP_DELIMITER);
	}

	/**
	 * Sends a CTCP command. A CTCP reply is always sent as PRIVMSG.
	 * <p>
	 * This is equivalent to <code>sendCtcpRequest(dest, command, args);</code>.
	 * @param dest Either a channel name or a nickname.
	 * @param command The CTCP command (e.g. ACTION).
	 * @param args The CTCP command's arguments.
	 */
	public void sendCtcpCommand(String dest, String command, String args) {
		sendCtcpRequest(dest, command, args);
	}

	/**
	 * Sends a CTCP reply. A CTCP reply is always sent as NOTICE, because
	 * as a rule, PRIVMSGs (incoming CTCP requests) should never trigger
	 * PRIVMSG replies.
	 * <p>
	 * This is equivalent to <code>sendNotice(dest, IRCConstants.CTCP_DELIMITER 
	 * + command + IRCConstants.CTCP_DELIMITER);</code>.
	 * @param dest Either a channel name or a nickname.
	 * @param command The CTCP command (e.g. VERSION).
	 */
	public void sendCtcpReply(String dest, String command) {
		send("NOTICE "+ dest + " :"+ 
				IRCConstants.CTCP_DELIMITER 
				+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command))
				+ IRCConstants.CTCP_DELIMITER);
	}

	/**
	 * Sends a CTCP reply. A CTCP reply is always sent as NOTICE, because
	 * as a rule, PRIVMSGs (incoming CTCP requests) should never trigger
	 * PRIVMSG replies.
	 * <p>
	 * This is equivalent to <code>sendNotice(dest, IRCConstants.CTCP_DELIMITER 
	 * + command +" "+ args + IRCConstants.CTCP_DELIMITER);</code>.
	 * @param dest Either a channel name or a nickname.
	 * @param command The CTCP command (e.g. VERSION).
	 * @param args The CTCP command's arguments.
	 */
	public void sendCtcpReply(String dest, String command, String args) {
		String tmp;
		if (args != null && args.length() > 0) {
			tmp = " "+ args;
		} else {
			tmp = "";
		}
		send("NOTICE "+ dest + " :"+ 
				IRCConstants.CTCP_DELIMITER 
				+ CtcpUtil.lowQuote(CtcpUtil.ctcpQuote(command + tmp))
				+ IRCConstants.CTCP_DELIMITER);
	}
	
	/**
	 * Sends a DCC chat invitation. As address, the value returned by
	 * <code>IRCConnection.getLocalAddress()</code> is taken.
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
	 * @param dest The receiver, i.e. a nickname or channel.
	 * @param fileName The file's name.
	 * @param addr The host of the DCC chat (typically the own address).
	 * @param port The port the DCC chat host is listening to.
	 * @param size The file size.
	 */
	public void sendDccSend(String dest, String fileName, InetAddress addr, 
			int port, long size) {
		long address = CtcpUtil.convertInetAddressToLong(addr);
		sendCtcpCommand(dest, "DCC", "SEND "+ fileName +" "+ address +" "+ port +" "+ size);
	}

	/**
	 * Sends a DCC chat invitation. As address, the value returned by
	 * <code>IRCConnection.getLocalAddress()</code> is taken.
	 * @param dest The receiver, i.e. a nickname or channel.
	 * @param port The port the DCC chat host is listening to.
	 */
	public void sendDccChat(String dest, int port) {
		sendDccChat(dest, conn.getLocalAddress(), port);
	}

	/**
	 * Sends a DCC chat invitation.
	 * @param dest The receiver, i.e. a nickname or channel.
	 * @param addr The host of the DCC chat (typically the own address).
	 * @param port The port the DCC chat host is listening to.
	 */
	public void sendDccChat(String dest, InetAddress addr, int port) {
		long address = CtcpUtil.convertInetAddressToLong(addr);
		sendCtcpCommand(dest, "DCC", "CHAT chat "+ address +" "+ port);
	}

	/**
	 * Indicates whether directly after joining a channel, the channel modes
	 * are requested automatically.
	 * By default, this is <code>true</code>.
	 */
	public boolean getRequestModes() {
		return requestModes;
	}
	
	/**
	 * Indicates whether directly after joining a channel, the channel modes
	 * are requested automatically.
	 * By default, this is <code>true</code>.
	 */
	public void setRequestModes(boolean requestModes) {
		this.requestModes = requestModes;
	}
	
	/**
	 * Enables or disables colors. By colors, the mIRC color codes are meant.
	 * If they're disabled, these color codes are removed from all incoming
	 * messages (NOTICE, PRIVMSG and KICK, PART, QUIT messages).
	 * By default, <code>enableColors</code> is <code>true</code>.
	 */
	public void setColors(boolean enableColors) {
		this.colorsEnabled = enableColors;
	}
	
	/**
	 * Indicates whether colors are enabled. By colors, the mIRC color codes 
	 * are meant. By default, <code>isColorsEnabled()</code> is 
	 * <code>true</code>.
	 * @return <code>false</code> means that color codes are removed from
	 * all incoming messages (NOTICE, PRIVMSG and KICK, PART, QUIT messages).
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
	 * Returns a set of <code>Channel</code> objects that the connection
	 * participates in.
	 */
	public Collection getChannels() {
		return Collections.unmodifiableCollection(channels.values());
	}
	
	/**
	 * Returns <code>true</code> if the connection participates in the
	 * channel.
	 */
	public boolean hasChannel(String channelName) {
		return channels.containsKey(channelName);
	}
	
	/**
	 * Returns <code>true</code> if the connection participates in the
	 * channel.
	 */
	public boolean hasChannel(Channel channel) {
		return channels.containsKey(channel.getName());
	}
	
	/**
	 * Clears the channel map.
	 */
	synchronized void clearChannels() {
		channels.clear();
	}
	
	/**
	 * Stores a new channel in the map.
	 */
	synchronized void addChannel(Channel channel) {
		channels.put(channel.getName(), channel);
	}
	
	/**
	 * Removes a channel in the map.
	 */
	synchronized void removeChannel(String channel) {
		channels.remove(channel);
	}
	
	/**
	 * Removes a channel in the map.
	 */
	void removeChannel(Channel channel) {
		channels.remove(channel.getName());
	}
	
	/**
	 * Returns a <code>Channel</code> object that contains all known 
	 * information about the channel. This is rather empty if the connection
	 * has not joined the respective channel.
	 */
	public Channel resolveChannel(String channelName) {
		if (channelName == null) {
			throw new IllegalArgumentException();
		}
		Channel channel = (Channel)channels.get(channelName);
		return channel != null ? channel : new Channel(channelName);
	}
	
	/**
	 * Returns a <code>User</code> object that contains all known 
	 * information about a user. The <code>User</code> object may contain 
	 * further information like the user's host, username and away status.
	 * <p>
	 * First, the method searches a <code>User</code> object in the joined
	 * channels. If this fails, a new <code>User</code> is created.
	 */
	public User resolveUser(String nick) {
		if (nick == null) {
			throw new IllegalArgumentException();
		}
		for (Iterator it = getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			ChannelUser user = channel.getUser(nick);
			if (user != null) {
				return user.getUser();
			}
		}
		return new User(nick);
	}
	
	/**
	 * Returns a <code>User</code> object that contains all known 
	 * information about a user. The <code>User</code> object may contain 
	 * further information like the user's host, username and away status.
	 * <p>
	 * First, the method searches a <code>User</code> object in the joined
	 * channels. If this fails, a new <code>User</code> is created.
	 */
	public User resolveUser(IRCUser ircUser) {
		if (ircUser == null) {
			throw new IllegalArgumentException();
		}
		for (Iterator it = getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			ChannelUser user = channel.getUser(ircUser.getNick());
			if (user != null) {
				user.update(ircUser);
				return user.getUser();
			}
		}
		return new User(ircUser);
	}
	
	/**
	 * Returns <code>true</code> if the user is the one represented by this
	 * connection.
	 */
	public boolean isMe(User user) {
		return user.isSame(conn.getNick());
	}
	
	/**
	 * Adds a pure <code>IRCEventListener</code> to the pure
	 * <code>IRCConnection</code>.
	 */
	public synchronized void addIRCEventListener(IRCEventListener listener) {
		conn.addIRCEventListener(listener);
	}
	
	/**
	 * Removes a pure <code>IRCEventListener</code> from the pure
	 * <code>IRCConnection</code>.
	 */
	public synchronized void removeIRCEventListener(IRCEventListener listener) {
		conn.removeIRCEventListener(listener);
	}
	
	/* ConnectionListener methods */
	
	public synchronized void addConnectionListener(ConnectionListener listener) {
		connectionListeners.add(listener);
	}
	
	public synchronized void removeConnectionListener(ConnectionListener listener) {
		connectionListeners.remove(listener);
	}
	
	synchronized void fireConnectionEstablished(ConnectionEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).connectionEstablished(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireConnectionLost(ConnectionEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).connectionLost(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireErrorReceived(ErrorEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).errorReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireMotdReceived(MotdEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).motdReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireInfoReceived(InfoEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).infoReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireLinksReceived(LinksEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).linksReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireStatsReceived(StatsEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).statsReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePingReceived(PingEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).pingReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireChannelJoined(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).channelJoined(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireChannelLeft(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).channelLeft(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireInvitationReceived(InvitationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).invitationReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireInvitationDeliveryReceived(InvitationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).invitationDeliveryReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNumericReplyReceived(NumericEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).numericReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNumericErrorReceived(NumericEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).numericErrorReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireUserModeReceived(UserModeEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).userModeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireWhoisReceived(WhoisEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).whoisReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireWhowasReceived(WhowasEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).whowasReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireUserJoined(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).userJoined(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireUserLeft(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).userLeft(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireTopicReceived(TopicEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).topicReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireListReceived(ListEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).listReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireChannelModeReceived(ChannelModeEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).channelModeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNickChanged(NickEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).nickChanged(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireMessageReceived(MessageEvent event) {
	 	for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).messageReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNoticeReceived(MessageEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).noticeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNamesReceived(NamesEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).namesReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireWhoReceived(WhoEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).whoReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireBanlistReceived(BanlistEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			try {
				((ConnectionListener)it.next()).banlistReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	/* PrivateMessageListener methods */
	
	public synchronized void addPrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.add(listener);
	}
	
	public synchronized void removePrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.remove(listener);
	}
	
	synchronized void firePrivateMessageReceived(MessageEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			try {
				((PrivateMessageListener)it.next()).messageReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateNoticeReceived(MessageEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			try {
				((PrivateMessageListener)it.next()).noticeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	/* Ctcp listener methods */
	
	public synchronized void addCtcpListener(CtcpListener listener) {
		ctcpListeners.add(listener);
	}
	
	public synchronized void removeCtcpListener(CtcpListener listener) {
		ctcpListeners.remove(listener);
	}
	
	synchronized void fireCtcpDccChatReceived(CtcpDccChatEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).dccChatReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpDccSendReceived(CtcpDccSendEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).dccSendReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpSedReceived(CtcpSedEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sedReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpActionReceived(CtcpActionEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).actionReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpPingRequestReceived(CtcpPingRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).pingRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpTimeRequestReceived(CtcpTimeRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).timeRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpVersionRequestReceived(CtcpVersionRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).versionRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpFingerRequestReceived(CtcpFingerRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).fingerRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpSourceRequestReceived(CtcpSourceRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sourceRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUserinfoRequestReceived(CtcpUserinfoRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).userinfoRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpClientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).clientinfoRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpErrmsgRequestReceived(CtcpErrmsgRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).errmsgRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUnknownRequestEventReceived(
			CtcpUnknownRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).unknownRequestEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpPingReplyReceived(CtcpPingReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).pingReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpTimeReplyReceived(CtcpTimeReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).timeReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpVersionReplyReceived(CtcpVersionReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).versionReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpFingerReplyReceived(CtcpFingerReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).fingerReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpSourceReplyReceived(CtcpSourceReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sourceReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUserinfoReplyReceived(CtcpUserinfoReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).userinfoReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpClientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).clientinfoReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpErrmsgReplyReceived(CtcpErrmsgReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).errmsgReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUnknownReplyEventReceived(
			CtcpUnknownReplyEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).unknownReplyEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	/* private Ctcp listener methods */
	
	public synchronized void addPrivateCtcpListener(CtcpListener listener) {
		privateCtcpListeners.add(listener);
	}
	
	public synchronized void removePrivateCtcpListener(CtcpListener listener) {
		privateCtcpListeners.remove(listener);
	}
	
	synchronized void firePrivateCtcpDccChatReceived(CtcpDccChatEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).dccChatReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpDccSendReceived(CtcpDccSendEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).dccSendReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpSedReceived(CtcpSedEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sedReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpActionReceived(CtcpActionEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).actionReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpPingRequestReceived(CtcpPingRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).pingRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpTimeRequestReceived(CtcpTimeRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).timeRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpVersionRequestReceived(CtcpVersionRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).versionRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpFingerRequestReceived(CtcpFingerRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).fingerRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpSourceRequestReceived(CtcpSourceRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sourceRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpUserinfoRequestReceived(CtcpUserinfoRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).userinfoRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpClientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).clientinfoRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpErrmsgRequestReceived(CtcpErrmsgRequestEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).errmsgRequestReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpUnknownRequestEventReceived(
			CtcpUnknownRequestEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).unknownRequestEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpPingReplyReceived(CtcpPingReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).pingReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpTimeReplyReceived(CtcpTimeReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).timeReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpVersionReplyReceived(CtcpVersionReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).versionReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpFingerReplyReceived(CtcpFingerReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).fingerReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpSourceReplyReceived(CtcpSourceReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sourceReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpUserinfoReplyReceived(CtcpUserinfoReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).userinfoReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpClientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).clientinfoReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpErrmsgReplyReceived(CtcpErrmsgReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).errmsgReplyReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void firePrivateCtcpUnknownReplyEventReceived(
			CtcpUnknownReplyEvent event) {
		for (Iterator it = privateCtcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).unknownReplyEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	/* UnexpectedEventListener methods */
	
	public synchronized void addUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.add(listener);
	}
	
	public synchronized void removeUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.remove(listener);
	}
	
	synchronized void fireUnexpectedEventReceived(UnexpectedEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			try {
				((UnexpectedEventListener)it.next()).unexpectedEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	/**
	 * Called by the fire-methods when the handler method of a listener 
	 * throws an exception.
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
