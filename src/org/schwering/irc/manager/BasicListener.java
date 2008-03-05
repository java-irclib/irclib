package org.schwering.irc.manager;

import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

/**
 * Distributes the <code>IRCEventListener</code> events to the respective
 * listeners of a <code>Connection</code> object.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
class BasicListener implements IRCEventListener {
	private Connection owner;
	
	public BasicListener(Connection owner) {
		this.owner = owner;
	}

	public void onRegistered() {
		owner.fireConnectionEstablished();
	}

	public void onDisconnected() {
		owner.fireConnectionLost();
	}

	public void onError(String msg) {
		owner.fireErrorReceived(new Message(msg));
	}

	public void onInvite(String chan, IRCUser user, String passiveNick) {
		if (passiveNick != null && passiveNick.equals(owner.getNickname())) {
			owner.fireInvited(owner.resolveChannel(chan), 
					owner.resolveUser(user));
		} else {
			Object[] args = new Object[] {
					chan, user, passiveNick
			};
			owner.fireUnexpectedEventReceived("onInvite", args);
		}
	}

	public void onError(int num, String msg) {
	}

	public void onJoin(String chan, IRCUser user) {
	}

	public void onKick(String chan, IRCUser user, String passiveNick, String msg) {
	}

	public void onMode(IRCUser user, String passiveNick, String mode) {
	}

	public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {
	}

	public void onNick(IRCUser user, String newNick) {
	}

	public void onNotice(String target, IRCUser user, String msg) {
	}

	public void onPart(String chan, IRCUser user, String msg) {
	}

	public void onPing(String ping) {
	}

	public void onPrivmsg(String target, IRCUser user, String msg) {
	}

	public void onQuit(IRCUser user, String msg) {
	}

	public void onReply(int num, String value, String msg) {
	}

	public void onTopic(String chan, IRCUser user, String topic) {
	}

	public void unknown(String prefix, String command, String middle,
			String trailing) {
	}

}
