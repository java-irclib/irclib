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
