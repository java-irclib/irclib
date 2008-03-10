package org.schwering.irc.manager;

import java.util.Collections;
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
import org.schwering.irc.lib.IRCConstants;
import org.schwering.irc.lib.IRCUtil;
import org.schwering.irc.manager.event.BanlistEvent;
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
import org.schwering.irc.manager.event.UserStatusEvent;
import org.schwering.irc.manager.event.WhoisEvent;

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
		owner.fireInvitationReceived(event);
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
			case IRCConstants.ERR_NICKNAMEINUSE:
			case IRCConstants.ERR_ERRONEUSNICKNAME:
			case IRCConstants.ERR_NONICKNAMEGIVEN:
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
	
	public void onReply(int num, String val, String msg) {
		System.out.println("onReply("+num+","+val+","+msg+")");
		boolean handled = false;
		try {
			handled |= whoisChain.numericReceived(num, val, msg);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		try {
			handled |= namesChain.numericReceived(num, val, msg);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		try {
			handled |= topicChain.numericReceived(num, val, msg);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		try {
			handled |= banlistChain.numericReceived(num, val, msg);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		try {
			handled |= motdChain.numericReceived(num, val, msg);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		if (num == IRCConstants.RPL_WHOREPLY || num == IRCConstants.RPL_ENDOFWHO) {
			handleWhoReply(val, msg);
		} else if (num == IRCConstants.RPL_WHOWASUSER || num == IRCConstants.RPL_ENDOFWHOWAS) {
			// terminated by RPL_ENDOFWHOWAS
		} else if (num == IRCConstants.RPL_LIST) {
			// terminated by RPL_ENDOFLIST
		} else if (num == IRCConstants.RPL_LINKS || num == IRCConstants.RPL_ENDOFLINKS) {
			// terminated by RPL_ENDOFLINKS
		}
		
		if (!handled) {
			NumericEvent event = new NumericEvent(owner, num, val, msg); 
			owner.fireNumericReplyReceived(event);
		}
	}
	
	private NumericEventChain topicChain = new NumericEventChain(
			new int[] { IRCConstants.RPL_TOPIC, IRCConstants.RPL_NOTOPIC, IRCConstants.RPL_TOPICINFO }, 
			IRCConstants.RPL_TOPICINFO) {
		protected Object getInitObject(String id) {
			return new Topic(owner.resolveChannel(id));
		}

		protected void handle(Object obj, int num, String val,
				String msg) {
			Topic topic = (Topic)obj;
			if (num == IRCConstants.RPL_TOPIC) {
				Message message = new Message(msg);
				topic.setMessage(message);
			} else if (num == IRCConstants.RPL_NOTOPIC) {
				topic.setMessage(null);
			} else if (num == IRCConstants.RPL_TOPICINFO) {
				StringTokenizer tokenizer = new StringTokenizer(val);
				tokenizer.nextToken();
				tokenizer.nextToken();
				User user = owner.resolveUser(tokenizer.nextToken());
				topic.setUser(user);
				try {
					long millis = Long.parseLong(msg) * 1000;
					Date date = new Date(millis);
					topic.setDate(date);
				} catch (Exception exc) {
				}
			}
		}
			
		protected void fire(Object obj) {
			Topic topic = (Topic)obj;
			TopicEvent event = new TopicEvent(owner, topic);
			owner.fireTopicReceived(event);
			topic.getChannel().fireTopicReceived(event);
		}
	};
	
	private NumericEventChain banlistChain = new NumericEventChain(
			IRCConstants.RPL_BANLIST, IRCConstants.RPL_ENDOFBANLIST) {
		class Banlist {
			Channel channel;
			Vector list = new Vector();
			
			Banlist(Channel channel) { this.channel = channel; }
		}
		
		protected Object getInitObject(String id) {
			return new Banlist(owner.resolveChannel(id));
		}

		protected void handle(Object obj, int num, String val,
				String msg) {
			Banlist banlist = (Banlist)obj;
			banlist.list.add(msg);
		}
		
		protected void fire(Object obj) {
			Banlist banlist = (Banlist)obj;
			BanlistEvent event = new BanlistEvent(owner, banlist.channel, 
					banlist.list);
			owner.fireBanlistReceived(event);
			banlist.channel.fireBanlistReceived(event);
		}
	};
	
	private NumericEventChain whoisChain = new NumericEventChain(
			new int[] { IRCConstants.RPL_WHOISUSER,
					IRCConstants.RPL_WHOISSERVER,
					IRCConstants.RPL_WHOISCHANNELS,
					IRCConstants.RPL_WHOISOPERATOR,
					IRCConstants.RPL_WHOISIDLE,
					IRCConstants.RPL_WHOISAUTHNAME },
					IRCConstants.RPL_ENDOFWHO
			) {
		class Whois {
			User user;
			String username, host, realName, server, serverInfo, authName;
			boolean operator = false, idle = false;
			Message awayMessage;
			long millisIdle, millisSignon;
			List channels = new LinkedList();
			
			Whois(String id) { user = owner.resolveUser(id); }
		}
		
		protected Object getInitObject(String id) {
			return new Whois(id);
		}

		protected void handle(Object obj, int num, String val,
				String msg) {
			Whois whois = (Whois)obj;
			val = skipFirstToken(val);
			if (whois.user == null) {
				String nick = getFirstToken(val);
				whois.user = owner.resolveUser(nick);
			}
			val = skipFirstToken(val);
			if (num == IRCConstants.RPL_WHOISUSER) {
				StringTokenizer valTok = new StringTokenizer(val);
				whois.username = valTok.nextToken();
				whois.host = valTok.nextToken();
				whois.realName = msg;
			} else if (num == IRCConstants.RPL_WHOISSERVER) {
				whois.server = val;
				whois.serverInfo = msg;
			} else if (num == IRCConstants.RPL_WHOISOPERATOR) {
				whois.operator = true;
			} else if (num == IRCConstants.RPL_WHOISIDLE) {
				whois.idle = true;
				StringTokenizer valTok = new StringTokenizer(val);
				try {
					whois.millisIdle = Long.parseLong(valTok.nextToken()) * 1000;
				} catch (Exception exc) {
					whois.millisIdle = -1;
					exc.printStackTrace();
				}
				try {
					whois.millisSignon = Long.parseLong(valTok.nextToken()) * 1000;
				} catch (Exception exc) {
					whois.millisSignon = -1;
					exc.printStackTrace();
				}
			} else if (num == IRCConstants.RPL_WHOISCHANNELS) {
				StringTokenizer msgTok = new StringTokenizer(msg);
				while (msgTok.hasMoreTokens()) {
					String tok = msgTok.nextToken();
					whois.channels.add(tok);
				}
			} else if (num == IRCConstants.RPL_WHOISAUTHNAME) {
				whois.authName = val;
			}
		}

		protected void fire(Object obj) {
			Whois whois = (Whois)obj;
			if (whois.username != null && whois.host != null) {
				whois.user.update(whois.username, whois.host);
			}
			if (whois.idle) {
				whois.user.setAway(true);
			}
			Date dateSignon = null;
			if (whois.millisSignon != -1) {
				dateSignon = new Date(whois.millisSignon);
			}
			WhoisEvent event = new WhoisEvent(owner, whois.user, 
					whois.realName, whois.authName, whois.server, 
					whois.serverInfo, whois.operator, dateSignon,
					whois.idle, whois.millisIdle, whois.awayMessage, 
					whois.channels);
			owner.fireWhoisReceived(event);
		}
	};
	
	private NumericEventChain namesChain = new NumericEventChain(
			new int[] { IRCConstants.RPL_NAMREPLY, IRCConstants.RPL_ENDOFNAMES }, 
			IRCConstants.RPL_ENDOFNAMES) {
		class Names {
			Channel channel;
			List channelUsers = new LinkedList();
			
			Names(String id) { channel = owner.resolveChannel(id); }
		}
		
		protected Object getInitObject(String id) {
			return new Names(id);
		}

		protected void handle(Object obj, int num, String val,
				String msg) {
			if (num == IRCConstants.RPL_ENDOFNAMES) {
				return;
			}
			
			Names names = (Names)obj;
			String str = skipFirstToken(val) +" "+ msg;
			StringTokenizer tokenizer = new StringTokenizer(str);
			String first = tokenizer.nextToken();
			if (first.equals("@") || first.equals("*") || first.equals("=")) {
				tokenizer.nextToken();
			}
			tokenizer.nextToken(); // skip channel name
			while (tokenizer.hasMoreTokens()) {
				String tok = tokenizer.nextToken();
				int status = ChannelUser.NONE;
				if (tok.charAt(0) == '@') {
					status = ChannelUser.OPERATOR;
					tok = tok.substring(1);
				} else if (tok.charAt(0) == '+') {
					status = ChannelUser.VOICED;
					tok = tok.substring(1);
				}
				User user = owner.resolveUser(tok);
				ChannelUser chanUser = names.channel.getUser(user);
				if (chanUser == null) {
					chanUser = new ChannelUser(names.channel, user, status);
				} else if (chanUser != null && chanUser.getStatus() != status) {
					UserStatusEvent event = new UserStatusEvent(owner, 
							names.channel, chanUser);
					names.channel.fireUserStatusChanged(event);
				}
				names.channelUsers.add(chanUser);
			}
		}
		
		protected String getID(int num, String val, String msg) {
			if (num == IRCConstants.RPL_NAMREPLY) {
				String str = val +" "+ msg;
				StringTokenizer tokenizer = new StringTokenizer(str);
				tokenizer.nextToken();
				String tok = tokenizer.nextToken();
				if (tok.equals("@") || tok.equals("*") || tok.equals("=")) {
					return tokenizer.nextToken();
				} else {
					return tok;
				}
			} else {
				return super.getID(num, val, msg);
			}
		}
		
		protected void fire(Object obj) {
			Names names = (Names)obj;
			NamesEvent event = new NamesEvent(owner, names.channel, names.channelUsers);
			owner.fireNamesReceived(event);
			names.channel.fireNamesReceived(event);
		}
	};
	
	private NumericEventChain motdChain = new NumericEventChain(
			IRCConstants.RPL_MOTDSTART,
			new int[] { IRCConstants.RPL_MOTDSTART, IRCConstants.RPL_MOTD }, 
			IRCConstants.RPL_ENDOFMOTD
			) {
		protected Object getInitObject(String id) {
			return new LinkedList();
		}

		protected void handle(Object obj, int num, String val,
				String msg) {
			List motd = (List)obj;
			motd.add(msg);
		}
		
		protected void fire(Object obj) {
			MOTDEvent event = new MOTDEvent(owner, (List)obj);
			owner.fireMotdReceived(event);
		}
	};
	
	private static String skipFirstToken(String str) {
		str = str.trim();
		boolean found = false;
		for (int i = 0; i < str.length(); i++) {
			if (!found && Character.isWhitespace(str.charAt(i))) {
				found = true;
			} else if (found && !Character.isWhitespace(str.charAt(i))) {
				return str.substring(i);
			}
		}
		return "";
	}
	
	private Set blockedWhoChannels = Collections.synchronizedSet(new HashSet());
	
	private void handleWhoReply(String val, String msg) {
		val = skipFirstToken(val); // skip own name
		final Channel channel = owner.resolveChannel(val);
		if (blockedWhoChannels.contains(channel)) {
			return;
		}
		blockedWhoChannels.add(channel);
		final List who = new Vector();
		handleWhoLine(who, msg);
		new NumericEventWaiter(owner) {
			protected boolean handle(NumericEvent event) {
				if (event.getNumber() == IRCConstants.RPL_WHOREPLY || event.getNumber() == IRCConstants.RPL_ENDOFWHO) {
					String val = skipFirstToken(event.getValue()); // skip own name
					Channel secondChannel = owner.resolveChannel(val);
					boolean rightChannel = channel.isSame(secondChannel);
					if (rightChannel && event.getNumber() == IRCConstants.RPL_WHOREPLY) {
						handleWhoLine(who, event.getMessage());
						return true;
					} else if (rightChannel && event.getNumber() == IRCConstants.RPL_ENDOFWHO) {
						return false;
					}
				}
				return true;
			}
			
			protected void fire() {
				blockedWhoChannels.remove(channel);
//				WhoEvent event = new WhoEvent(owner, channel, who);
//				owner.fireWhoReceived(event);
//				if (owner.hasChannel(channel)) {
//					channel.fireWhoReceived(event);
//				}
			}
		};
	}
	
	private void handleWhoLine(List who, String msg) {
		// TODO implement, format: <channel> <user> <host> <server> <nick> <H|G>[*][@|+] :<hopcount> <real name>
	}
	
	public void onNick(IRCUser ircUser, String newNick) {
		User user = owner.resolveUser(ircUser);
		String oldNick = user.getNick();
		user.setNick(newNick);
		NickEvent event = new NickEvent(owner, user, oldNick);
		for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			if (channel.hasUser(oldNick)) {
				channel.removeUser(oldNick); // re-add user with new nick
				channel.addUser(user);
			}
		}
		owner.fireNickChanged(event);
		for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
			Channel channel = (Channel)it.next();
			if (channel.hasUser(user)) {
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
			UserParticipationEvent event = new UserParticipationEvent(owner,
					null, user, UserParticipationEvent.QUIT, new Message(msg));
			owner.fireUserLeft(event);
			for (Iterator it = owner.getChannels().iterator(); it.hasNext(); ) {
				Channel channel = (Channel)it.next();
				UserParticipationEvent event2 = new UserParticipationEvent(owner,
						channel, user, UserParticipationEvent.QUIT, 
						new Message(msg));
				if (channel.hasUser(user)) {
					channel.fireUserLeft(event2);
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
		User sender = owner.resolveUser(ircUser);
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.resolveChannel(target);
			MessageEvent event = new MessageEvent(owner, sender, channel, msg);
			owner.fireNoticeReceived(event);
			channel.fireNoticeReceived(event);
		} else {
			User user = owner.resolveUser(target);
			MessageEvent event = new MessageEvent(owner, sender, user, 
					new Message(msg));
			owner.fireNoticeReceived(event);
			owner.firePrivateNoticeReceived(event);
		}
	}

	public void onPrivmsg(String target, IRCUser ircUser, String msg) {
		User sender = owner.resolveUser(ircUser);
		if (IRCUtil.isChan(target)) {
			Channel channel = owner.resolveChannel(target);
			MessageEvent event = new MessageEvent(owner, sender, channel, msg);
			owner.fireMessageReceived(event);
			channel.fireMessageReceived(event);
		} else {
			User user = owner.resolveUser(target);
			MessageEvent event = new MessageEvent(owner, sender, user, 
					new Message(msg));
			owner.fireMessageReceived(event);
			owner.firePrivateMessageReceived(event);
		}
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
			owner.fireUserJoined(event);
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
			owner.fireUserLeft(event);
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
			owner.fireUserLeft(event);
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
		owner.fireTopicReceived(event);
		channel.fireTopicReceived(event);
	}

	public void onMode(String chan, IRCUser ircUser, IRCModeParser modeParser) {
		Channel channel = owner.resolveChannel(chan);
		User user = owner.resolveUser(ircUser);
		ChannelModeEvent event = new ChannelModeEvent(owner, channel, user, 
				modeParser);
		owner.fireChannelModeReceived(event);
		channel.fireChannelModeReceived(event);
		for (int i = 0; i < modeParser.getCount(); i++) { // user status changed
			int mode = modeParser.getModeAt(i);
			if (mode == 'o' || mode == 'v') {
				int status = (mode == 'o') ? ChannelUser.OPERATOR
						: ChannelUser.VOICED;
				int oper = modeParser.getOperatorAt(i);
				String arg = modeParser.getArgAt(i);
				ChannelUser chanUser = channel.getUser(arg);
				int oldStatus = chanUser.getStatus();
				if (oper == '+') {
					chanUser.addStatus(status);
				} else if (oper == '-') {
					chanUser.removeStatus(status);
				}
				if (chanUser.getStatus() != oldStatus) {
					UserStatusEvent e = new UserStatusEvent(owner, channel, 
							chanUser);
					channel.fireUserStatusChanged(e);
				}
			}
		}
	}
}
