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

package org.schwering.irc.manager.event;

import java.util.EventListener;

/**
 * A connection listener listens for general events on connection level.
 * <p>
 * Events that are directly related to a channel the connection participates
 * in are processed by <code>ChannelListener</code>s. Additionally, they're
 * told to all <code>ConnectionListener</code>s.
 * <p>
 * Hence, it is possible to gather all information about incoming 
 * channel-related events by using only a <code>ConnectionListener</code>.
 * However, in most cases it is a good idea to make use of the specialization
 * <code>ChannelListener</code>.
 * <p>
 * Generally, the <code>ConnectionListener</code> events are fired directly
 * before the <code>ChannelListener</code> or 
 * <code>PrivateMessageListener</code> events.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface ConnectionListener extends EventListener {
	/**
	 * Fired when the connection is registered successfully. At this point
	 * of time, the connecting user is fully-privileged.
	 */
	void connectionEstablished(ConnectionEvent event);

	/**
	 * Fired when the connection has been terminated is completely dead.
	 */
	void connectionLost(ConnectionEvent event);
	
	/**
	 * Fired when a serious error occurred. This means in most cases that
	 * the connection will be terminated.
	 */
	void errorReceived(ErrorEvent event);

	/**
	 * Fired when the MOTD was received completely.
	 */
	void motdReceived(MotdEvent event);
	
	/**
	 * Fired when an INFO response was received completely.
	 */
	void infoReceived(InfoEvent event);
	
	/**
	 * Fired when an LINKS response was received completely.
	 */
	void linksReceived(LinksEvent event);
	
	/**
	 * Fired when a STATS response was received completely.
	 */
	void statsReceived(StatsEvent event);

	/**
	 * Fired when the server asked for a ping pong.
	 * <p>
	 * Note: You don't have to answer the request, this is done automatically.
	 */
	void pingReceived(PingEvent event);
	
	/**
	 * Fired when you've joined a channel.
	 */
	void channelJoined(UserParticipationEvent event);

	/**
	 * Fired when you've left a channel, either by parting or being kicked.
	 */
	void channelLeft(UserParticipationEvent event);
	
	/**
	 * Fired when someone is invited by someone else.
	 * <p>
	 * Note: The invited user is always you.
	 */
	void invitationReceived(InvitationEvent event);
	
	/**
	 * Fired when the invitation was successfully delivered to somebody.
	 * <p>
	 * Note: The inviting user is always you.
	 */
	void invitationDeliveryReceived(InvitationEvent event);
	
	/**
	 * Fired when a numeric reply is received.
	 * <p>
	 * Note: Many queries are answered with numeric replies, for example
	 * WHOIS, WHO, NAMES. The topic is transmitted via numeric replies when
	 * joining a channel, too. Since, for example, these topic replies are
	 * processed by this library and lead to <code>topicReceived</code>
	 * events of the <code>ChannelListener</code> and/or this listener, you 
	 * shouldn't process the respective replies (RPL_TOPIC and RPL_TOPICINFO)
	 * in this event handler. The purpose of this event handler is to 
	 * process those numeric replies that aren't processed by the library.
	 * @see org.schwering.irc.lib.IRCConstants
	 */
	void numericReplyReceived(NumericEvent event);
	
	/**
	 * Fired when a numeric error was received. This is the case when 
	 * requesting a WHOIS answer for a non-existent user, for example.
	 * @see org.schwering.irc.lib.IRCConstants
	 */
	void numericErrorReceived(NumericEvent event);
	
	/**
	 * Fired when the user mode of someone changed or received.
	 * Examples are when someone gets network (!) operator status or if 
	 * someone unmarks someone was network operator.
	 */
	void userModeReceived(UserModeEvent event);
	
	/**
	 * Fired when a WHOIS of a user is received.
	 */
	void whoisReceived(WhoisEvent event);
	
	/**
	 * Fired when a WHOWAS of a not-present user was received.
	 */
	void whowasReceived(WhowasEvent event);
	
	/* Redundant events (with ChannelListener) */
	
	/**
	 * Fired when another user joins any known channel. Known channels are those
	 * in which the connection participates.
	 * @see ChannelListener#userJoined(UserParticipationEvent)
	 */
	void userJoined(UserParticipationEvent event);

	/**
	 * Fired when another user leaves any known channel. This can be either by
	 * parting, quitting or being kicked. Known channels are those
	 * in which the connection participates.
	 * @see ChannelListener#userLeft(UserParticipationEvent)
	 */
	void userLeft(UserParticipationEvent event);

	/**
	 * Fired when a topic of any channel was received.
	 * This is the case directly after joining a channel, when requesting
	 * the topic of any channel and when a known channel's topic is changed.
	 * Known channels are those in which the connection participates.
	 * @see ChannelListener#topicReceived(TopicEvent)
	 */
	void topicReceived(TopicEvent event);
	
	/**
	 * Fired when a LIST response was received.
	 * Note that the response is not further processed, i.e. though the
	 * response contains topics, no <code>topicReceived</code> events
	 * are fired.
	 */
	void listReceived(ListEvent event);

	/**
	 * Fired when a known user changed his nickname.
	 * Known users are basically those in channels in which the connection 
	 * participates.
	 * @see ChannelListener#nickChanged(NickEvent)
	 */
	void nickChanged(NickEvent event);
	
	/**
	 * Fired when a channel mode was initially received or changed.
	 * Channel modes include the user channel modes like operator status
	 * or voiced status, but also stuff like channel keys.
	 * @see ChannelListener#channelModeReceived(ChannelModeEvent)
	 */
	void channelModeReceived(ChannelModeEvent event);
	
	/**
	 * Fired when a PRIVMSG as user-to-user or user-to-channel communication
	 * comes in. This event comes along with a respective event in
	 * <code>ChannelListener</code> or <code>PrivateMessageListener</code>.
	 * @see ChannelListener#messageReceived(MessageEvent)
	 * @see PrivateMessageListener#messageReceived(MessageEvent)
	 */
	void messageReceived(MessageEvent event);
	
	/**
	 * Fired when a NOTICE as user-to-user or user-to-channel communication
	 * comes in. This event comes along with a respective event in
	 * <code>ChannelListener</code> or <code>PrivateMessageListener</code>.
	 * @see ChannelListener#noticeReceived(MessageEvent)
	 * @see PrivateMessageListener#noticeReceived(MessageEvent)
	 */
	void noticeReceived(MessageEvent event);
	
	/**
	 * Fired when a NAMES list of any channel was received.
 	 * @see ChannelListener#namesReceived(NamesEvent)
	 */
	void namesReceived(NamesEvent event);
	
	/**
	 * Fired when a WHO list of any channel was received.
	 * @see ChannelListener#whoReceived(WhoEvent)
	 */
	void whoReceived(WhoEvent event);
	
	/**
	 * Fired when a banlist of any channel is received.
 	 * @see ChannelListener#banlistReceived(BanlistEvent)
	 */
	void banlistReceived(BanlistEvent event);
}
