package org.schwering.irc.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.manager.event.ChannelListener;

/**
 * Represents an IRC channel. This object manages a list of users in the
 * channel, the channel's topic and a list of <code>ChannelListener</code>s.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Channel implements Comparable {
	private String name;
	private SortedMap users = new TreeMap();
	private Topic topic;
	private Collection listeners = new LinkedList();
	
	public Channel(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Set getUsers() {
		return users.entrySet();
	}
	
	public User getUser(String nick) {
		return (User)users.get(nick);
	}
	
	public boolean hasUser(User user) {
		return users.containsKey(user.getNick());
	}
	
	public boolean hasUser(String nick) {
		return users.containsKey(nick);
	}
	
	void addUser(User user) {
		users.put(user.getNick(), user);
	}
	
	void removeUser(User user) {
		users.remove(user.getNick());
	}
	
	void removeUser(String nick) {
		users.remove(nick);
	}
	
	public Topic getTopic() {
		return topic;
	}
	
	public int compareTo(Object other) {
		return getName().compareTo(((Channel)other).getName());
	}
	
	public String toString() {
		return name;
	}
	
	/* ChannelListener methods */
	
	public void addChannelListener(ChannelListener listener) {
		listeners.add(listener);
	}
	
	public void removeChannelListener(ChannelListener listener) {
		listeners.remove(listener);
	}
	
	void fireUserJoined(User user) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).userJoined(user);
		}
	}
	
	void fireUserLeft(User user, Message msg, int type) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).userLeft(user, msg, type);
		}
	}
	
	void fireTopicChanged(Topic topic) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).topicChanged(topic);
		}
	}
	
	void fireTopicChanged(IRCModeParser mode) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).modeChanged(mode);
		}
	}
	
	void fireNickChanged(User oldUser, User newUser) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).nickChanged(oldUser, newUser);
		}
	}
	
	void firePrivmsgReceived(User user, Message msg) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).privmsgReceived(user, msg);
		}
	}
	
	void fireNoticeReceived(User user, Message msg) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).noticeReceived(user, msg);
		}
	}
}
