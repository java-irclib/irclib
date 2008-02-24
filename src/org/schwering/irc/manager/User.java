package org.schwering.irc.manager;

import org.schwering.irc.lib.IRCUser;

public class User implements Comparable {
	private String nickname;
	private String username;
	private String host;
	private boolean away = false;
	
	User(String nickname) {
		this(nickname, null, null);
	}
	
	User(String nickname, String username, String host) {
		if (nickname == null) {
			throw new IllegalArgumentException();
		}
		this.nickname = nickname;
		this.username = username;
		this.host = host;
	}
	
	void update(IRCUser user) {
		if (!nickname.equals(user.getNick())) {
			throw new IllegalArgumentException();
		}
		username = user.getUsername();
		host = user.getHost();
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getHost() {
		return host;
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
		return getNickname();
	}

	public int compareTo(Object other) {
		return nickname.compareTo(((User)other).nickname);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		return nickname.equals(((User)obj).nickname);
	}

	public int hashCode() {
		return nickname.hashCode();
	}
}
