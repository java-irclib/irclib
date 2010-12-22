/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCEventListener
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

import java.util.EventListener;

/**
 * Used as listener for incoming events like messages.
 * <p>
 * The <code>IRCEventListener</code> is used by the 
 * <code>IRCConnection.addEventListener(IRCEventListener)</code> method to add
 * a listener which listens to the connection for incoming IRC events like 
 * <code>PRIVMSG</code>s or numeric replies.
 * <p>
 * Supported events:
 * <ul>
 * <li>Connect</li>
 * <li>Disconnect</li>
 * <li>Error</li>
 * <li>Invite</li>
 * <li>Join</li>
 * <li>Kick</li>
 * <li>Private Message</li>
 * <li>Mode (Chan)</li>
 * <li>Mode (User)</li>
 * <li>Nick</li>
 * <li>Notice</li>
 * <li>Numeric Reply</li>
 * <li>Numeric Error</li>
 * <li>Part</li>
 * <li>Ping</li>
 * <li>Quit</li>
 * <li>Topic</li>
 * </ul>
 * <p>
 * For other, unkown events there's the <code>unknown</code>-method.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.64
 * @see IRCEventAdapter
 * @see IRCConnection
 */
public interface IRCEventListener extends EventListener, IRCConstants {
	
	/** 
	 * Fired when the own connection is successfully established. 
	 * This is the case when the first PING? is received. <br />
	 * This happens between the connection is opened with a socket and the 
	 * connection is registered: The client sends his information to the server 
	 * (nickname, username). The server says hello to you by sending you 
	 * some <code>NOTICE</code>s. And if your nickname is invalid or in use or 
	 * anything else is wrong with your nickname, it asks you for a new one.
	 */
	public void onRegistered();
	
// ------------------------------
	
	/** 
	 * Fired when the own connection is broken.
	 */
	public void onDisconnected();
	
// ------------------------------
	
	/** 
	 * Fired when an <code>ERROR</code> command is received.
	 * @param msg The message of the error.
	 */
	public void onError(String msg);
	
// ------------------------------
	
