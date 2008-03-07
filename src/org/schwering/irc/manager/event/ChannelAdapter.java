package org.schwering.irc.manager.event;

/**
 * Adapter for channel listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ChannelAdapter implements ChannelListener {
	public void channelModeReceived(ChannelModeEvent event) {
	}

	public void nickChanged(NickEvent event) {
	}

	public void noticeReceived(MessageEvent event) {
	}

	public void privmsgReceived(MessageEvent event) {
	}

	public void topicReceived(TopicEvent event) {
	}

	public void userJoined(UserParticipationEvent event) {
	}

	public void userLeft(UserParticipationEvent event) {
	}
	
	public void namesReceived(NamesEvent event) {
	}
	
	public void userStatusChanged(UserStatusEvent event) {
	}
}
