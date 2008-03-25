package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP FINGER request has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#fingerRequestReceived(CtcpFingerRequestEvent)
 */
public class CtcpFingerRequestEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;

	public CtcpFingerRequestEvent(Connection connection, User sender, User destUser,
			String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
	}

	public CtcpFingerRequestEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public User getSender() {
		return sender;
	}
	
	public Channel getDestinationChannel() {
		return destChannel;
	}
	
	public User getDestinationUser() {
		return destUser;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getArguments() {
		return rest;
	}
	
	/**
	 * Informs a user about this client's name, version and environment.
	 * @param clientName e.g. "IRClib"
	 * @param clientVersion e.g. "2.0"
	 * @param environment e.g. "Java 1.6"
	 */
	public void reply(String clientName, String clientVersion, 
			String environment) {
		String dest = (destUser != null) ? destUser.getNick() 
				: destChannel.getName();
		connection.sendCtcpReply(dest, "FINGER", 
				clientName +":"+ clientVersion +":"+ environment);
	}
}
