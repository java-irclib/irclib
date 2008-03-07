package org.schwering.irc.manager;

import org.schwering.irc.lib.IRCUser;

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
	public static final int NONE = 0;
	public static final int VOICED = 1;
	public static final int OPERATOR = 2;
	
	private User user;
	private int status;
	
	ChannelUser(User user) {
		this(user, NONE);
	}
	
	ChannelUser(User user, int status) {
		super(null, null, null);
		if (user instanceof ChannelUser) {
			throw new RuntimeException("Wrapping a ChannelUser in a " +
					"ChannelUser is a bad idea; this exception shows " +
					"the bad design of the org.schwering.irc.manager package");
		}
		this.user = user;
		this.status = status;
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
		return status == OPERATOR;
	}
	
	public boolean isVoiced() {
		return status == VOICED;
	}

	public int compareTo(Object other) {
		return user.compareTo(other);
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

	public int hashCode() {
		return user.hashCode();
	}

	public boolean isAway() {
		return user.isAway();
	}

	public boolean isSame(Object obj) {
		return user.isSame(obj);
	}

	public void setAway(boolean away) {
		user.setAway(away);
	}

	public String toString() {
		return user.toString();
	}

	void update(IRCUser user) {
		this.user.update(user);
	}
}
