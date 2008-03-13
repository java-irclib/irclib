package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when someone is invited by someone else to a channel.
 * Either the invited or the inviting user is the user represented by this
 * connection.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#invitationReceived(InvitationEvent)
 */
public class InvitationEvent {
	private Connection connection;
	private Channel channel;
	private User invitingUser;
	private User invitedUser;

	public InvitationEvent(Connection connection, Channel channel,
			User invitingUser, User invitedUser) {
		this.connection = connection;
		this.channel = channel;
		this.invitedUser = invitedUser;
		this.invitingUser = invitingUser;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public User getInvitingUser() {
		return invitingUser;
	}

	public User getInvitedUser() {
		return invitedUser;
	}
}
