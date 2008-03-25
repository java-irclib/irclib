package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP FINGER reply has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#fingerReplyReceived(CtcpFingerReplyEvent)
 */
public class CtcpFingerReplyEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;
	private String clientName;
	private String clientVersion;
	private String environment;

	public CtcpFingerReplyEvent(Connection connection, User sender, User destUser,
			String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(":", 3);
		if (arr.length == 3) {
			this.clientName = arr[0];
			this.clientVersion = arr[1];
			this.environment = arr[2];
		}
	}

	public CtcpFingerReplyEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
		String[] arr = rest.split(":", 3);
		if (arr.length == 3) {
			this.clientName = arr[0];
			this.clientVersion = arr[1];
			this.environment = arr[2];
		}
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
	
	public String getClientName() {
		return clientName;
	}
	
	public String getClientVersion() {
		return clientVersion;
	}
	
	public String getEnvironment() {
		return environment;
	}
}
