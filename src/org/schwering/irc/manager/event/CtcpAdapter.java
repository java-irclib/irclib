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
 * Adapter for <code>CtcpListener</code>.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class CtcpAdapter implements CtcpListener {

	public void actionReceived(CtcpActionEvent event) {
	}

	public void clientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
	}

	public void clientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
	}

	public void dccChatReceived(CtcpDccChatEvent event) {
	}

	public void dccSendReceived(CtcpDccSendEvent event) {
	}

	public void errmsgRequestReceived(CtcpErrmsgRequestEvent event) {
	}

	public void errmsgReplyReceived(CtcpErrmsgReplyEvent event) {
	}

	public void fingerRequestReceived(CtcpFingerRequestEvent event) {
	}

	public void fingerReplyReceived(CtcpFingerReplyEvent event) {
	}

	public void pingRequestReceived(CtcpPingRequestEvent event) {
	}

	public void pingReplyReceived(CtcpPingReplyEvent event) {
	}

	public void sedReceived(CtcpSedEvent event) {
	}

	public void sourceRequestReceived(CtcpSourceRequestEvent event) {
	}

	public void sourceReplyReceived(CtcpSourceReplyEvent event) {
	}

	public void timeRequestReceived(CtcpTimeRequestEvent event) {
	}

	public void timeReplyReceived(CtcpTimeReplyEvent event) {
	}

	public void unknownRequestEventReceived(CtcpUnknownRequestEvent event) {
	}

	public void unknownReplyEventReceived(CtcpUnknownReplyEvent event) {
	}

	public void userinfoRequestReceived(CtcpUserinfoRequestEvent event) {
	}

	public void userinfoReplyReceived(CtcpUserinfoReplyEvent event) {
	}

	public void versionRequestReceived(CtcpVersionRequestEvent event) {
	}

	public void versionReplyReceived(CtcpVersionReplyEvent event) {
	}
}