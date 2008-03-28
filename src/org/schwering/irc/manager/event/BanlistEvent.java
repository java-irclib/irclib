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
import java.util.Date;
import java.util.List;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when the banlist was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#banlistReceived(BanlistEvent)
 * @see ChannelListener#banlistReceived(BanlistEvent)
 */
public class BanlistEvent {
	private Connection connection;
	private Channel channel;
	private List banIDs;
	private List users;
	private List dates;

	public BanlistEvent(Connection connection, Channel channel, List banIDs,
			List users, List dates) {
		if (banIDs.size() != users.size() || users.size() != dates.size()) {
			throw new IllegalArgumentException("Lists must have same size");
		}
		this.connection = connection;
		this.channel = channel;
		this.banIDs = banIDs;
		this.users = users;
		this.dates = dates;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public List getBanIDs() {
		return Collections.unmodifiableList(banIDs);
	}
	
	public List getUsers() {
		return Collections.unmodifiableList(users);
	}
	
	public List getDates() {
		return Collections.unmodifiableList(dates);
	}
	
	public int getCount() {
		return banIDs.size();
	}
	
	public String getBanID(int i) {
		return (String)banIDs.get(i);
	}
	
	public User getUser(int i) {
		return (User)users.get(i);
	}
	
	public Date getDate(int i) {
		return (Date)dates.get(i);
	}
}
