package org.schwering.irc.manager;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.IRCUtil;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.ErrorEvent;
import org.schwering.irc.manager.event.InvitationEvent;
import org.schwering.irc.manager.event.MOTDEvent;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.NumericEvent;
import org.schwering.irc.manager.event.PingEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UnexpectedEvent;
import org.schwering.irc.manager.event.UserModeEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;

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
		owner.fireConnectionEstablished(new ConnectionEvent(owner));
	}

	public void onDisconnected() {
		registered = false;
		owner.fireConnectionLost(new ConnectionEvent(owner));
		owner.clearChannels();
	}

	public void onError(String msg) {
		ErrorEvent event = new ErrorEvent(owner, msg);
		owner.fireErrorReceived(event);
	}

	public void onInvite(String chan, IRCUser user, String passiveNick) {
		User invitingUser = owner.resolveUser(user);
		User invitedUser = owner.resolveUser(passiveNick);
		Channel channel = owner.resolveChannel(chan);
		InvitationEvent event = new InvitationEvent(owner, channel, 
				invitingUser, invitedUser);
		owner.fireInvited(event);
	}

	public void onPing(String ping) {
		PingEvent event = new PingEvent(owner, ping);
		owner.firePingReceived(event);
	}

	public void onMode(IRCUser user, String passiveNick, String mode) {
		User activeUser = owner.resolveUser(user);
		User passiveUser = owner.resolveUser(passiveNick);
		UserModeEvent event = new UserModeEvent(owner, activeUser, passiveUser,
				mode);
		owner.fireUserModeReceived(event);
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
		} else {
			NumericEvent event = new NumericEvent(owner, num, null, msg); 
			owner.fireNumericErrorReceived(event);
		}
	}

	public void onReply(int num, String value, String msg) {
		if (num == IRCUtil.RPL_TOPIC) {
			handleTopicReply(value, msg);
		} else if (num == IRCUtil.RPL_NOTOPIC) {
			Channel channel = owner.resolveChannel(value);
			Topic topic = new Topic(channel, null, null, null);
			TopicEvent event = new TopicEvent(owner, topic);
			if (owner.hasChannel(channel)) {
				channel.fireTopicReceived(event);
			} else {
				owner.fireTopicReceived(event);
			}
		} else if (num == IRCUtil.RPL_NAMREPLY) {
			handleNamesReply(value, msg);
		} else if (num == IRCUtil.RPL_WHOREPLY) {
			handleWhoReply(value, msg);
		} else if (num == IRCUtil.RPL_WHOWASUSER) {
			// terminated by RPL_ENDOFWHOWAS
		} else if (num == IRCUtil.RPL_WHOISUSER 
				|| num == IRCUtil.RPL_WHOISSERVER
				|| num == IRCUtil.RPL_WHOISCHANNELS
				|| num == IRCUtil.RPL_WHOISOPERATOR
				|| num == IRCUtil.RPL_WHOISIDLE
				|| num == IRCUtil.RPL_WHOISCHANNELS) {
			// handle whois, RPL_WHOISCHANNELS may appear multiple times, formats:
			// RPL_WHOISUSER: <nick> <user> <host> * :<real name>
			// RPL_WHOISSERVER: <nick> <server> :<server info>
			// RPL_WHOISOPERATOR: <nick> :is an IRC operator
			// RPL_WHOISIDLE: <nick> <integer> :seconds idle
			// RPL_WHOISCHANNELS <nick> :{[@|+]<channel><space>}
			// RPL_ENDOFWHOIS terminates
		} else if (num == IRCUtil.RPL_LIST) {
			// termianted by RPL_ENDOFLIST
		} else if (num == IRCUtil.RPL_BANLIST) {
			// terminated by RPL_ENDOFBANLIST, format <channel> <banid>
		} else if (num == IRCUtil.RPL_LINKS) {
			// terminated by RPL_ENDOFLINKS
		} else if (num == IRCUtil.RPL_MOTDSTART) {
			handleMOTD(msg);
		}
		NumericEvent event = new NumericEvent(owner, num, value, msg); 
		owner.fireNumericReplyReceived(event);
	}
	
	private void handleTopicReply(String val, String msg) {
		StringTokenizer valTok = new StringTokenizer(val);
		valTok.nextToken(); // skip first (our name)
		final Channel channel = owner.resolveChannel(valTok.nextToken());
		final Message message = (msg == null || msg.trim().length() > 0) ? new Message(msg) : null;
		
		new NumericEventWaiter(owner) { // waits for topic info (user, date)
			private User user = null;
			private Date date = null;
			
			protected boolean handle(NumericEvent event) {
				if (event.getNumber() == IRCUtil.RPL_TOPICINFO) {
					StringTokenizer valTok = new StringTokenizer(event.getValue()); // skip first (our name)
					Channel secondChannel = owner.resolveChannel(valTok.nextToken());
					if (secondChannel.isSame(channel)) {
						user = owner.resolveUser(valTok.nextToken());
						try {
							String seconds = event.getMessage();
							long millis = Long.parseLong(seconds) * 1000;
							date = new Date(millis);
						} catch (Exception exc) {
							date = null;
						}
					}
					return false;
				} else {
					return true;
				}
			}
			
			protected void fire() {
				Topic topic = new Topic(channel, message, user, date);
				TopicEvent event = new TopicEvent(owner, topic);
				if (owner.hasChannel(channel)) {
					channel.setTopic(topic);
					channel.fireTopicReceived(event);
				} else {
					owner.fireTopicReceived(event);
				}
			}
		};
	}
	
	private Set blockedNamesChannels = new HashSet();
	
	private void handleNamesReply(String val, String msg) {
		final Channel channel = owner.resolveChannel(val);
		if (blockedNamesChannels.contains(channel)) {
			return;
		}
		blockedNamesChannels.add(channel);
		final List names = new Vector();
		handleNamesLine(names, msg);
		new NumericEventWaiter(owner) {
			protected boolean handle(NumericEvent event) {
				Channel secondChannel = owner.resolveChannel(event.getValue());
				boolean rightChannel = channel.isSame(secondChannel);
				if (rightChannel && event.getNumber() == IRCUtil.RPL_NAMREPLY) {
					handleNamesLine(names, event.getMessage());
					return true;
				} else if (rightChannel && event.getNumber() == IRCUtil.RPL_ENDOFNAMES) {
					blockedNamesChannels.remove(channel);
					return false;
				} else {
					return true;
				}
			}
			
			protected void fire() {
				NamesEvent event = new NamesEvent(owner, channel, names);
				if (owner.hasChannel(channel)) {
					channel.fireNamesReceived(event);
				} else {
					owner.fireNamesReceived(event);
				}
			}
		};
	}
	
	private void handleNamesLine(List names, String msg) {
		StringTokenizer tok = new StringTokenizer(msg);
		while (tok.hasMoreTokens()) {
			String name = tok.nextToken();
			int status = ChannelUser.NONE;
			if (name.charAt(0) == '@') {
				status = ChannelUser.OPERATOR;
				name = name.substring(1);
			} else if (name.charAt(0) == '+') {
				status = ChannelUser.VOICED;
				name = name.substring(1);
			}
			User user = owner.resolveUser(name);
			ChannelUser channelUser = new ChannelUser(user, status);
			names.add(channelUser);
		}
	}
	
	private Set blockedWhoChannels = new HashSet();
	
	private void handleWhoReply(String val, String msg) {
		final Channel channel = owner.resolveChannel(val);
		if (blockedWhoChannels.contains(channel)) {
			return;
		}
		blockedWhoChannels.add(channel);
		final List who = new Vector();
		handleWhoLine(who, msg);
		new NumericEventWaiter(owner) {
			protected boolean handle(NumericEvent event) {
				Channel secondChannel = owner.resolveChannel(event.getValue());
				boolean rightChannel = channel.isSame(secondChannel);
				if (rightChannel && event.getNumber() == IRCUtil.RPL_WHOREPLY) {
					handleWhoLine(who, event.getMessage());
					return true;
				} else if (rightChannel && event.getNumber() == IRCUtil.RPL_ENDOFWHO) {
					blockedWhoChannels.remove(channel);
					return false;
				} else {
					return true;
				}
			}
			
			protected void fire() {
				NamesEvent event = new NamesEvent(owner, channel, who);
				if (owner.hasChannel(channel)) {
					channel.fireNamesReceived(event);
				} else {
					owner.fireNamesReceived(event);
				}
			}
		};
	}
	
	private void handleWhoLine(List who, String msg) {
		// TODO implement, format: <channel> <user> <host> <server> <nick> <H|G>[*][@|+] :<hopcount> <real name>
	}
	
	private void handleMOTD(String msg) {
		final List motd = new LinkedList();
		motd.add(msg);
		new NumericEventWaiter(owner) {
			protected boolean handle(NumericEvent event) {
				if (event.getNumber() == IRCUtil.RPL_MOTD) {
					motd.add(event.getMessage());
					return true;
				} else if (event.getNumber() == IRCUtil.RPL_ENDOFMOTD) {
					return false;
				} else {
					return true;
				}
			}
			
			protected void fire() {
				String[] arr = new String[motd.size()];
				int i = 0;
				for (Iterator it = motd.iterator(); it.hasNext(); ) {
					arr[i++] = (String)it.next();
				}
				MOTDEvent event = new MOTDEvent(owner, arr);
				owner.fireMotdReceived(event);
			}
		};
	}

	public void onNick(IRCUser user, String newNick) {
		User oldUser = owner.resolveUser(user);
		User newUser = new User(oldUser, newNick);
		NickEvent event = new NickEvent(owner, oldUser, newUser);
		for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			if (channel.hasUser(oldUser)) {
				channel.removeUser(oldUser);
				channel.addUser(newUser);
				channel.fireNickChanged(event);
			}
		}
	}

	public void onQuit(IRCUser ircUser, String msg) {
		User user = owner.resolveUser(ircUser);
		if (user.getNick().equals(owner.getNick())) {
			for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
				Channel channel = (Channel)it.next();
				UserParticipationEvent event = new UserParticipationEvent(owner,
						channel, user, UserParticipationEvent.QUIT, 
						new Message(msg));
				owner.fireChannelLeft(event);
				owner.removeChannel(channel);
			}
		} else {
			for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
				Channel channel = (Channel)it.next();
				if (channel.hasUser(user)) {
					UserParticipationEvent event = new UserParticipationEvent(owner,
							channel, user, UserParticipationEvent.QUIT, 
							new Message(msg));
					channel.fireUserLeft(event);
					channel.removeUser(user);
				}
			}
		}
	}

	public void unknown(String prefix, String command, String middle,
			String trailing) {
		Object[] args = new Object[] { prefix, command, middle };
		UnexpectedEvent event = new UnexpectedEvent(owner, "unknown", args);
		owner.fireUnexpectedEventReceived(event);
	}
	
	public void onNotice(String target, IRCUser ircUser, String msg) {
		MessageEvent event;
		User sender = owner.resolveUser(ircUser);
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.resolveChannel(target);
			event = new MessageEvent(owner, sender, channel, msg);
		} else {
			User user = owner.resolveUser(target);
			event = new MessageEvent(owner, sender, user, new Message(msg));
		}
		owner.fireNoticeReceived(event);
	}

	public void onPrivmsg(String target, IRCUser ircUser, String msg) {
		MessageEvent event;
		User sender = owner.resolveUser(ircUser);
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.resolveChannel(target);
			event = new MessageEvent(owner, sender, channel, msg);
		} else {
			User user = owner.resolveUser(target);
			event = new MessageEvent(owner, sender, user, new Message(msg));
		}
		owner.fireNoticeReceived(event);
	}
	
	/* Channel events */

	public void onJoin(String chan, IRCUser ircUser) {
		Channel channel = owner.resolveChannel(chan);
		User user = owner.resolveUser(ircUser);
		UserParticipationEvent event = new UserParticipationEvent(owner, 
				channel, user, UserParticipationEvent.JOIN);
		if (user.getNick().equals(owner.getNick())) {
			owner.addChannel(channel);
			channel.addUser(user);
			owner.fireChannelJoined(event);
			if (owner.getRequestModes()) {
				owner.getIRCConnection().doMode(chan);
				owner.getIRCConnection().doMode(chan, "+b");
			}
		} else {
			channel.addUser(user);
			channel.fireUserJoined(event);
		}
	}

	public void onPart(String chan, IRCUser ircUser, String msg) {
		Channel channel = owner.resolveChannel(chan);
		User user = owner.resolveUser(ircUser);
		UserParticipationEvent event = new UserParticipationEvent(owner, 
				channel, user, UserParticipationEvent.PART, 
				new Message(msg));
		if (user.getNick().equals(owner.getNick())) {
			owner.fireChannelLeft(event);
			channel.removeUser(user);
			owner.removeChannel(channel);
		} else {
			channel.fireUserLeft(event);
			channel.removeUser(user);
		}
	}

	public void onKick(String chan, IRCUser ircUser, String passiveNick, 
			String msg) {
		Channel channel = owner.resolveChannel(chan);
		User user = owner.resolveUser(ircUser);
		User kickingUser = owner.resolveUser(passiveNick);
		User kickedUser = owner.resolveUser(passiveNick);
		UserParticipationEvent event = new UserParticipationEvent(owner, 
				channel, user, UserParticipationEvent.KICK, 
				new Message(msg), kickingUser);
		if (user.getNick().equals(owner.getNick())) {
			owner.fireChannelLeft(event);
			channel.removeUser(kickedUser);
			owner.removeChannel(channel);
		} else {
			channel.fireUserLeft(event);
			channel.removeUser(kickedUser);
		}
	}
	
	public void onTopic(String chan, IRCUser ircUser, String msg) {
		Channel channel = owner.resolveChannel(chan);
		Message message = (msg == null || msg.trim().length() > 0) ? new Message(msg) : null;
		User user = owner.resolveUser(ircUser);
		Date date = new Date();
		Topic topic = new Topic(channel, message, user, date);
		channel.setTopic(topic);
		TopicEvent event = new TopicEvent(owner, topic);
		channel.fireTopicReceived(event);
	}

	public void onMode(String chan, IRCUser ircUser, IRCModeParser modeParser) {
		Channel channel = owner.resolveChannel(chan);
		User user = owner.resolveUser(ircUser);
		ChannelModeEvent event = new ChannelModeEvent(owner, channel, user, 
				modeParser);
		channel.fireChannelModeReceived(event);
	}
}
