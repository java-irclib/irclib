package org.schwering.irc.manager.event;

/**
 * Adapter for CTCP listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class CtcpAdapter implements CtcpListener {
	public void actionReceived(CtcpActionEvent event) {
	}

	public void clientinfoReceived(CtcpClientinfoEvent event) {
	}

	public void dccReceived(CtcpDccEvent event) {
	}

	public void errmsgReceived(CtcpErrmsgEvent event) {
	}

	public void fingerReceived(CtcpFingerEvent event) {
	}

	public void pingReceived(CtcpPingEvent event) {
	}

	public void sedReceived(CtcpSedEvent event) {
	}

	public void sourceReceived(CtcpSourceEvent event) {
	}

	public void timeReceived(CtcpTimeEvent event) {
	}

	public void unknownEventReceived(CtcpUnknownEvent event) {
	}

	public void userinfoReceived(CtcpUserinfoEvent event) {
	}

	public void versionReceived(CtcpVersionEvent event) {
	}
}
