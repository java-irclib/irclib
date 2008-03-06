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
	
	Topic(Channel channel, Message topic) {
		this.channel = channel;
		this.topic = topic;
	}
	
	Topic(Channel channel, Message topic, User user, Date date) {
		this.channel = channel;
		this.topic = topic;
		this.user = user;
		this.date = date;
	}
	
	/**
	 * Returns the channel of the topic.
	 */
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * Returns the topic message itself or <code>null</code>. The latter
	 * is the case if no topic is set.
	 */
	public Message getTopic() {
		return topic;
	}
	
	void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Returns the user who set the topic or <code>null</code>. The latter
	 * is the case if the topic is empty or if the information is not known.
	 */
	public User getUser() {
		return user;
	}
	
	void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the date when set the topic or <code>null</code>. The latter
	 * is the case if the topic is empty or if the information is not known.
	 */
	public Date getDate() {
		return date;
	}
}
