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
import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Connection;

/**
 * Fired when a WHO list was received. Such a list is similar to a NAMES
 * reply, but offers some additional information (at the cost of a longer 
 * taking reply).
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#whoReceived(WhoEvent)
 * @see ChannelListener#whoReceived(WhoEvent)
 */
public class WhoEvent {
	private Connection connection;
	private Channel channel;
	private boolean hasNewUsers;
	private List channelUsers;
	private List servers;
	private List realNames;
	private List hopCounts;

	public WhoEvent(Connection connection, Channel channel, boolean hasNewUsers, 
			List channelUsers, List realNames, List servers, List hopCounts) {
		this.connection = connection;
		this.channel = channel;
		this.hasNewUsers = hasNewUsers;
		this.channelUsers = channelUsers;
		this.servers = servers;
		this.realNames = realNames;
		this.hopCounts = hopCounts;
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
	 * and the names in the WHO reply contains new users that weren't seen 
	 * before. This is the case when you've joined a channel and don't know who 
	 * joined the channel before you. This flag is intended to let you know 
	 * when you have to update your nicklist on incoming WHO and NAMES replies 
	 * and when you don't have to.
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
	
	public List getServers() {
		return Collections.unmodifiableList(servers);
	}
	
	public List getRealnames() {
		return Collections.unmodifiableList(realNames);
	}
	
	public List getHopCounts() {
		return Collections.unmodifiableList(hopCounts);
	}
	
	public int getCount() {
		return channelUsers.size();
	}
	
	public ChannelUser getChannelUser(int i) {
		return (ChannelUser)channelUsers.get(i);
	}
	
	public String getServer(int i) {
		return (String)servers.get(i);
	}
	
	public String getRealname(int i) {
		return (String)realNames.get(i);
	}
	
	public String getHopCount(int i) {
		return (String)hopCounts.get(i);
	}
}
