package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public interface PrivateMessageListener extends EventListener {
	/**
	 * Fired when a user-to-user PRIVMSG comes in.
	 */
	void privmsgReceived(User user, Message msg);

	/**
	 * Fired when a user-to-user NOTICE comes in.
	 */
	void noticeReceived(User user, Message msg);
}
