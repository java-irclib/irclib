package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;

/**
 * Fired when the MOTD was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class MOTDEvent {
	private Connection connect;
	private String[] text;

	public MOTDEvent(Connection connect, String[] text) {
		this.connect = connect;
		this.text = text;
	}

	public Connection getConnect() {
		return connect;
	}

	public String[] getText() {
		return text;
	}
}
