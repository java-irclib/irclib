package org.schwering.irc.manager;

import java.util.Date;

/**
 * Represents a channel's topic. A topic is composed of the actual topic
 * message, the channel, the user that set the topic and the date when
 * it was set. 
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Topic {
	private Channel channel;
	private Message topic;
	private User user;
	private Date date;
	
	Topic(Channel channel, Message topic, User user, Date date) {
		this.channel = channel;
		this.topic = topic;
		this.user = user;
		this.date = date;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Message getTopic() {
		return topic;
	}
	
	public User getUser() {
		return user;
	}
	
	public Date getDate() {
		return date;
	}
}
