package org.schwering.irc.manager.event;

import java.util.EventListener;

/**
 * Listener for channel-related events. Examples are incoming messages, 
 * nick changes and joining and leaving users.
 * <p>
 * Events that are directly related to a channel the connection participates
 * in are processed by <code>ChannelListener</code>s.
 * However, events like an incoming topic cannot be clearly identified as
 * channel or connection event, because one can also request the topic of
 * channels one doesn't participate in. Therefore, the libraries general
 * strategy is to attribute events to channels if possible and fire them
 * with <code>ChannelListener</code>s. In the case of the topic-example,
 * this means that a <code>ChannelListener.topicReceived</code> is fired
 * if the connection participates in the channel and a 
 * <code>ConnectionListener.topicChanged</code> otherwise.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface ChannelListener extends EventListener {
	/**
	 * Fired when another user joins the channel.
	 */
	void userJoined(UserParticipationEvent event);

	/**
	 * Fired when another user leaves the channel. This can be either by
	 * parting, quitting or being kicked.
	 */
	void userLeft(UserParticipationEvent event);

	/**
	 * Fired when a topic of the channel was received.
	 * This is the case directly after joining the channel, when requesting
	 * the topic of the channel and when the channel's topic is changed.
	 */
	void topicReceived(TopicEvent event);

	/**
	 * Fired when a user in the channel changed his nickname.
	 * <p> 
	 * Note: (1) When this event is fired, the channel's user list is already
	 * updated, i.e. it doesn't contain <code>oldUser</code> but 
	 * <code>newUser</code> and <code>Channel.getUser(oldUser.getNick())</code> 
	 * doesn't return <code>oldUser</code>, and (2) the channels'
	 * <code>nickChanged</code> event is fired before the global
	 * <code>ConnectionListener.nickChanged</code> event. 
	 */
	void nickChanged(NickEvent event);
	
	void channelModeReceived(ChannelModeEvent event);
	
	void privmsgReceived(MessageEvent event);
	
	void noticeReceived(MessageEvent event);
	
	/**
	 * Fired when a NAMES list was received and the connection participates
	 * in the channel.
	 */
	void namesReceived(NamesEvent event);
	
	/**
	 * Fired when the status of a user changed. This can be triggered by
	 * an incoming NAMES list, for example, or an incoming MODE command.
	 * <p>
	 * However, this is just an additional event to the existing 
	 * <code>namesReceived</code> and <code>channelModeReceived</code> events.
	 * Note that when the connection joins an empty channel, it automatically
	 * gets operator status. This is not the result of a MODE command 
	 * executed by anyone, it is implicitly. However, this information is
	 * included in the NAMES list received when joining a channel and therefore
	 * propagated with this event.
	 * <p>
	 * This event is fired after the <code>channelModeChanged</code> event,
	 * but before <code>namesReceived</code> is fired.
	 */
	void userStatusChanged(UserStatusEvent event);
	
	/**
	 * Fired when the banlist is received and the connection participates
	 * in the channel.
	 */
	void banlistReceived(BanlistEvent event);
}
