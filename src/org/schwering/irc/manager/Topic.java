package org.schwering.irc.manager;

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
