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

import java.net.InetAddress;
import java.util.StringTokenizer;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.CtcpUtil;
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP DCC CHAT request has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#dccChatReceived(CtcpDccChatEvent)
 */
public class CtcpDccChatEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;
	private InetAddress addr;
	private int port;

	public CtcpDccChatEvent(Connection connection, User sender, User destUser,
			String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
		init(rest);
	}

	public CtcpDccChatEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
		init(rest);
	}

	private void init(String rest) {
		try {
			StringTokenizer st = new StringTokenizer(rest);
			st.nextToken(); // skip "SEND"
			st.nextToken(); // skip "chat"
			String tmphost = st.nextToken();
			if (tmphost.charAt(0) == '\"') {
				tmphost = tmphost.substring(1);
			}
			if (tmphost.charAt(tmphost.length() - 1) == '\"') {
				tmphost = tmphost.substring(0, tmphost.length() - 1);
			}
	  		addr = CtcpUtil.convertLongToInetAddress(Long.parseLong(tmphost));
	  		port = Integer.parseInt(st.nextToken());
		} catch (Exception exc) {
			exc.printStackTrace();
			addr = null;
			port = -1;
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
	 * Returns the host. If the DCC line is invalid, <code>null</code>
	 * is returned.
	 */
	public InetAddress getAddress() {
		return addr;
	}

	/**
	 * Returns the port. If the DCC line was invalid, <code>-1</code> is
	 * returned.
	 */
	public int getPort() {
		return port;
	}
}
