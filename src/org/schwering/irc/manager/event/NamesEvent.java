package org.schwering.irc.manager.event;

import java.util.Collections;
import java.util.List;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;

/**
 * Fired when a NAMES list was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#namesReceived(NamesEvent)
 * @see ChannelListener#namesReceived(NamesEvent)
 */
public class NamesEvent {
	private Connection connection;
	private Channel channel;
	private List channelUsers;

	public NamesEvent(Connection connection, Channel channel, 
			List channelUsers) {
		this.connection = connection;
		this.channel = channel;
		this.channelUsers = channelUsers;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returns the channel the NAMEs list is about.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Returns a list of <code>ChannelUser</code>s.
	 */
	public List getChannelUsers() {
		return Collections.unmodifiableList(channelUsers);
	}
}
