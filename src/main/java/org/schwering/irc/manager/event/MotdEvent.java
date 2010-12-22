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

import org.schwering.irc.manager.Connection;

/**
 * Fired when the MOTD was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#motdReceived(MotdEvent)
 */
public class MotdEvent {
	private Connection connection;
	private List text;

	public MotdEvent(Connection connection, List text) {
		this.connection = connection;
		this.text = text;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returns the text of the MOTD. The elements in the list are
	 * <code>String</code>s.
	 */
	public List getText() {
		return Collections.unmodifiableList(text);
	}
}
