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
			Channel destinationChannel, Message msg) {
		this.connection = connection;
		this.destinationChannel = destinationChannel;
		this.sender = sender;
		this.message = msg;
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

	public User getUser() {
		return sender;
	}

	public Message getMessage() {
		return message;
	}
}
