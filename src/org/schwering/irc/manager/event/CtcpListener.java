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
 * Listener for Ctcp events in incoming PRIVMSG and NOTICE messages.
 * <p>
 * <code>CtcpListener</code>s can be added to <code>Channel</code>s and
 * <code>Connection</code> objects. The normal <code>CtcpListener</code>s
 * added to a <code>Connection</code> handle <i>all</i> incoming Ctcp
 * events; the <i>private</i> <code>CtcpListener</code>s in added to a
 * <code>Connection</code> handle only user-to-user-sent Ctcp events;
 * the <code>CtcpListener</code>s added to <code>Channel</code>s handle 
 * only user-to-group-sent Ctcp events.
 * (The idea is similar to the handling of incoming PRIVMSGs and NOTICEs
 * in <code>ConnectionListener</code>s, <code>PrivateMessageListener</code>s
 * and <code>ChannelListener</code>s.)
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface CtcpListener extends EventListener {
	void actionReceived(CtcpActionEvent event);
	void dccChatReceived(CtcpDccChatEvent event);
	void dccSendReceived(CtcpDccSendEvent event);
	void sedReceived(CtcpSedEvent event);
	
	void errmsgRequestReceived(CtcpErrmsgRequestEvent event);
	void userinfoRequestReceived(CtcpUserinfoRequestEvent event);
	void clientinfoRequestReceived(CtcpClientinfoRequestEvent event);
	void pingRequestReceived(CtcpPingRequestEvent event);
	void sourceRequestReceived(CtcpSourceRequestEvent event);
	void versionRequestReceived(CtcpVersionRequestEvent event);
	void fingerRequestReceived(CtcpFingerRequestEvent event);
	void timeRequestReceived(CtcpTimeRequestEvent event);
	void unknownRequestEventReceived(CtcpUnknownRequestEvent event);

	void errmsgReplyReceived(CtcpErrmsgReplyEvent event);
	void userinfoReplyReceived(CtcpUserinfoReplyEvent event);
	void clientinfoReplyReceived(CtcpClientinfoReplyEvent event);
	void pingReplyReceived(CtcpPingReplyEvent event);
	void sourceReplyReceived(CtcpSourceReplyEvent event);
	void versionReplyReceived(CtcpVersionReplyEvent event);
	void fingerReplyReceived(CtcpFingerReplyEvent event);
	void timeReplyReceived(CtcpTimeReplyEvent event);
	void unknownReplyEventReceived(CtcpUnknownReplyEvent event);
}