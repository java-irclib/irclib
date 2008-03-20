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
	void errmsgReceived(CtcpErrmsgEvent event);
	void actionReceived(CtcpActionEvent event);
	void userinfoReceived(CtcpUserinfoEvent event);
	void clientinfoReceived(CtcpClientinfoEvent event);
	void pingReceived(CtcpPingEvent event);
	void sourceReceived(CtcpSourceEvent event);
	void versionReceived(CtcpVersionEvent event);
	void unknownEventReceived(CtcpUnknownEvent event);
	void fingerReceived(CtcpFingerEvent event);
	void timeReceived(CtcpTimeEvent event);
	void dccReceived(CtcpDccEvent event);
	void sedReceived(CtcpSedEvent event);
}
