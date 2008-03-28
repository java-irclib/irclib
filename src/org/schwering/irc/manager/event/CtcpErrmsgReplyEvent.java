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
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP ERRMSG reply has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#errmsgReplyReceived(CtcpErrmsgReplyEvent)
 */
public class CtcpErrmsgReplyEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;
	private String query;
	private String answer;

	public CtcpErrmsgReplyEvent(Connection connection, User sender, 
			User destUser, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(" :", 2);
		if (arr.length == 2) {
			query = arr[0];
			answer = arr[1];
		}
	}

	public CtcpErrmsgReplyEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(" :", 2);
		if (arr.length == 2) {
			query = arr[0];
			answer = arr[1];
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public User getSender() {
		return sender;
	}
	
	public Channel getDestinationChannel() {
		return destChannel;
	}
	
	public User getDestinationUser() {
		return destUser;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getArguments() {
		return rest;
	}
	
	/**
	 * Returns the string for which the ERRMSG was originally requested.
	 * If "ERRMSG xyz" was requested, then the reply is "ERRMSG xyz :bla"
	 * and the query is "xyz".
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Returns answer string for the originally reuqested query.
	 * If "ERRMSG xyz" was requested, then the reply is "ERRMSG xyz :bla"
	 * and the answer is "bla".
	 */
	public String getAnswer() {
		return answer;
	}
}
