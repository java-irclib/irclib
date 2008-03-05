package org.schwering.irc.manager.event;

import java.util.EventListener;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

public interface ConnectionListener extends EventListener {
	/**
	 * Fired when the connection is registered successfully. At this point
	 * of time, the connecting user is fully-privileged.
	 */
	void connectionEstablished();

	/**
	 * Fired when the connection has been terminated is completely dead.
	 */
	void connectionLost();
	
	void errorReceived(Message msg);

	void motdReceived(String[] motd);

	/**
	 * Thrown when the server asked for a ping pong.
	 * <p>
	 * Note: You don't have to answer the request, this is done automatically.
	 * @param msg
	 */
	void pingReceived(Message msg);
	
	void channelJoined(Channel channel);

	void channelLeft(Channel channel);
	
	void invited(Channel channel, User user);
}
