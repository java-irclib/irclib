package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Topic;

/**
 * Fired when a topic was received either after joining a channel or after
 * requesting a topic or a topic was changed.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class TopicEvent {
	private Connection connection;
	private Topic topic;

	public TopicEvent(Connection connection, Topic topic) {
		this.connection = connection;
		this.topic = topic;
	}

	public Connection getConnection() {
		return connection;
	}

	public Topic getTopic() {
		return topic;
	}
}
