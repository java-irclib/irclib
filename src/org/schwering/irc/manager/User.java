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

import org.schwering.irc.lib.IRCUser;

/**
 * Represents a user. The minimal information is his nickname. Further
 * and optional information are his username, host and away status.
 * <p>
 * Each <code>Connection</code> object maintains a list of known users,
 * i.e. those users in the channels the connection participates in. 
 * Information about these users is collected and can be requested
 * via {@link Connection#resolveUser(String)} and 
 * {@link Connection#resolveUser(IRCUser)}.
 * <p>
 * You might wonder why <code>User</code> in contrast to <code>Channel</code>
 * and <code>Connection</code> has no corresponding <code>UserListener</code>.
 * This is because the character of the <code>User</code> objects is quite
 * volatile. For example, the <code>User</code> object of the partner of a 
 * user-to-user communication is created newly for each incoming message
 * if the connection doesn't have any channel in common with that user.
 * The connection isn't even informed about potential nick changes of that 
 * user. Hence, though a <code>UserListener</code> would be nice, that concept
 * barely exists in IRC. 
 * <p>
 * Some words about the implementation: In <code>User</code> one should
 * use the get- and setters instead of direct access of class fields, because
 * of the special subclass <code>ChannelUser</code>. Using 
 * <code>getNick()</code> in <code>equals()</code>, for example, guarantees
 * proper behavior of <code>equals()</code> for <code>ChannelUser</code>
 * objects, too.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class User implements Comparable {
	private String nickname;
	private String username;
	private String host;
	private boolean away = false;
	
	User(String nickname) {
		this(nickname, null, null);
	}
	
	User(IRCUser user) {
		if (user.getNick() == null && user.getHost() != null) {
			throw new RuntimeException("nickname must not be null");
		}
		if (user.getNick() != null) {
			this.nickname = user.getNick();
		} else {
			this.nickname = user.getUsername();
		}
		this.username = user.getUsername();
		this.host = user.getHost();
	}
	
	User(String nickname, String username, String host) {
		if (nickname == null) {
			throw new RuntimeException("nickname must not be null");
		}
		this.nickname = nickname;
		this.username = username;
		this.host = host;
	}
	
	void update(IRCUser user) {
		if (!getNick().equals(user.getNick())) {
			throw new IllegalArgumentException();
		}
		setUsername(user.getUsername());
		setHost(user.getHost());
	}
	
	void setNick(String newNick) {
		this.nickname = newNick;
	}
	
	public String getNick() {
		return nickname;
	}
	
	void setHost(String host) {
		this.host = host;
	}
	
	public String getHost() {
		return host;
	}
	
	void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isAway() {
		return away;
	}

	public void setAway(boolean away) {
		this.away = away;
	}

	public String toString() {
		return getNick();
	}

	public int compareTo(Object other) {
		return getNick().compareToIgnoreCase(((User)other).getNick());
	}
	
	public boolean isSame(Object obj) {
		if (obj instanceof User) {
			return getNick().equalsIgnoreCase(((User)obj).getNick());
		} else if (obj instanceof String) {
			return getNick().equalsIgnoreCase((String)obj);
		} else {
			return false;
		}
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return getNick().equalsIgnoreCase(((User)obj).getNick());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return getNick().hashCode();
	}
}