	/** 
	 * Fired when a numeric error is received.
	 * The server often sends numeric errors (wrong nickname etc.). 
	 * The <code>msg</code>'s format is different for every reply. All replies'
	 * formats are described in the {@link org.schwering.irc.lib.IRCUtil}. 
	 * @param num The identifier (usually a 3-digit number).
	 * @param msg The message of the error. 
	 */
	public void onError(int num, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when somebody is invited to a channel.
	 * @param chan The channel the user is invited to.
	 * @param user The user who invites another. Contains nick, username and host.
	 * @param passiveNick The nickname of the user who is invited by another user 
	 *                    (passive).
	 */
	public void onInvite(String chan, IRCUser user, String passiveNick);
	
// ------------------------------
	
	/** 
	 * Fired when somebody joins a channel.
	 * @param chan The channel the person joins.
	 * @param user The user who joins. Contains nick, username and host.
	 */
	public void onJoin(String chan, IRCUser user);
	
// ------------------------------
	
	/** 
	 * Fired when somebody is kicked from a channel.
	 * @param chan The channel somebody is kicked from.
	 * @param user The user who kicks another user from a channel. 
	 *             Contains nick, username and host.
	 * @param passiveNick The nickname of the user who is kicked from a channel 
	 *                    (passive).
	 * @param msg The message the active user has set. This is <code>""</code> if 
	 *            no message was set.
	 */
	public void onKick(String chan, IRCUser user, String passiveNick, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when an operator changes the modes of a channel. 
	 * For example, he can set somebody as an operator, too, or take him the 
	 * oper-status. 
	 * Also keys, moderated and other channelmodes are fired here.
	 * @param chan The channel in which the modes are changed. 
	 * @param user The user who changes the modes. 
	 *             Contains nick, username and host.
	 * @param modeParser The <code>IRCModeParser</code> object which contains the 
	 *                   parsed information about the modes which are changed. 
	 */
	public void onMode(String chan, IRCUser user, IRCModeParser modeParser);
	
// ------------------------------
	
	/** 
	 * Fired when somebody changes somebody's usermodes. 
	 * Note that this event is not fired when a channel-mode is set, for example
	 * when someone sets another user as operator or the mode moderated.
	 * @param user The user who changes the modes of another user or himself. 
	 *             Contains nick, username and host.
	 * @param passiveNick The nickname of the person whose modes are changed by 
	 *                    another user or himself. 
	 * @param mode The changed modes which are set.
	 */
	public void onMode(IRCUser user, String passiveNick, String mode);
	
// ------------------------------
	
	/** 
	 * Fired when somebody changes his nickname successfully.
	 * @param user The user who changes his nickname. 
	 *             Contains nick, username and host.
	 * @param newNick The new nickname of the user who changes his nickname.
	 */
	public void onNick(IRCUser user, String newNick);
	
// ------------------------------
	
	/** 
	 * Fired when somebody sends a <code>NOTICE</code> to a user or a group. 
	 * @param target The channel or nickname the user sent a <code>NOTICE</code> 
	 *               to.
	 * @param user The user who notices another person or a group. 
	 *             Contains nick, username and host.
	 * @param msg The message.
	 */
	public void onNotice(String target, IRCUser user, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when somebody parts from a channel.
	 * @param chan The channel somebody parts from.
	 * @param user The user who parts from a channel. 
	 *             Contains nick, username and host.
	 * @param msg The part-message which is optionally. 
	 *            If it's empty, msg is <code>""</code>.
	 */
	public void onPart(String chan, IRCUser user, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when a <code>PING</code> comes in. 
	 * The IRC server tests in different periods if the client is still there by 
	 * sending PING &lt;ping&gt;. The client must response PONG &lt;ping&gt;.
	 * @param ping The ping which is received from the server.
	 */
	public void onPing(String ping);
	
// ------------------------------
	
	/** 
	 * Fired when a user sends a <code>PRIVMSG</code> to a user or to a
	 * group.
	 * @param target The channel or nickname the user sent a <code>PRIVMSG</code> 
	 *               to.
	 * @param user The user who sent the <code>PRIVMSG</code>. 
	 *             Contains nick, username and host.
	 * @param msg The message the user transmits.
	 */
	public void onPrivmsg(String target, IRCUser user, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when somebody quits from the network.
	 * @param user The user who quits. Contains nick, username and host.
	 * @param msg The optional message. <code>""</code> if no message is set by 
	 *            the user.
	 */
	public void onQuit(IRCUser user, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when a numeric reply is received. 
	 * For example, <code>WHOIS</code> queries are answered by the server with 
	 * numeric replies. 
	 * The <code>msg</code>'s format is different for every reply. All replies'
	 * formats are described in the {@link org.schwering.irc.lib.IRCUtil}. 
	 * The first word in the <code>value</code> is always your own nickname! 
	 * @param num The numeric reply. 
	 * @param value The first part of the message.
	 * @param msg The main part of the message.
	 */
	public void onReply(int num, String value, String msg);
	
// ------------------------------
	
	/** 
	 * Fired when the topic is changed by operators. 
	 * Note that the topic is given as a numeric reply fired in 
	 * <code>onReply</code> when you join a channel.
	 * @param chan The channel where the topic is changed. 
	 * @param user The user who changes the topic. 
	 *             Contains nick, username and host.
	 * @param topic The new topic.
	 */
	public void onTopic(String chan, IRCUser user, String topic);
	
// ------------------------------
	
	/** 
	 * This event is fired when the incoming line can not be identified as a known
	 * event.
	 * @param prefix The prefix of the incoming line.
	 * @param command The command of the incoming line.
	 * @param middle The part until the colon (<code>:</code>).
	 * @param trailing The part behind the colon (<code>:</code>).
	 */
	public void unknown(String prefix, String command, String middle,
			String trailing);
	
}
