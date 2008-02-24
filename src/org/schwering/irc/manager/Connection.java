package org.schwering.irc.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.manager.event.CTCPListener;
import org.schwering.irc.manager.event.ConnectionListener;
import org.schwering.irc.manager.event.PrivateMessageListener;
import org.schwering.irc.manager.event.UnexpectedEventListener;

public class Connection {
	private IRCConnection conn;
	private SortedMap channels = new TreeMap();
	private boolean filterCTCP = true;
	private boolean requestModes = true;
	private Collection connectionListeners = new LinkedList();
	private Collection ctcpListeners = new LinkedList();
	private Collection privateMessageListeners = new LinkedList();
	private Collection unexpectedEventListeners = new LinkedList();
	
	Connection(String host, int portMin, int portMax, String pass, String nick,
			String username, String realname) {
		conn = new IRCConnection(host, portMin, portMax, pass, nick, username, 
				realname);
	}

	Connection(String host, int[] ports, String pass, String nick,
			String username, String realname) {
		conn = new IRCConnection(host, ports, pass, nick, username, realname);
	}

	public boolean getFilterCTCP() {
		return filterCTCP;
	}
	
	public void setFilterCTCP(boolean filterCTCP) {
		this.filterCTCP = filterCTCP;
	}
	
	public boolean getRequestModes() {
		return requestModes;
	}
	
	public void setRequestModes(boolean requestModes) {
		this.requestModes = requestModes;
	}
	
	public Set getChannels() {
		return Collections.unmodifiableSet(channels.keySet());
	}
	
	public Channel getChannel(String channel) {
		return (Channel)channels.get(channel);
	}
	
	public void addIRCEventListener(IRCEventListener listener) {
		conn.addIRCEventListener(listener);
	}
	
	public void removeIRCEventListener(IRCEventListener listener) {
		conn.removeIRCEventListener(listener);
	}
	
	public void addConnectionListener(ConnectionListener listener) {
		connectionListeners.add(listener);
	}
	
	public void removeConnectionListener(ConnectionListener listener) {
		connectionListeners.remove(listener);
	}
	
	void fireConnectionEstablished() {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).connectionEstablished();
		}
	}
	
	void fireConnectionLost() {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).connectionLost();
		}
	}
	
	void fireErrorReceived(Message msg) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).errorReceived(msg);
		}
	}
	
	void fireMotdReceived(String[] motd) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).motdReceived(motd);
		}
	}
	
	void firePingReceived(Message msg) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).pingReceived(msg);
		}
	}
	
	void fireChannelJoined(Channel channel) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).channelJoined(channel);
		}
	}
	
	void fireChannelLeft(Channel channel) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).channelLeft(channel);
		}
	}
	
	void fireInvited(Channel channel, User user) {
		for (Iterator it = connectionListeners.iterator(); it.hasNext(); ) {
			((ConnectionListener)it.next()).invited(channel, user);
		}
	}
	
	public void addPrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.add(listener);
	}
	
	public void removevPrivateMessageListener(PrivateMessageListener listener) {
		privateMessageListeners.remove(listener);
	}
	
	void firePrivmsgReceived(User user, Message msg) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((PrivateMessageListener)it.next()).privmsgReceived(user, msg);
		}
	}
	
	void fireNoticeReceived(User user, Message msg) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((PrivateMessageListener)it.next()).noticeReceived(user, msg);
		}
	}
	
	public void addCTCPListener(CTCPListener listener) {
		ctcpListeners.add(listener);
	}
	
	public void removeCTCPListener(CTCPListener listener) {
		ctcpListeners.remove(listener);
	}
	
	public void addUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.add(listener);
	}
	
	public void removeUnexpectedEventListener(UnexpectedEventListener listener) {
		unexpectedEventListeners.remove(listener);
	}
	
	void fireUnexpectedEventReceived(String event, Object[] args) {
		for (Iterator it = privateMessageListeners.iterator(); it.hasNext(); ) {
			((UnexpectedEventListener)it.next()).unexpectedEventReceived(event, args);
		}
	}
	
}
