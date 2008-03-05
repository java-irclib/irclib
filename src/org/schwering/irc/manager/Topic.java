package org.schwering.irc.manager;

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
	private String topic;
	private User user;
	private long time;
	
	Topic(Channel channel, String topic, User user, long time) {
		this.channel = channel;
		this.topic = topic;
		this.user = user;
		this.time = time;
	}
	
	void update(User user, long time) {
		this.user = user;
		this.time = time;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public User getUser() {
		return user;
	}
	
	public long getTime() {
		return time;
	}
}
