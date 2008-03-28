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
 * Listener for channel-related events. Examples are incoming messages, 
 * nick changes and joining and leaving users.
 * <p>
 * Events that are directly related to a channel the connection participates
 * in are processed by <code>ChannelListener</code>s. Additionally, they're
 * told to all <code>ConnectionListener</code>s except for the 
 * <code>userStatusChanged</code> event which makes only sense in close relation
 * to a channel and is a redundant event in the sense that is a specialization
 * of <code>channelModeChanged</code>.
 * <p>
 * Hence, it is possible to gather all information about incoming 
 * channel-related events by using only a <code>ConnectionListener</code>.
 * However, in most cases it is a good idea to make use of the specialization
 * <code>ChannelListener</code>.
 * <p>
 * Generally, the <code>ConnectionListener</code> events are fired directly
 * before the <code>ChannelListener</code> events.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface ChannelListener extends EventListener {
	/**
	 * Fired when another user joins the channel.
	 */
	void userJoined(UserParticipationEvent event);

	/**
	 * Fired when another user leaves the channel. This can be either by
	 * parting, quitting or being kicked.
	 */
	void userLeft(UserParticipationEvent event);

	/**
	 * Fired when a topic of the channel was received.
	 * This is the case directly after joining the channel, when requesting
	 * the topic of the channel and when the channel's topic is changed.
	 */
	void topicReceived(TopicEvent event);

	/**
	 * Fired when a user in the channel changed his nickname.
	 */
	void nickChanged(NickEvent event);
	
	/**
	 * Fired when a channel mode was initially received or changed.
	 * Channel modes include the user channel modes like operator status
	 * or voiced status, but also stuff like channel keys.
	 * <p>
	 * The {@link #userStatusChanged(UserStatusEvent)} is a specialization
	 * of this event that informs about user channel mode changes.
	 * @see #userStatusChanged(UserStatusEvent)
	 */
	void channelModeReceived(ChannelModeEvent event);
	
	/**
	 * Fired when a PRIVMSG to the channel comes in.
	 */
	void messageReceived(MessageEvent event);
	
	/**
	 * Fired when a NOTICE to the channel comes in.
	 */
	void noticeReceived(MessageEvent event);
	
	/**
	 * Fired when a NAMES list of the channel was received.
	 */
	void namesReceived(NamesEvent event);
	
	/**
	 * Fired when a WHO list of the channel was received.
	 */
	void whoReceived(WhoEvent event);
	
	/**
	 * Fired when the banlist is received.
	 */
	void banlistReceived(BanlistEvent event);
	
	/**
	 * Fired when the status of a user changed. This can be triggered by
	 * an incoming NAMES list, for example, or an incoming MODE command.
	 * <p>
	 * However, this is just an additional event to the existing 
	 * <code>namesReceived</code> and <code>channelModeReceived</code> events.
	 * Note that when the connection joins an empty channel, it automatically
	 * gets operator status. This is not the result of a MODE command 
	 * executed by anyone, it is implicitly. However, this information is
	 * included in the NAMES list received when joining a channel and therefore
	 * propagated with this event.
	 * <p>
	 * This event is fired after the <code>channelModeChanged</code> event,
	 * but before <code>namesReceived</code> is fired.
	 */
	void userStatusChanged(UserStatusEvent event);
}
