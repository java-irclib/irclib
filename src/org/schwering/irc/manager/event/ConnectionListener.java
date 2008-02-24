package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public interface ConnectionListener extends EventListener {
	void connectionEstablished();

	void connectionLost();
	
	void errorReceived(Message msg);

	void motdReceived(String[] motd);

	void pingReceived(Message msg);

	void channelJoined(Channel channel);

	void channelLeft(Channel channel);
	
	void invited(Channel channel, User user);
}
