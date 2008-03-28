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

package org.schwering.irc.manager;

import java.util.Date;

/**
 * Represents a channel's topic. A topic is composed of the actual topic
 * message, the channel, the user that set the topic and the date when
 * it was set. 
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Topic {
	private Channel channel;
	private Message message;
	private User user;
	private Date date;
	
	Topic(Channel channel) {
		this.channel = channel;
	}
	
	Topic(Channel channel, Message topic) {
		this.channel = channel;
		this.message = topic;
	}
	
	Topic(Channel channel, Message message, User user, Date date) {
		this.channel = channel;
		this.message = message;
		this.user = user;
		this.date = date;
	}
	
	/**
	 * Returns the channel of the topic.
	 */
	public Channel getChannel() {
		return channel;
	}
	
	void setMessage(Message message) {
		this.message = message;
	}
	
	/**
	 * Returns the topic message itself or <code>null</code>. The latter
	 * is the case if no topic is set.
	 */
	public Message getMessage() {
		return message;
	}
	
	void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Returns the user who set the topic or <code>null</code>. The latter
	 * is the case if the topic is empty or if the information is not known.
	 */
	public User getUser() {
		return user;
	}
	
	void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the date when set the topic or <code>null</code>. The latter
	 * is the case if the topic is empty or if the information is not known.
	 */
	public Date getDate() {
		return date;
	}
}
