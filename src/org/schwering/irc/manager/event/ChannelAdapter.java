package org.schwering.irc.manager.event;

import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;

public class ChannelAdapter implements ChannelListener {

	public void modeChanged(IRCModeParser mode) {
	}

	public void topicChanged(Topic topic) {
	}

	public void userJoined(User user) {
	}

	public void userLeft(User user, Message msg, int method) {
	}
	
	public void nickChanged(User oldUser, User newUser) {
	}

	public void noticeReceived(User user, Message msg) {
	}

	public void privmsgReceived(User user, Message msg) {
	}

}
