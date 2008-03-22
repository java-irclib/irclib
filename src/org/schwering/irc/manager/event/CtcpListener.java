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

	void errmsgResponseReceived(CtcpErrmsgResponseEvent event);
	void userinfoResponseReceived(CtcpUserinfoResponseEvent event);
	void clientinfoResponseReceived(CtcpClientinfoResponseEvent event);
	void pingResponseReceived(CtcpPingResponseEvent event);
	void sourceResponseReceived(CtcpSourceResponseEvent event);
	void versionResponseReceived(CtcpVersionResponseEvent event);
	void fingerResponseReceived(CtcpFingerResponseEvent event);
	void timeResponseReceived(CtcpTimeResponseEvent event);
	void unknownResponseEventReceived(CtcpUnknownResponseEvent event);
}