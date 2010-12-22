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

/**
 * Fired when a PING was received. Note that PINGs are answered with a 
 * PONG automatically.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#pingReceived(PingEvent)
 */
public class PingEvent {
	private Connection connection;
	private String message;

	public PingEvent(Connection connection, String message) {
		this.connection = connection;
		this.message = message;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getMessage() {
		return message;
	}
}
