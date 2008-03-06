package org.schwering.irc.manager.event;

/**
 * Listener for unexpected events.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface UnexpectedEventListener {
	void unexpectedEventReceived(UnexpectedEvent event);
}
