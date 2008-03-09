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
