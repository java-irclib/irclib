package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when someone changes his nickname.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class NickEvent {
	private Connection connection;
	private User oldUser;
	private User newUser;

	public NickEvent(Connection connection, User oldUser, User newUser) {
		this.connection = connection;
		this.oldUser = oldUser;
		this.newUser = newUser;
	}

	public Connection getConnection() {
		return connection;
	}

	public User getOldUser() {
		return oldUser;
	}

	public User getNewUser() {
		return newUser;
	}
}
