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
		this.nickname = user.getNick();
		this.username = user.getUsername();
		this.host = user.getHost();
	}
	
	User(String nickname, String username, String host) {
		this.nickname = nickname;
		this.username = username;
		this.host = host;
	}
	
	User(User oldUser, String newNick) {
		this.nickname = newNick;
		this.username = oldUser.username;
		this.host = oldUser.host;
		this.away = oldUser.away;
	}
	
	void update(IRCUser user) {
		if (!nickname.equals(user.getNick())) {
			throw new IllegalArgumentException();
		}
		username = user.getUsername();
		host = user.getHost();
	}
	
	void update(String username, String host) {
		this.username = username;
		this.host = host;
	}
	
	void setNick(String newNick) {
		this.nickname = newNick;
	}
	
	public String getNick() {
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
		return getNick();
	}

	public int compareTo(Object other) {
		return nickname.compareToIgnoreCase(((User)other).nickname);
	}
	
	public boolean isSame(Object obj) {
		if (obj instanceof User) {
			return nickname.equalsIgnoreCase(((User)obj).nickname);
		} else if (obj instanceof String) {
			return nickname.equalsIgnoreCase((String)obj);
		} else {
			return false;
		}
	}
	
	public boolean equals(Object obj) {
		return nickname.equalsIgnoreCase(((User)obj).nickname);
	}

	public int hashCode() {
		return nickname.hashCode();
	}
}
