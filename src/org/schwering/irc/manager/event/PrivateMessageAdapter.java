package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public class PrivateMessageAdapter implements PrivateMessageListener {

	public void noticeReceived(User user, Message msg) {
	}

	public void privmsgReceived(User user, Message msg) {
	}

}
