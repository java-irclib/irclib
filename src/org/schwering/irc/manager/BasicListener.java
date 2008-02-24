package org.schwering.irc.manager;

import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

public class BasicListener implements IRCEventListener {

	public void onDisconnected() {
	}

	public void onError(int num, String msg) {
	}

	public void onError(String msg) {
	}

	public void onInvite(String chan, IRCUser user, String passiveNick) {
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

	public void onRegistered() {
	}

	public void onReply(int num, String value, String msg) {
	}

	public void onTopic(String chan, IRCUser user, String topic) {
	}

	public void unknown(String prefix, String command, String middle,
			String trailing) {
	}

}
