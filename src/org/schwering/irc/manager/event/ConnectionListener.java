package org.schwering.irc.manager.event;

import java.util.EventListener;

/**
 * A connection listener listens for general events on connection level.
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
public interface ConnectionListener extends EventListener {
	/**
	 * Fired when the connection is registered successfully. At this point
	 * of time, the connecting user is fully-privileged.
	 */
	void connectionEstablished(ConnectionEvent event);

	/**
	 * Fired when the connection has been terminated is completely dead.
	 */
	void connectionLost(ConnectionEvent event);
	
	void errorReceived(ErrorEvent event);

	void motdReceived(MOTDEvent event);

	/**
	 * Fired when the server asked for a ping pong.
	 * <p>
	 * Note: You don't have to answer the request, this is done automatically.
	 */
	void pingReceived(PingEvent event);
	
	/**
	 * Fired when you've joined a channel.
	 */
	void channelJoined(UserParticipationEvent channel);

	/**
	 * Fired when you've left a channel, either by parting or being kicked.
	 * @param channel
	 */
	void channelLeft(UserParticipationEvent channel);
	
	/**
	 * Fired when someone is invited by someone else.
	 * <p>
	 * Note: The invited user is always you.
	 */
	void invited(InvitationEvent event);
	
	/**
	 * Fired when a numeric reply is received.
	 * <p>
	 * Note: Many queries are answered with numeric replies, for example
	 * WHOIS, WHO, NAMES. The topic is transmitted via numeric replies when
	 * joining a channel, too. Since, for example, these topic replies are
	 * processed by this library and lead to <code>topicReceived</code>
	 * events of the <code>ChannelListener</code> or this listener, you 
	 * shouldn't process the respective replies (RPL_TOPIC and RPL_TOPICINFO)
	 * in this event handler. The purpose of this event handler is to 
	 * process those numeric replies that aren't processed by the library.
	 */
	void numericReplyReceived(NumericEvent event);
	
	void numericErrorReceived(NumericEvent event);
	
	void userModeReceived(UserModeEvent event);
	
	void topicReceived(TopicEvent event);
	
	/**
	 * Fired when a NAMES list was received and the connection participates
	 * in the channel.
	 */
	void namesReceived(NamesEvent event);
}
