package org.schwering.irc.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.manager.event.ChannelListener;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;

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
	
	// TODO implement a queue-like system that buffers respondings that
	// belong to another, e.g. parts of a WHOIS or of a topic when joining
	// a channel (ResponseBuffer) (note: WHOIS x,y,z has only one 
	// end of whois reply)
	
	// TODO an alternative system: a ResponseBuffer that knows one or multiple
	// special reply-numbers that indicate beginning respectively ending
	
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
	
	void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	public int compareTo(Object other) {
		return getName().compareTo(((Channel)other).getName());
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
}
