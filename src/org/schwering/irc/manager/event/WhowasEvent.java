package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when a WHOWAS response was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#whowasReceived(WhowasEvent)
*/
public class WhowasEvent {
	private Connection connection;
	private User user;
	private String realName;

	public WhowasEvent(Connection connection, User user, String realName) {
		this.connection = connection;
		this.user = user;
		this.realName = realName;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returns a user with initialized nickname, username and host.
	 */
	public User getUser() {
		return user;
	}

	public String getRealname() {
		return realName;
	}
}
