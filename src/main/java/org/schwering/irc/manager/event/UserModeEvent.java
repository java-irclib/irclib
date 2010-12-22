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

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * A user mode event is fired when someone's server-operator status
 * is changed, for example.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#userModeReceived(UserModeEvent)
 */
public class UserModeEvent {
	private Connection connection;
	private User activeUser;
	private User passiveUser;
	private String mode;

	public UserModeEvent(Connection connection, User activeUser, 
			User passiveUser, String mode) {
		this.connection = connection;
		this.activeUser = activeUser;
		this.passiveUser = passiveUser;
		this.mode = mode;
	}

	public Connection getConnection() {
		return connection;
	}

	public User getActiveUser() {
		return activeUser;
	}
	
	public User getPassiveUser() {
		return passiveUser;
	}
	
	public String getMode() {
		return mode;
	}
}
