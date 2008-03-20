package org.schwering.irc.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpClientinfoEvent;
import org.schwering.irc.manager.event.CtcpDccEvent;
import org.schwering.irc.manager.event.CtcpErrmsgEvent;
import org.schwering.irc.manager.event.CtcpFingerEvent;
import org.schwering.irc.manager.event.CtcpListener;
import org.schwering.irc.manager.event.CtcpPingEvent;
import org.schwering.irc.manager.event.CtcpSedEvent;
import org.schwering.irc.manager.event.CtcpSourceEvent;
import org.schwering.irc.manager.event.CtcpTimeEvent;
import org.schwering.irc.manager.event.CtcpUnknownEvent;
import org.schwering.irc.manager.event.CtcpUserinfoEvent;
import org.schwering.irc.manager.event.CtcpVersionEvent;
import org.schwering.irc.manager.event.ChannelListener;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.UserStatusEvent;
import org.schwering.irc.manager.event.WhoEvent;

/**
 * Represents an IRC channel. This object manages a list of users in the
 * channel, the channel's topic and a list of <code>ChannelListener</code>s
 * and a list of <code>CtcpListener</code>s which handle Ctcp events sent
 * to a whole channel.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Channel implements Comparable {
	public static final int NONE = 0;
	public static final int VOICED = 1;
	public static final int OPERATOR = 2;
	
	private String name;
	private SortedMap users = new TreeMap();
	private Topic topic;
	private List banIDs;
	private Collection channelListeners = new LinkedList();
	private Collection ctcpListeners = new LinkedList();
	
	// TODO administer channel modes 
	
	public Channel(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Collection getChannelUsers() {
		return Collections.unmodifiableCollection(users.values());
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
		users.put(user.getNick(), new ChannelUser(this, user));
	}
	
	void addUser(ChannelUser user) {
		users.put(user.getNick(), user);
	}
	
	ChannelUser removeUser(String nick) {
		return (ChannelUser) users.remove(nick);
	}
	
	ChannelUser removeUser(User user) {
		return (ChannelUser) users.remove(user.getNick());
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
	
	public List getBanIDs() {
		return Collections.unmodifiableList(banIDs);
	}
	
	void setBanIDs(List banIDs) {
		this.banIDs = banIDs;
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
		if (obj instanceof Channel) {
			return name.equalsIgnoreCase(((Channel)obj).name);
		} else {
			return false;
		}
	}

	public String toString() {
		return name;
	}
	
	/* ChannelListener methods */
	
	public synchronized void addChannelListener(ChannelListener listener) {
		channelListeners.add(listener);
	}
	
	public synchronized void removeChannelListener(ChannelListener listener) {
		channelListeners.remove(listener);
	}
	
	synchronized void fireUserJoined(UserParticipationEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).userJoined(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireUserLeft(UserParticipationEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).userLeft(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireUserStatusChanged(UserStatusEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).userStatusChanged(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireTopicReceived(TopicEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).topicReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireChannelModeReceived(ChannelModeEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).channelModeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNickChanged(NickEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).nickChanged(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireMessageReceived(MessageEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).messageReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNoticeReceived(MessageEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).noticeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireNamesReceived(NamesEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).namesReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireWhoReceived(WhoEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).whoReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireBanlistReceived(BanlistEvent event) {
		for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {
			try {
				((ChannelListener)it.next()).banlistReceived(event);
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
	
	synchronized void fireCtcpDccReceived(CtcpDccEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).dccReceived(event);
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
	
	synchronized void fireCtcpPingReceived(CtcpPingEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).pingReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpTimeReceived(CtcpTimeEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).timeReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpVersionReceived(CtcpVersionEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).versionReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpFingerReceived(CtcpFingerEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).fingerReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpSourceReceived(CtcpSourceEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).sourceReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUserinfoReceived(CtcpUserinfoEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).userinfoReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpClientinfoReceived(CtcpClientinfoEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).clientinfoReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpUnknownEventReceived(CtcpUnknownEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).unknownEventReceived(event);
			} catch (Exception exc) {
				handleException(exc);
			}
		}
	}
	
	synchronized void fireCtcpErrmsgReceived(CtcpErrmsgEvent event) {
		for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {
			try {
				((CtcpListener)it.next()).errmsgReceived(event);
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
}
