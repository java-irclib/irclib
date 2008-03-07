package org.schwering.irc.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.manager.event.ChannelListener;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.UserStatusEvent;

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
	
	// TODO administer modes (and banlist) of users in channel
	
	public Channel(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Set getChannelUsers() {
		return Collections.unmodifiableSet(users.entrySet());
	}
	
	public ChannelUser getUser(String nick) {
		return (ChannelUser)users.get(nick);
	}
	
	public ChannelUser getUser(User user) {
		return (ChannelUser)users.get(user.getNick());
	}
	
	public boolean hasUser(String nick) {
		return users.containsKey(nick);
	}
	
	public boolean hasUser(User user) {
		return users.containsKey(user.getNick());
	}
	
	void addUser(User user) {
		users.put(user.getNick(), new ChannelUser(user));
	}
	
	void addUser(ChannelUser user) {
		users.put(user.getNick(), user);
	}
	
	void removeUser(String nick) {
		users.remove(nick);
	}
	
	void removeUser(User user) {
		users.remove(user.getNick());
	}
	
	void setUserStatus(String nick, int status) {
		ChannelUser channelUser = (ChannelUser)users.get(nick);
		if (channelUser != null) {
			channelUser.setStatus(status);
		}
	}
	
	/**
	 * Returns the user's status or -1 if the user is not found.
	 * Valid user status can be <code>UserStatusPair</code>'s
	 * <code>NONE</code>, <code>OPERATOR</code>, <code>VOICED</code>. 
	 */
	public int getUserStatus(String nick) {
		ChannelUser channelUser= (ChannelUser)users.get(nick);
		if (channelUser != null) {
			return channelUser.getStatus();
		} else {
			return -1;
		}
	}
	
	void setUserStatus(User user, int status) {
		ChannelUser channelUser = (ChannelUser)users.get(user.getNick());
		if (channelUser != null) {
			channelUser.setStatus(status);
		}
	}
	
	/**
	 * Returns the user's status or -1 if the user is not found.
	 * Valid user status can be <code>UserStatusPair</code>'s
	 * <code>NONE</code>, <code>OPERATOR</code>, <code>VOICED</code>. 
	 */
	public int getUserStatus(User user) {
		ChannelUser channelUser= (ChannelUser)users.get(user.getNick());
		if (channelUser != null) {
			return channelUser.getStatus();
		} else {
			return -1;
		}
	}
	
	public Topic getTopic() {
		return topic;
	}
	
	void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	public int compareTo(Object other) {
		return getName().compareToIgnoreCase(((Channel)other).getName());
	}
	
	public boolean isSame(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Channel) {
			return name.equalsIgnoreCase(((Channel)obj).name);
		} else if (obj instanceof String) {
			return name.equalsIgnoreCase((String)obj);
		} else {
			return false;
		}
	}
	
	public boolean equals(Object obj) {
		return name.equalsIgnoreCase(((Channel)obj).name);
	}

	public String toString() {
		return name;
	}
	
	/* ChannelListener methods */
	
	public synchronized void addChannelListener(ChannelListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeChannelListener(ChannelListener listener) {
		listeners.remove(listener);
	}
	
	void fireUserJoined(UserParticipationEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).userJoined(event);
		}
	}
	
	void fireUserLeft(UserParticipationEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).userLeft(event);
		}
	}
	
	void fireUserStatusChanged(UserStatusEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).userStatusChanged(event);
		}
	}
	
	void fireTopicReceived(TopicEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).topicReceived(event);
		}
	}
	
	void fireChannelModeReceived(ChannelModeEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).channelModeReceived(event);
		}
	}
	
	void fireNickChanged(NickEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).nickChanged(event);
		}
	}
	
	void firePrivmsgReceived(MessageEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).privmsgReceived(event);
		}
	}
	
	void fireNoticeReceived(MessageEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).noticeReceived(event);
		}
	}
	
	void fireNamesReceived(NamesEvent event) {
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			((ChannelListener)it.next()).namesReceived(event);
		}
	}
}
