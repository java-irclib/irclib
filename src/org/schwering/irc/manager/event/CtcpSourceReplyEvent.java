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
 * Fired when a CTCP SOURCE reply has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#sourceReplyReceived(CtcpSourceReplyEvent)
 */
public class CtcpSourceReplyEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;
	private String host;
	private String dir;
	private String files;

	public CtcpSourceReplyEvent(Connection connection, User sender, User destUser,
			String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(":", 3);
		if (arr.length == 3) {
			this.host = arr[0];
			this.dir = arr[1];
			this.files = arr[2];
		}
	}

	public CtcpSourceReplyEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(":", 3);
		if (arr.length == 3) {
			this.host = arr[0];
			this.dir = arr[1];
			this.files = arr[2];
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
	 * Returns <code>true</code> if this is the end marker. In this case,
	 * the <code>getHost()</code>, <code>getDirectory()</code> and
	 * <code>getFiles()</code> method return <code>null</code>.
	 */
	public boolean isEndMarker() {
		return rest == null || rest.length() == 0;
	}
	
	/**
	 * Returns the host. <code>null</code> if this is the end-marker-reply.
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Returns the directory. <code>null</code> if this is the end-marker-reply.
	 */
	public String getDirectory() {
		return dir;
	}
	
	/**
	 * Returns the space separated list of files. 
	 * <code>null</code> if this is the end-marker-reply.
	 */
	public String getFiles() {
		return files;
	}
}
