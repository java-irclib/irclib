package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;

public interface ChannelListener extends EventListener {
	void userJoined(User user);

	void userLeft(User user, Message msg, int method);

	void topicChanged(Topic topic);

	/**
	 * Fired when a user in the channel changed his nickname.
	 * <p> 
	 * Note: (1) When this event is fired, the channel's user list is already
	 * updated, i.e. it doesn't contain <code>oldUser</code> but 
	 * <code>newUser</code> and <code>Channel.getUser(oldUser.getNick())</code> 
	 * doesn't return <code>oldUser</code>, and (2) the channels'
	 * <code>nickChanged</code> event is fired before the global
	 * <code>ConnectionListener.nickChanged</code> event. 
	 */
	void nickChanged(User oldUser, User newUser);
	
	void modeChanged(IRCModeParser mode);
	
	void privmsgReceived(User user, Message msg);
	
	void noticeReceived(User user, Message msg);
}
