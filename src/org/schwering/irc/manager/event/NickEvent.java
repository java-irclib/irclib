package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when someone changes his nickname.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#nickChanged(NickEvent)
 * @see ChannelListener#nickChanged(NickEvent)
 */
public class NickEvent {
	private Connection connection;
	private User user;
	private String oldNick;

	public NickEvent(Connection connection, User user, String oldNick) {
		this.connection = connection;
		this.user = user;
		this.oldNick = oldNick;
	}

	public Connection getConnection() {
		return connection;
	}

	public User getUser() {
		return user;
	}
	
	public String getOldNick() {
		return oldNick;
	}
}
