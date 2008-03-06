package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;

/**
 * Fired when a numeric reply or numeric error was received. Note that a
 * numeric error is sent by the server when trying to WHOIS a non-existent
 * person, for example, i.e. numeric errors don't mean anything evil.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class NumericEvent {
	private Connection connection;
	private int num;
	private String message;

	public NumericEvent(Connection connection, int num, String message) {
		this.connection = connection;
		this.num = num;
		this.message = message;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public int getNumer() {
		return num;
	}
	
	public String getMessage() {
		return message;
	}
}
