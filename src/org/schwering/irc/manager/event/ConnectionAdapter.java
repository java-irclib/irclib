package org.schwering.irc.manager.event;

/**
 * Adapter for connection listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ConnectionAdapter implements ConnectionListener {
	public void channelJoined(UserParticipationEvent channel) {
	}

	public void channelLeft(UserParticipationEvent channel) {
	}

	public void connectionEstablished(ConnectionEvent event) {
	}

	public void connectionLost(ConnectionEvent event) {
	}

	public void errorReceived(ErrorEvent event) {
	}

	public void invited(InvitationEvent event) {
	}

	public void motdReceived(MOTDEvent event) {
	}

	public void pingReceived(PingEvent event) {
	}

	public void numericErrorReceived(NumericEvent event) {
	}

	public void numericReplyReceived(NumericEvent event) {
	}

	public void userModeReceived(UserModeEvent event) {
	}
	
	public void topicReceived(TopicEvent event) {
	}
}
