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

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;

/**
 * Fired when a NAMES list was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#namesReceived(NamesEvent)
 * @see ChannelListener#namesReceived(NamesEvent)
 */
public class NamesEvent {
	private Connection connection;
	private Channel channel;
	private boolean hasNewUsers;
	private List channelUsers;

	public NamesEvent(Connection connection, Channel channel, 
			boolean hasNewUsers, List channelUsers) {
		this.connection = connection;
		this.channel = channel;
		this.hasNewUsers = hasNewUsers;
		this.channelUsers = channelUsers;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returns the channel the NAMEs list is about.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Returns <code>true</code> if the connection participates in the channel
	 * and the names in the NAMES reply contains new users that weren't seen 
	 * before. This is the case when you've joined a channel and don't know who 
	 * joined the channel before you. This flag is intended to let you know 
	 * when you have to update your nicklist on incoming WHO and NAMES replies 
	 * and when you don't have to.
	 * <p>
	 * Normally, IRC servers send a NAMES reply when one joins a channel.
	 * This initial NAMES reply is intended to inform the joining user who
	 * currently is in the channel he joined. This flag is primarily for this
	 * case, and in this case, it returns <code>true</code>.
	 */
	public boolean hasNewUsers() {
		return hasNewUsers;
	}

	/**
	 * Returns a list of <code>ChannelUser</code>s.
	 */
	public List getChannelUsers() {
		return Collections.unmodifiableList(channelUsers);
	}
}
