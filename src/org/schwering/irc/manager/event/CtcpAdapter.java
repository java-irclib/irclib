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

	public void clientinfoResponseReceived(CtcpClientinfoResponseEvent event) {
	}

	public void dccChatReceived(CtcpDccChatEvent event) {
	}

	public void dccSendReceived(CtcpDccSendEvent event) {
	}

	public void errmsgRequestReceived(CtcpErrmsgRequestEvent event) {
	}

	public void errmsgResponseReceived(CtcpErrmsgResponseEvent event) {
	}

	public void fingerRequestReceived(CtcpFingerRequestEvent event) {
	}

	public void fingerResponseReceived(CtcpFingerResponseEvent event) {
	}

	public void pingRequestReceived(CtcpPingRequestEvent event) {
	}

	public void pingResponseReceived(CtcpPingResponseEvent event) {
	}

	public void sedReceived(CtcpSedEvent event) {
	}

	public void sourceRequestReceived(CtcpSourceRequestEvent event) {
	}

	public void sourceResponseReceived(CtcpSourceResponseEvent event) {
	}

	public void timeRequestReceived(CtcpTimeRequestEvent event) {
	}

	public void timeResponseReceived(CtcpTimeResponseEvent event) {
	}

	public void unknownRequestEventReceived(CtcpUnknownRequestEvent event) {
	}

	public void unknownResponseEventReceived(CtcpUnknownResponseEvent event) {
	}

	public void userinfoRequestReceived(CtcpUserinfoRequestEvent event) {
	}

	public void userinfoResponseReceived(CtcpUserinfoResponseEvent event) {
	}

	public void versionRequestReceived(CtcpVersionRequestEvent event) {
	}

	public void versionResponseReceived(CtcpVersionResponseEvent event) {
	}
}