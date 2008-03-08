package org.schwering.irc.manager.event;

import java.util.List;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;

/**
 * Fired when the banlist was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class BanlistEvent {
	private Connection connection;
	private Channel channel;
	private List banIDs;

	public BanlistEvent(Connection connection, Channel channel, List banIDs) {
		this.connection = connection;
		this.channel = channel;
		this.banIDs = banIDs;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public List getBanIDs() {
		return banIDs;
	}
}
