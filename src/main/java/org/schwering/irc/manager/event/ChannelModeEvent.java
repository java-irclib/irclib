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

import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when a channel mode was changed or received. Channel modes include
 * when somebody gets channel operator status by someone else or somebody
 * is banned from the channel, for example.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#channelModeReceived(ChannelModeEvent)
 * @see ChannelListener#channelModeReceived(ChannelModeEvent)
 */
public class ChannelModeEvent {
	private Connection connection;
	private Channel channel;
	private User user;
	private IRCModeParser ircModeParser;

	public ChannelModeEvent(Connection connection, Channel channel, User user,
			IRCModeParser ircModeParser) {
		this.connection = connection;
		this.channel = channel;
		this.ircModeParser = ircModeParser;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * The user who set the modes. <code>null</code> if it's the channel
	 * modes returned when joining.
	 */
	public User getUser() {
		return user;
	}

	public IRCModeParser getIRCModeParser() {
		return ircModeParser;
	}
}
