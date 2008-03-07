package org.schwering.irc.manager;

import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.manager.event.CTCPListener;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.ConnectionListener;
import org.schwering.irc.manager.event.ErrorEvent;
import org.schwering.irc.manager.event.InvitationEvent;
import org.schwering.irc.manager.event.MOTDEvent;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NumericEvent;
import org.schwering.irc.manager.event.PingEvent;
import org.schwering.irc.manager.event.PrivateMessageListener;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UnexpectedEvent;
import org.schwering.irc.manager.event.UnexpectedEventListener;
import org.schwering.irc.manager.event.UserModeEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;

/**
 * A wrapper for <code>IRCConnection</code> and interface to various
 * administration tasks of an IRC connection.
 * <p>
 * This class manages the wrapped <code>IRCConnection</code> object and
 * a set of the joined channels. Additionally, a connection is the point where 
 * <code>ConnectionListener</code>s, <code>CTCPListener</code>s,
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
	private NickGenerator nickGenerator = new DefaultNickGenerator();
	private Collection connectionListeners = new LinkedList();
	private Collection ctcpListeners = new LinkedList();
	private Collection privateMessageListeners = new LinkedList();
	private Collection unexpectedEventListeners = new LinkedList();
	
	// TODO what about CTCP? The messages MUST be parsed in BasicListener
	// already to fire the CTCP events, don't they?
	
	/**
	 * Initializes a new connection. This means that the internal
	 * <code>IRCConnection</code> is initialized with the connection data
	 * specified as constructor arguments and some other specific values.
	 * Further configuration can be done via the <code>Connection</code>
	 * class's methods.
	 */
	Connection(String host, int portMin, int portMax, String pass, String nick,
			String username, String realname) {
		conn = new IRCConnection(host, portMin, portMax, pass, nick, username, 
				realname);
		conn.setPong(true);
		conn.setColors(false);
		conn.addIRCEventListener(new BasicListener(this));
	}

	/**
	 * Initializes a new connection. This means that the internal
	 * <code>IRCConnection</code> is initalized with the connection data
	 * specified as constructor arguments and some other specific values.
	 * Further configuration can be done via the <code>Connection</code>
	 * class's methods.
	 */
	Connection(String host, int[] ports, String pass, String nick,
			String username, String realname) {
		conn = new IRCConnection(host, ports, pass, nick, username, realname);
		conn.setPong(true);
		conn.setColors(false);
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
	 * This method simply calls <code>IRCConnection.getNick</code>.
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
	 * This method simply calls <code>IRCConnection.getHost</code>.
	 * @see IRCConnection#getHost()
	 */
	public String getServerHostname() {
		return conn.getHost();
	}
	
	/**
	 * Returns the encoding of the connection.
	 * This method simply calls <code>IRCConnection.getEncoding</code>.
	 * @see IRCConnection#getEncoding()
	 */
	public String getEncoding() {
		return conn.getEncoding();
	}
	
	/**
	 * Sets the encoding of the connection.
	 * This method simply calls <code>IRCConnection.setEncoding</code>.
	 * @see IRCConnection#setEncoding(String)
	 */
	public void setEncoding(String encoding) {
		conn.setEncoding(encoding);
	}

	/**
	 * Returns the timeout of the connection.
	 * This method simply calls <code>IRCConnection.getTimeout</code>.
	 * @see IRCConnection#getTimeout()
	 */
	public int getTimeout() {
		return conn.getTimeout();
	}
	
	/**
	 * Sets the timeout of the connection.
	 * This method simply calls <code>IRCConnection.setTimeout</code>.
	 * @see IRCConnection#setTimeout(int)
	 */
	public void setTimeout(int millis) {
		conn.setTimeout(millis);
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
	 * Returns a set of <code>Channel</code> objects that the connection
	 * participates in.
	 */
	public Set getChannels() {
		return Collections.unmodifiableSet(channels.keySet());
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
	
	void fireConnectionEstablished(ConnectionEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).connectionEstablished(event);
		}
	}
	
	void fireConnectionLost(ConnectionEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).connectionLost(event);
		}
	}
	
	void fireErrorReceived(ErrorEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).errorReceived(event);
		}
	}
	
	void fireMotdReceived(MOTDEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).motdReceived(event);
		}
	}
	
	void firePingReceived(PingEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).pingReceived(event);
		}
	}
	
	void fireChannelJoined(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).channelJoined(event);
		}
	}
	
	void fireChannelLeft(UserParticipationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).channelLeft(event);
		}
	}
	
	void fireInvited(InvitationEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).invited(event);
		}
	}
	
	void fireNumericReplyReceived(NumericEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).numericReplyReceived(event);
		}
	}
	
	void fireNumericErrorReceived(NumericEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).numericErrorReceived(event);
		}
	}
	
	void fireUserModeReceived(UserModeEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).userModeReceived(event);
		}
	}
	
	void fireTopicReceived(TopicEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).topicReceived(event);
		}
	}
	
	void fireNamesReceived(NamesEvent event) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).namesReceived(event);
		}
	}
	
	/* PrivateMessageListener methods */
	
	public void addPrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.add(listener);
	}
	
	public void removevPrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.remove(listener);
	}
	
	void firePrivmsgReceived(MessageEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((PrivateMessageListener)it.next()).privmsgReceived(event);
		}
	}
	
	void fireNoticeReceived(MessageEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((PrivateMessageListener)it.next()).noticeReceived(event);
		}
	}
	
	/* CTCP listener methods (TODO: fireEvent() methods!) */
	
	public void addCTCPListener(CTCPListener listener) {
		ctcpListeners.add(listener);
	}
	
	public void removeCTCPListener(CTCPListener listener) {
		ctcpListeners.remove(listener);
	}
	
	/* UnexpectedEventListener methods */
	
	public synchronized void addUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.add(listener);
	}
	
	public synchronized void removeUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.remove(listener);
	}
	
	void fireUnexpectedEventReceived(UnexpectedEvent event) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((UnexpectedEventListener)it.next()).unexpectedEventReceived(event);
		}
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
