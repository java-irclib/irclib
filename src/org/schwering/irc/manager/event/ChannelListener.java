package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;

public interface ChannelListener extends EventListener {
	void userJoined(User user);

	void userLeft(User user);

	void userKicked(User user, Message msg);

	void topicChanged(Topic topic);

	void modeChanged(IRCModeParser mode);
	
	void privmsgReceived(User user, Message msg);
	
	void noticeReceived(User user, Message msg);
}
