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
 * Fired when an unexpected IRC event was seen.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see UnexpectedEventListener#unexpectedEventReceived(UnexpectedEvent)
 */
public class UnexpectedEvent {
	private Connection connection;
	private String event;
	private Object[] args;
	
	public UnexpectedEvent(Connection connection, String eventName, 
			Object[] args) {
		this.connection = connection;
		this.args = args;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getEventName() {
		return event;
	}
	
	public Object[] getArguments() {
		return args;
	}
}
