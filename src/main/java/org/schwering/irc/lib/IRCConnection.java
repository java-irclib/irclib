/**
 * IRClib - A Java Internet Relay Chat library
 * Copyright (C) 2006-2015 Christoph Schwering <schwering@gmail.com>
 * and/or other contributors as indicated by the @author tags.
 *
 * This library and the accompanying materials are made available under the
 * terms of the
 *  - GNU Lesser General Public License,
 *  - Apache License, Version 2.0 and
 *  - Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */
package org.schwering.irc.lib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * A connection to an IRC server.
 * <p>
 * Typical usage:
 * <pre>
 * IRCConfig config = IRCConfigBuilder.newBuilder()
 *          .host("irc.freenode.net")
 *          .port(6667)
 *          .username(System.getProperty("user.name"))
 *          .password("secret")
 *          .realname(System.getProperty("user.name"))
 *          .build();
 * IRCConnection connection = IRCConnectionFactory.newConnection(config);
 * connection.addIRCEventListener(new IRCEventAdapter() {
 *     &#x2F;* implement whatever you need *&#x2F;
 * });
 * connection.connect();
 * connection.doJoin("#test");
 * connection.doPrivmsg("#test", "Hello World!");
 * connection.close();
 * </pre>
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCConnection {
    /** Value returned when there is no timeout to deliver. */
    int INVALID_TIMEOUT = -1;

    /**
     * Adds a new {@link IRCEventListener} which listens for actions coming from
     * the IRC server.
     *
     * @param l
     *            An instance of the {@link IRCEventListener} interface.
     * @throws IllegalArgumentException
     *             If <code>listener</code> is <code>null</code>.
     */
    void addIRCEventListener(IRCEventListener l);

    /**
     * Close the connection forcefully.
     * <p>
     * This method does <b>not</b> send <code>QUIT</code> IRC command. Consider
     * using {@link #doQuit()} or {@link #doQuit(String)} to send the proper
     * QUIT command to the server.
     * <p>
     * This method should be used only when there is a good reason for that,
     * e.g. that the IRC server does not react to <code>QUIT</code> command.
     * <p>
     * Possibly occuring <code>IOException</code>s are handled according to the
     * set exception handling.
     *
     * @see #connect()
     * @see #doQuit
     * @see #doQuit(String)
     */
    void close();

    /**
     * Establish a connection to the server. This method must be invoked to
     * start a connection as the constructor does not do that.
     * <p>
     * It tries all ports from {@link IRCConfig#getPorts()} until the connecting
     * succeeds. If all ports fail an <code>IOException</code> is thrown.
     * <p>
     * This method can be invokde once only.
     *
     * @throws IOException
     *             If an I/O error occurs.
     * @throws NoSuchAlgorithmException rethrown from the SSL layer
     * @throws KeyManagementException rethrown from the SSL layer
     * @throws SocketException
     *             If the <code>connect</code> method was already invoked.
     * @see #isConnected()
     * @see #doQuit()
     * @see #doQuit(String)
     * @see #close()
     */
    void connect() throws IOException, KeyManagementException, NoSuchAlgorithmException;

    /**
     * Removes away message.
     */
    void doAway();

    /**
     * Sets away message.
     *
     * @param msg
     *            The away message.
     */
    void doAway(String msg);

    /**
     * Invites a user to a channel.
     *
     * @param nick
     *            The nickname of the user who should be invited.
     * @param chan
     *            The channel the user should be invited to.
     */
    void doInvite(String nick, String chan);

    /**
     * Checks if one or more nicks are used on the server.
     *
     * @param nick
     *            The nickname of the user we search for.
     */
    void doIson(String nick);

    /**
     * Joins a channel without a key.
     *
     * @param chan
     *            The channel which is to join.
     */
    void doJoin(String chan);

    /**
     * Joins a channel with a key.
     *
     * @param chan
     *            The channel which is to join.
     * @param key
     *            The key of the channel.
     */
    void doJoin(String chan, String key);

    /**
     * Kicks a user from a channel.
     *
     * @param chan
     *            The channel somebody should be kicked from.
     * @param nick
     *            The nickname of the user who should be kicked.
     */
    void doKick(String chan, String nick);


    /**
     * Kicks a user from a channel with a comment.
     *
     * @param chan
     *            The channel somebody should be kicked from.
     * @param nick
     *            The nickname of the user who should be kicked.
     * @param msg
     *            The optional kickmessage.
     */
    void doKick(String chan, String nick, String msg);

    /**
     * Lists all channels with their topic and status.
     */
    void doList();

    /**
     * Lists channel(s) with their topic and status.
     *
     * @param chan
     *            The channel the <code>LIST</code> refers to.
     */
    void doList(String chan);

    /**
     * Requests a Reply 324 for the modes of a given channel.
     *
     * @param chan
     *            The channel the <code>MODE</code> request is refering to.
     */
    void doMode(String chan);

    /**
     * Sends a mode to the server.
     * The first argument is a nickname (user-mode) or a channel (channel-mode).
     * <code>String mode</code> must contain the operators (+/-), the modes
     * (o/v/i/k/l/p/s/w) and the possibly values (nicks/banmask/limit/key).
     *
     * @param target
     *            The nickname or channel of the user whose modes will be
     *            changed.
     * @param mode
     *            The new modes.
     */
    void doMode(String target, String mode);

    /**
     * Lists all visible users.
     */
    void doNames();

    /**
     * Lists all visible users of (a) channel(s).
     *
     * @param chan
     *            The channel the <code>NAMES</code> command is refering to.
     */
    void doNames(String chan);

    /**
     * Changes the nickname.
     *
     * @param nick
     *            The new nickname.
     */
    void doNick(String nick);

    /**
     * Notices a message to a person or a channel.
     *
     * @param target
     *            The nickname or channel (group) the message should be sent to.
     * @param msg
     *            The message which should be transmitted.
     */
    void doNotice(String target, String msg);

    /**
     * Parts from a given channel.
     *
     * @param chan
     *            The channel you want to part from.
     */
    void doPart(String chan);

    /**
     * Parts from a given channel with a given parg-msg.
     *
     * @param chan
     *            The channel you want to part from.
     * @param msg
     *            The optional partmessage.
     */
    void doPart(String chan, String msg);

    /**
     * Quits from the IRC server with a quit-msg.
     *
     * @param ping
     *            The ping which was received in <code>onPing</code>. It's a
     *            <code>String</code>, because sometimes on some networks the
     *            server-hostname (for example splatterworld.quakenet.org) is
     *            given as parameter which would throw an Exception if we gave
     *            the ping as long.
     */
    void doPong(String ping);

    /**
     * Sends a message to a person or a channel.
     *
     * @param target
     *            The nickname or channel the message should be sent to.
     * @param msg
     *            The message which should be transmitted.
     */
    void doPrivmsg(String target, String msg);

    /**
     * Quits from the IRC server. Calls the <code>disconnect</code>-method which
     * does the work actually.
     *
     * @see #isConnected()
     * @see #connect()
     * @see #doQuit(String)
     * @see #close()
     */
    void doQuit();

    /**
     * Quits from the IRC server with a quit-msg. Calls the
     * <code>disconnect</code>-method which does the work actually.
     *
     * @param msg
     *            The optional quitmessage.
     * @see #isConnected()
     * @see #connect()
     * @see #doQuit()
     * @see #close()
     */
    void doQuit(String msg);

    /**
     * Requests the topic of a chan. The topic is given in a numeric reply.
     *
     * @param chan
     *            The channel which topic should be requested.
     */
    void doTopic(String chan);

    /**
     * Changes the topic of a chan.
     *
     * @param chan
     *            The channel which topic is changed.
     * @param topic
     *            The new topic.
     */
    void doTopic(String chan, String topic);

    /**
     * Requires host-information about up to 5 users which must be listed and
     * divided by spaces.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    void doUserhost(String nick);

    /**
     * Requests information about users matching the given criteric, for example
     * a channel they are on.
     *
     * @param criteric
     *            The criterics of the <code>WHO</code> query.
     */
    void doWho(String criteric);

    /**
     * Requires information about an existing user.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    void doWhois(String nick);

    /**
     * Requires host-information about a user, who is not connected anymore.
     *
     * @param nick
     *            The nickname of the user the query is refering to.
     */
    void doWhowas(String nick);

    /**
     * Returns the local address of the connection socket. If the connection is
     * not yet connected, <code>null</code> is returned.
     *
     * @return the local address
     */
    InetAddress getLocalAddress();

    /**
     * Returns the nickname of this instance.
     *
     * @return The nickname.
     */
    String getNick();

    /**
     * Returns the port to which the <code>IRCConnection</code> connected, or
     * <code>0</code> if the connection failed or wasn't tried yet.
     *
     * @return The port to which the <code>IRCConnection</code>, or
     *         <code>0</code> if the connection failed or wasn't tried yet.
     */
    int getPort();

    /**
     * Returns the timeout of the socket.
     * If an error occurs, which is never the case, <code>-1</code> is returned.
     * The possibly occuring <code>IOException</code> are handled according to
     * the set exception handling.
     *
     * @return The timeout.
     */
    int getTimeout();

    /**
     * Tells whether there's a connection to the IRC network or not.
     * If <code>connect</code> wasn't called yet, it returns <code>false</code>.
     *
     * @return The status of the connection; <code>true</code> if it's
     *         connected.
     * @see #connect()
     * @see #doQuit()
     * @see #doQuit(String)
     * @see #close()
     */
    boolean isConnected();

    /**
     * @return {@code true} if the connection is using SSL
     */
    boolean isSSL();

    /**
     * Removes the first occurence of the given {@link IRCEventListener} from
     * the listener-vector.
     *
     * @param l
     *            An instance of the {@link IRCEventListener} interface.
     * @return <code>true</code> if the listener was successfully removed;
     *         <code>false</code> if it was not found.
     */
    boolean removeIRCEventListener(IRCEventListener l);

    /**
     * Sends a String to the server. You should use this method only, if you
     * must do it. For most purposes, there are <code>do*</code> methods (like
     * <code>doJoin</code>). A carriage return line feed (<code>\r\n</code>) is
     * appended automatically.
     *
     * @param line
     *            The line which should be send to the server without the
     *            trailing carriage return line feed (<code>\r\n</code>).
     */
    void send(String line);

}
