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
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

/**
 * Fired when the set of WHOIS answers was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#whoisReceived(WhoisEvent)
 */
public class WhoisEvent {
	private Connection connection;
	private User user;
	private String realName;
	private String server;
	private String serverInfo;
	private String authName;
	private boolean operator;
	private Date dateSignon;
	private boolean idle;
	private long millisIdle;
	private Message awayMsg;
	private List channels;
	
	public WhoisEvent(Connection connection, User user, String realName,
			String authName, String server, String serverInfo, boolean operator, 
			Date dateSignon, boolean idle, long millisIdle, Message awayMsg, 
			List channels) {
		super();
		this.connection = connection;
		this.user = user;
		this.realName = realName;
		this.authName = authName;
		this.server = server;
		this.serverInfo = serverInfo;
		this.operator = operator;
		this.dateSignon = dateSignon;
		this.idle = idle;
		this.millisIdle = millisIdle;
		this.awayMsg = awayMsg;
		this.channels = channels;
	}

	public Connection getConnection() {
		return connection;
	}

	public User getUser() {
		return user;
	}

	public String getRealname() {
		return realName;
	}
	
	public String getAuthname() {
		return authName;
	}

	public String getServer() {
		return server;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public boolean isOperator() {
		return operator;
	}
	
	public Date getSignonDate() {
		return dateSignon;
	}
	
	public boolean isIdle() {
		return idle;
	}
	
	public long getIdleMillis() {
		return millisIdle;
	}
	
	public String getIdleTime() {
		if (millisIdle == -1) {
			return null;
		}
		
		long x = millisIdle;
		final long SECOND = 1000;
		final long MINUTE = 60*SECOND;
		final long HOUR = 60*MINUTE;
		final long DAY = 24*HOUR;
		int days = (int)(x / DAY);
		x -= days * DAY;
		int hours = (int)(x / HOUR);
		x -= hours * HOUR;
		int minutes = (int)(x / MINUTE);
		x -= minutes * MINUTE;
		int seconds = (int)(x / SECOND);
		StringBuffer time = new StringBuffer();
		if (days > 0) {
			time.append(days +" days, ");
		}
		if (days > 0 || hours > 0) {
			time.append(hours +" hours, ");
		}
		if (days > 0 || hours > 0 || minutes > 0) {
			time.append(minutes +" minutes, ");
		}
		if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
			time.append(seconds +" seconds");
		}
		return time.toString();
	}
	
	public Message getAwayMessage() {
		return awayMsg;
	}

	public List getChannelsWithStatus() {
		return channels != null ? Collections.unmodifiableList(channels) : null;
	}
	
	public int getChannelCount() {
		return channels != null ? channels.size() : 0;
	}
	
	public int getChannelStatus(int i) {
		char c = ((String)channels.get(i)).charAt(0);
		if (c == '@') {
			return Channel.OPERATOR;
		} else if (c == '+') {
			return Channel.VOICED;
		} else {
			return Channel.NONE;
		}
	}
	
	public Channel getChannel(int i) {
		String name = (String)channels.get(i);
		if (name.charAt(0) == '@') {
			name = name.substring(1);
		} else if (name.charAt(0) == '+') {
			name = name.substring(1);
		}
		return connection.resolveChannel(name);
	}
}
