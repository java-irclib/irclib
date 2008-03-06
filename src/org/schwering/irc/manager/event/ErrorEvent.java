package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;

/**
 * Fired when an error was received. An error typically means that the
 * connection must be terminated or will be terminated by the server.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ErrorEvent {
	private Connection connection;
	private String message;

	public ErrorEvent(Connection connection, String message) {
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
