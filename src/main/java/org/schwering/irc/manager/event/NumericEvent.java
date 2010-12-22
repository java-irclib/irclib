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

import org.schwering.irc.lib.IRCUtil;
import org.schwering.irc.manager.Connection;

/**
 * Fired when a numeric reply or numeric error was received. Note that a
 * numeric error is sent by the server when trying to WHOIS a non-existent
 * person, for example, i.e. numeric errors don't mean anything evil.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#numericReplyReceived(NumericEvent)
 * @see ConnectionListener#numericErrorReceived(NumericEvent)
 */
public class NumericEvent {
	private Connection connection;
	private int num;
	private String value;
	private String message;

	public NumericEvent(Connection connection, int num, String value, 
			String message) {
		this.connection = connection;
		this.num = num;
		this.value = value;
		this.message = message;
	}

	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Returns the error or reply number.
	 * @see IRCUtil
	 */
	public int getNumber() {
		return num;
	}
	
	/**
	 * Returns the value. What the value is depends on the error or reply
	 * number. If it's an error number, the value is always <code>null</code>!
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the message. What the message is depends on the error or reply
	 * number.
	 */
	public String getMessage() {
		return message;
	}
}
