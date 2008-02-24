package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public interface PrivateMessageListener extends EventListener {
	void privmsgReceived(User user, Message msg);

	void noticeReceived(User user, Message msg);
}
