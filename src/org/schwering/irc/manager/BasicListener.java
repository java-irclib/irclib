package org.schwering.irc.manager;

import java.util.Iterator;

import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.IRCUtil;

/**
 * Distributes the <code>IRCEventListener</code> events to the respective
 * listeners of a <code>Connection</code> object.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
class BasicListener implements IRCEventListener {
	private Connection owner;
	private boolean registered = false;
	
	public BasicListener(Connection owner) {
		this.owner = owner;
	}

	/* Connection events */
	
	public void onRegistered() {
		registered = true;
		owner.fireConnectionEstablished();
	}

	public void onDisconnected() {
		registered = false;
		owner.fireConnectionLost();
		owner.clearChannels();
	}

	public void onError(String msg) {
		owner.fireErrorReceived(new Message(msg));
	}

	public void onInvite(String chan, IRCUser user, String passiveNick) {
		if (passiveNick.equals(owner.getNick())) {
			owner.fireInvited(owner.resolveChannel(chan), 
					owner.resolveUser(user));
		} else {
			Object[] args = new Object[] { chan, user, passiveNick };
			owner.fireUnexpectedEventReceived("onInvite", args);
		}
	}

	public void onPing(String ping) {
		owner.firePingReceived(new Message(ping));
	}

	public void onMode(IRCUser user, String passiveNick, String mode) {
		// XXX ???
	}

	public void onError(int num, String msg) {
		if (!registered) { // ask for a new nickname
			switch (num) {
			case IRCUtil.ERR_NICKNAMEINUSE:
			case IRCUtil.ERR_ERRONEUSNICKNAME:
			case IRCUtil.ERR_NONICKNAMEGIVEN:
				String newNick = owner.getNickGenerator().createNewNick();
				if (newNick != null) {
					owner.getIRCConnection().doNick(newNick);
				} else {
					owner.getIRCConnection().doQuit();
				}
			}
		}
	}

	public void onReply(int num, String value, String msg) {
	}

	public void onNick(IRCUser user, String newNick) {
		User oldUser = owner.resolveUser(user);
		User newUser = new User(oldUser, newNick);
		for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			if (channel.hasUser(oldUser)) {
				channel.removeUser(oldUser);
				channel.addUser(newUser);
				channel.fireNickChanged(oldUser, newUser);
			}
		}
	}

	public void onQuit(IRCUser ircUser, String msg) {
		User user = owner.resolveUser(ircUser);
		for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			if (channel.hasUser(user)) {
				channel.removeUser(user);
				
			}
		}
	}

	public void unknown(String prefix, String command, String middle,
			String trailing) {
		Object[] args = new Object[] { prefix, command, middle };
		owner.fireUnexpectedEventReceived("unknown", args);
	}
	
	public void onNotice(String target, IRCUser user, String msg) {
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.getChannel(target);
			if (channel != null) {
				channel.fireNoticeReceived(owner.resolveUser(user), 
						new Message(msg));
			} else {
				Object[] args = new Object[] { target, user, msg };
				owner.fireUnexpectedEventReceived("onNotice", args);
			}
		} else if (target.equals(owner.getNick())) {
			owner.fireNoticeReceived(owner.resolveUser(user), new Message(msg));
		} else {
			Object[] args = new Object[] { target, user, msg };
			owner.fireUnexpectedEventReceived("onNotice", args);
		}
	}

	public void onPrivmsg(String target, IRCUser user, String msg) {
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.getChannel(target);
			if (channel != null) {
				channel.firePrivmsgReceived(owner.resolveUser(user), 
						new Message(msg));
			} else {
				Object[] args = new Object[] { target, user, msg };
				owner.fireUnexpectedEventReceived("onPrivmsg", args);
			}
		} else if (target.equals(owner.getNick())) {
			owner.firePrivmsgReceived(owner.resolveUser(user), new Message(msg));
		} else {
			Object[] args = new Object[] { target, user, msg };
			owner.fireUnexpectedEventReceived("onPrivmsg", args);
		}
	}
	
	/* Channel events */

	public void onJoin(String chan, IRCUser user) {
		if (user.getNick().equals(owner.getNick())) {
			
		} else {
			
		}
	}

	public void onPart(String chan, IRCUser user, String msg) {
	}

	public void onKick(String chan, IRCUser user, String passiveNick, String msg) {
	}
	
	public void onTopic(String chan, IRCUser user, String topic) {
	}

	public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {
	}
}
