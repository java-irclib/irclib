package org.schwering.irc.manager;

public class Message {
	String msg;
	
	public Message(String msg) {
		this.msg = msg;
	}
	
	public String getText() {
		return msg;
	}
}
