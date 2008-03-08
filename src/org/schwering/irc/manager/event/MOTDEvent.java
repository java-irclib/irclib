package org.schwering.irc.manager.event;

import java.util.Collections;
import java.util.List;

import org.schwering.irc.manager.Connection;

/**
 * Fired when the MOTD was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#motdReceived(MOTDEvent)
 */
public class MOTDEvent {
	private Connection connection;
	private List text;

	public MOTDEvent(Connection connection, List text) {
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
