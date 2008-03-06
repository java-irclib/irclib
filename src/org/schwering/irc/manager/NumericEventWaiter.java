package org.schwering.irc.manager;

import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.NumericEvent;

/**
 * Provides a mechanism to create a buffer in an IRC event handler that
 * collects subsequent IRC events and then firesa manager event. 
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
abstract class NumericEventWaiter extends ConnectionAdapter implements Runnable {
	public static int MILLIS_SLEEP = 500;
	
	private Connection owner;
	protected boolean sleepAgain = false;
	protected long millis = MILLIS_SLEEP;
	
	public NumericEventWaiter(Connection owner) {
		this.owner = owner;
		owner.addConnectionListener(this);
	}
	
	public synchronized void numericErrorReceived(NumericEvent event) {
		sleepAgain = true;
		handle(event);
	}

	public synchronized void numericReplyReceived(NumericEvent event) {
		sleepAgain = true;
		handle(event);
	}
	
	protected abstract void handle(NumericEvent event);
	
	protected abstract void fire();

	public void run() {
		do {
			try {
				Thread.sleep(millis);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} while (getAndSetSleepAgain(false));
		synchronized (this) {
			owner.removeConnectionListener(this);
			fire();
		}
	}
	
	private synchronized boolean getAndSetSleepAgain(boolean v) {
		boolean b = sleepAgain;
		sleepAgain = v;
		return b;
	}
}
