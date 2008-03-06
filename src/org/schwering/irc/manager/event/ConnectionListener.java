package org.schwering.irc.manager.event;

import java.util.EventListener;

/**
 * A connection listener listens for general events on connection level.
 * <p>
 * Events that are directly related to a channel the connection participates
 * in, these events are treated by <code>ChannelListener</code>s.
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
	
	void numericReplyReceived(NumericEvent event);
	
	void numericErrorReceived(NumericEvent event);
	
	void userModeReceived(UserModeEvent event);
}
