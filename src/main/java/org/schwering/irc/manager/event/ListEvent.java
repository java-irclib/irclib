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

import java.util.Collections;
import java.util.List;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Topic;

/**
 * Fired when a response of an LIST request was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#listReceived(ListEvent)
 */
public class ListEvent {
	private Connection connection;
	private List topics;
	private List visibleCounts;
	
	public ListEvent(Connection connection, List topics, List visibleCounts) {
		if (topics.size() != visibleCounts.size()) {
			throw new IllegalArgumentException("Lists don't have same size");
		}
		this.connection = connection;
		this.topics = topics;
		this.visibleCounts = visibleCounts;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public List getTopics() {
		return Collections.unmodifiableList(topics);
	}
	
	public List getVisibleCounts() {
		return Collections.unmodifiableList(visibleCounts);
	}
	
	public int getCount() {
		return topics.size();
	}
	
	public Topic getTopic(int i) {
		return (Topic)topics.get(i);
	}
	
	public int getVisibleCount(int i) {
		return ((Integer)visibleCounts.get(i)).intValue();
	}
}
