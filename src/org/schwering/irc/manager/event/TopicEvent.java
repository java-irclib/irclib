/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCConnection
 * Copyright (C) 2002 - 2006 Christoph Schwering <schwering@gmail.com>
 * 
 * This library and the accompanying materials are made available under the
 * terms of the
 * 	- GNU Lesser General Public License,
 * 	- Apache License, Version 2.0 and
 * 	- Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */

package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Topic;

/**
 * Fired when a topic was received either after joining a channel or after
 * requesting a topic or a topic was changed.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#topicReceived(TopicEvent)
 * @see ChannelListener#topicReceived(TopicEvent)
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
