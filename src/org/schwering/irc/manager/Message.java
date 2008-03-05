package org.schwering.irc.manager;

import org.schwering.irc.lib.IRCUtil;

/**
 * Creates a new message object that wraps a simple string message.
 * This container allows to easily extract CTCP commands and strip 
 * mIRC color codes.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class Message {
	private String msg;
	
	public Message(String msg) {
		this.msg = msg;
	}
	
	public String getText() {
		return msg;
	}
	
	public String getColorFilteredText() {
		return IRCUtil.parseColors(msg);
	}
	
	public String getCTCPFilteredText() {
		return CTCPUtil.removeCTCP(msg);
	}
	
	public String getFilteredText() {
		return IRCUtil.parseColors(CTCPUtil.removeCTCP(msg)); 
	}
	
	public String toString() {
		return msg;
	}
}
