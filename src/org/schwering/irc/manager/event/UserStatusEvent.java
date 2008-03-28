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
import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Connection;

/**
 * Used when someone's channel status has changed. By status, voice-status or
 * channel-operator-status is meant.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ChannelListener#userStatusChanged(UserStatusEvent)
 */
public class UserStatusEvent {
	private Connection connection;
	private Channel channel;
	private ChannelUser user;
	
	public UserStatusEvent(Connection connection, Channel channel, 
			ChannelUser user) {
		this.connection = connection;
		this.channel = channel;
		this.user = user;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public ChannelUser getUser() {
		return user;
	}

	public int getStatus() {
		return user.getStatus();
	}
}
