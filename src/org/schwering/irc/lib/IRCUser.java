/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCUser
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

package org.schwering.irc.lib;

/**
 * Holds variables for the nick, username and host of a user.
 * <p>
 * It's used to pack these information in one object.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.02
 * @see IRCEventListener
 * @see IRCParser
 */
public class IRCUser {
	
	/** 
	 * The user's nickname.
	 */
	private String nick;
	
	/**
	 * The user's username.
	 */
	private String username;
	
	/**
	 * The user's host.
	 */
	private String host;
	
// ------------------------------
	
	/**
	 * Creates a new <code>IRCUser</code> object.
	 * @param nick The user's nickname.
	 * @param username The user's username.
	 * @param host The user's host.
	 */
	public IRCUser(String nick, String username, String host) {
		this.nick = nick;
		this.username = username;
		this.host = host;
	}
	
// ------------------------------
	
	/** 
	 * Returns the nickname of the person who sent the line 
	 * or the servername of the server which sent the line. <br />
	 * If no nickname is given, <code>null</code> is returned.
	 * <br /><br />
	 * <b>Note:</b> This method is totally equal to <code>getServername</code>!
	 * @return The nickname or the servername of the line. If no nick is given,
	 *         <code>null</code> is returned.
	 * @see #getServername()
	 * @see #getUsername()
	 * @see #getHost()
	 */
	public String getNick() {
		return nick;
	}
	
// ------------------------------
	
	/** 
	 * Returns the servername of the server which sent the line or the nickname of
	 * the person who sent the line. <br />
	 * If no nickname is given, <code>null</code> is returned.
	 * <br /><br />
	 * <b>Note:</b> This method is totally equal to <code>getNick</code>!
	 * @return The servername or the nickname of the line. If no server is given,
	 *         <code>null</code> is returned.
	 * @see #getNick()
	 */
	public String getServername() {
		return getNick();
	}
	
// ------------------------------
	
	/** 
	 * Returns the username of the person who sent the line. <br />
	 * If the username is not specified, this method returns <code>null</code>.
	 * @return The username of the line; <code>null</code> if it's not given.
	 * @see #getNick()
	 * @see #getHost()
	 */
	public String getUsername() {
		return username;
	}
	
// ------------------------------
	
	/** 
	 * Returns the host of the person who sent the line. <br />
	 * If the host is not specified, this method returns <code>null</code>.
	 * @return The host of the line; <code>null</code> if it's not given.
	 * @see #getNick()
	 * @see #getUsername()
	 */
	public String getHost() {
		return host;
	}
	
// ------------------------------
	
	/**
	 * Returns the nickname.
	 * @return The nickname.
	 */
	public String toString() {
		return getNick();
	}
}
