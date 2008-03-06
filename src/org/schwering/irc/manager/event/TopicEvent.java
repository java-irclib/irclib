package org.schwering.irc.manager.event;

import java.util.Date;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

/**
 * Fired when a topic was received either after joining a channel or after
 * requesting a topic or a topic was changed.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class TopicEvent {
	private Connection connection;
	private Channel channel;
	private Message message;
	private User user;
	private Date date;

	public TopicEvent(Connection connection, Channel channel, Message message,
			User user, Date date) {
		this.connection = connection;
		this.channel = channel;
		this.message = message;
		this.user = user;
		this.date = date;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public Message getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public Date getDate() {
		return date;
	}
}
