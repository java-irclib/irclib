package org.schwering.irc.manager.event;

import java.util.EventListener;

/**
 * Listener for user-to-user communication via PRIVMSG or NOTICE.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface PrivateMessageListener extends EventListener {
	/**
	 * Fired when a user-to-user PRIVMSG comes in.
	 */
	void privmsgReceived(MessageEvent event);

	/**
	 * Fired when a user-to-user NOTICE comes in.
	 */
	void noticeReceived(MessageEvent event);
}
