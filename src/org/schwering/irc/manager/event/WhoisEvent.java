package org.schwering.irc.manager.event;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;

/**
 * Fired when the set of WHOIS answers was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class WhoisEvent {
	private Connection connection;
	private User user;
	private String realName;
	private String server;
	private String serverInfo;
	private String authName;
	private boolean operator;
	private boolean idle;
	private Date date;
	private Message awayMsg;
	private List channels;
	
	public WhoisEvent(Connection connection, User user, String realName,
			String authName, String server, String serverInfo, 
			boolean operator, boolean idle, Date date, Message awayMsg, 
			List channels) {
		super();
		this.connection = connection;
		this.user = user;
		this.realName = realName;
		this.authName = authName;
		this.server = server;
		this.serverInfo = serverInfo;
		this.operator = operator;
		this.idle = idle;
		this.date = date;
		this.awayMsg = awayMsg;
		this.channels = channels;
	}

	public Connection getConnection() {
		return connection;
	}

	public User getUser() {
		return user;
	}

	public String getRealname() {
		return realName;
	}
	
	public String getAuthname() {
		return authName;
	}

	public String getServer() {
		return server;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public boolean isOperator() {
		return operator;
	}
	
	public boolean isIdle() {
		return idle;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Message getAwayMessage() {
		return awayMsg;
	}

	public List getChannelsWithStatus() {
		return channels != null ? Collections.unmodifiableList(channels) : null;
	}
	
	public int getChannelCount() {
		return channels != null ? channels.size() : 0;
	}
	
	public int getChannelStatus(int i) {
		char c = ((String)channels.get(i)).charAt(0);
		if (c == '@') {
			return ChannelUser.OPERATOR;
		} else if (c == '+') {
			return ChannelUser.VOICED;
		} else {
			return ChannelUser.NONE;
		}
	}
	
	public Channel getChannel(int i) {
		String name = (String)channels.get(i);
		if (name.charAt(0) == '@') {
			name = name.substring(1);
		} else if (name.charAt(0) == '+') {
			name = name.substring(1);
		}
		return connection.resolveChannel(name);
	}
}
