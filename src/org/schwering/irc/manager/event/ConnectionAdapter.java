package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public class ConnectionAdapter implements ConnectionListener {

	public void channelJoined(Channel channel) {
	}

	public void channelLeft(Channel channel) {
	}
	
	public void errorReceived(Message msg) {
	}

	public void connectionEstablished() {
	}

	public void connectionLost() {
	}

	public void motdReceived(String[] motd) {
	}

	public void pingReceived(Message msg) {
	}

	public void invited(Channel channel, User user) {
	}
}
