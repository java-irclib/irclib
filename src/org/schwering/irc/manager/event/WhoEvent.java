package org.schwering.irc.manager.event;

import java.util.Collections;
import java.util.List;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Connection;

/**
 * Fired when a WHO list was received. Such a list is similar to a NAMES
 * reply, but offers some additional information (at the cost of a longer 
 * taking reply).
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#whoReceived(WhoEvent)
 * @see ChannelListener#whoReceived(WhoEvent)
 */
public class WhoEvent {
	private Connection connection;
	private Channel channel;
	private List channelUsers;
	private List servers;
	private List realNames;
	private List hopCounts;

	public WhoEvent(Connection connection, Channel channel, 
			List channelUsers, List realNames, List servers, List hopCounts) {
		this.connection = connection;
		this.channel = channel;
		this.channelUsers = channelUsers;
		this.servers = servers;
		this.realNames = realNames;
		this.hopCounts = hopCounts;
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
	
	public List getServers() {
		return Collections.unmodifiableList(servers);
	}
	
	public List getRealnames() {
		return Collections.unmodifiableList(realNames);
	}
	
	public List getHopCounts() {
		return Collections.unmodifiableList(hopCounts);
	}
	
	public int getCount() {
		return channelUsers.size();
	}
	
	public ChannelUser getChannelUser(int i) {
		return (ChannelUser)channelUsers.get(i);
	}
	
	public String getServer(int i) {
		return (String)servers.get(i);
	}
	
	public String getRealname(int i) {
		return (String)realNames.get(i);
	}
	
	public int getHopCount(int i) {
		return ((Integer)hopCounts.get(i)).intValue();
	}
}
