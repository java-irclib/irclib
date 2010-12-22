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

/**
 * Adapter for channel listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ChannelAdapter implements ChannelListener {
	public void channelModeReceived(ChannelModeEvent event) {
	}

	public void nickChanged(NickEvent event) {
	}

	public void noticeReceived(MessageEvent event) {
	}

	public void messageReceived(MessageEvent event) {
	}

	public void topicReceived(TopicEvent event) {
	}

	public void userJoined(UserParticipationEvent event) {
	}

	public void userLeft(UserParticipationEvent event) {
	}
	
	public void namesReceived(NamesEvent event) {
	}
	
	public void whoReceived(WhoEvent event) {
	}
	
	public void userStatusChanged(UserStatusEvent event) {
	}
	
	public void banlistReceived(BanlistEvent event) {
	}
}
