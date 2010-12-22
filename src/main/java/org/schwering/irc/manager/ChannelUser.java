/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCConnection
 * Copyright (C) 2002 - 2006 Christoph Schwering <schwering@gmail.com>
 * 
 * This library and the accompanying materials are made available under the
 * terms of the
 * 	- GNU Lesser General Public License,
 * 	- Apache License, Version 2.0 and
 * 	- Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */

package org.schwering.irc.manager;

/**
 * Pair of a user and its staus (operator, voiced or none). Used for the
 * status of a user in a channel.
 * <p>
 * Some words about the implementation of this class (not importand for
 * library users!): Although this class extends <code>User</code>, the 
 * super class instance doesn't really store a <code>User</code> object.
 * Instead, all calls of <code>User</code>-methods are forwarded to a 
 * <code>User</code> object stored as private class field in this class.
 * This guarantees that for example changing the away status of a 
 * <code>User</code> or a <code>ChannelUser</code> affects all 
 * <code>ChannelUser</code>s that have the same underlying <code>User</code>.
 * Copying the data of <code>User</code> to the super class instance 
 * would not guarantee this. The point to have in mind is that a 
 * <code>User</code> is a "global" object, whereas a <code>ChannelUser</code>
 * is a specialization of a <code>User</code> limited to the world of one
 * concrete channel. 
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ChannelUser extends User {
	private Channel channel;
	private User user;
	private int status;
	
	ChannelUser(Channel channel, User user) {
		this(channel, user, Channel.NONE);
	}
	
	ChannelUser(Channel channel, User user, int status) {
		super("(avoid NPE)", null, null);
		if (user instanceof ChannelUser) {
			throw new RuntimeException("Wrapping a ChannelUser in a " +
					"ChannelUser is a bad idea; this exception shows " +
					"the bad design of the org.schwering.irc.manager package");
		}
		this.channel = channel;
		this.user = user;
		this.status = status;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * Returns the underlying <code>User</code> object.
	 */
	User getUser() {
		return user;
	}
	
	void setStatus(int status) {
		this.status = status;
	}
	
	void addStatus(int status) {
		this.status |= status;
	}
	
	void removeStatus(int status) {
		this.status &= ~status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public boolean isOperator() {
		return status == Channel.OPERATOR;
	}
	
	public boolean isVoiced() {
		return status == Channel.VOICED;
	}

	public String getHost() {
		return user.getHost();
	}

	public String getNick() {
		return user.getNick();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public boolean isAway() {
		return user.isAway();
	}

	public void setAway(boolean away) {
		user.setAway(away);
	}
}
