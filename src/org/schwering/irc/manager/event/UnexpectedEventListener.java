package org.schwering.irc.manager.event;

public interface UnexpectedEventListener {
	void unexpectedEventReceived(String event, Object[] args);
}
