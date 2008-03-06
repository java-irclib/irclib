package org.schwering.irc.manager.event;

/**
 * Adapter for user-to-user-communication-listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class PrivateMessageAdapter implements PrivateMessageListener {

	public void noticeReceived(MessageEvent event) {
	}

	public void privmsgReceived(MessageEvent event) {
	}

}
