package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

/**
 * Fired when a message was received, either a NOTICE or a PRIVMSG, either
 * to a channel or to another user.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#messageReceived(MessageEvent)
 * @see ConnectionListener#noticeReceived(MessageEvent)
 * @see ChannelListener#messageReceived(MessageEvent)
 * @see ChannelListener#noticeReceived(MessageEvent)
 * @see PrivateMessageListener#messageReceived(MessageEvent)
 * @see PrivateMessageListener#noticeReceived(MessageEvent)
 */
public class MessageEvent {
	private Connection connection;
	private Channel destinationChannel;
	private User destinationUser;
	private User sender;
	private Message message;

	public MessageEvent(Connection connection, User sender, 
			User destinationUser, Message message) {
		this.connection = connection;
		this.destinationUser = destinationUser;
		this.sender = sender;
		this.message = message;
	}

	public MessageEvent(Connection connection, User sender, 
			Channel destinationChannel, String msg) {
		this.connection = connection;
		this.destinationChannel = destinationChannel;
		this.sender = sender;
		this.message = new Message(msg);
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getDestinationChannel() {
		return destinationChannel;
	}
	
	public User getDestinationUser() {
		return destinationUser;
	}

	public User getSender() {
		return sender;
	}

	public Message getMessage() {
		return message;
	}
}
