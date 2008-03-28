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
 * Used when somebody joins or leaves a channel by either parting, quitting
 * or being kicked.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#channelJoined(UserParticipationEvent)
 * @see ConnectionListener#channelLeft(UserParticipationEvent)
 * @see ChannelListener#userJoined(UserParticipationEvent)
 * @see ChannelListener#userLeft(UserParticipationEvent)
 * @see ChannelListener#userJoined(UserParticipationEvent)
 * @see ChannelListener#userLeft(UserParticipationEvent)
 */
public class UserParticipationEvent {
	public static final int JOIN = 1;
	public static final int PART = 2;
	public static final int KICK = 4;
	public static final int QUIT = 8;
	
	private Connection connection;
	private Channel channel;
	private User user;
	private int type;
	private Message message;
	private User kickingUser;
	
	public UserParticipationEvent(Connection connection, Channel channel,
			User user, int type) {
		this.connection = connection;
		this.channel = channel;
		this.user = user;
		this.type = type;
	}

	public UserParticipationEvent(Connection connection, Channel channel,
			User user, int type, Message message) {
		this.connection = connection;
		this.channel = channel;
		this.user = user;
		this.type = type;
		this.message = message;
	}

	public UserParticipationEvent(Connection connection, Channel channel,
			User user, int type, Message message, User kickingUser) {
		this.connection = connection;
		this.channel = channel;
		this.user = user;
		this.type = type;
		this.message = message;
		this.kickingUser = kickingUser;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public User getUser() {
		return user;
	}

	public boolean isJoin() {
		return type == JOIN;
	}
	
	public boolean isLeave() {
		return type == PART || type == KICK || type == QUIT;
	}
	
	public boolean isUnsolicitedLeave() {
		return type == PART || type == QUIT;
	}
	
	public boolean isPart() {
		return type == PART;
	}
	
	public boolean isKick() {
		return type == KICK;
	}
	
	public boolean isQuit() {
		return type == QUIT;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public User getKickingUser() {
		return kickingUser;
	}
}
