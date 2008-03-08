package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;

/**
 * Fired when a connection has been either established or closed.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#connectionEstablished(ConnectionEvent)
 * @see ConnectionListener#connectionLost(ConnectionEvent)
 */
public class ConnectionEvent {
	private Connection connection;

	public ConnectionEvent(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
}
