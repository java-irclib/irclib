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
package org.schwering.irc.lib.impl;

import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.lib.util.IRCParser;

/**
 * An immutable {@link IRCUser}.
 *
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @see IRCEventListener
 * @see IRCParser
 */
public class DefaultIRCUser implements IRCUser {

    /**
     * The user's nickname.
     */
    private final String nick;

    /**
     * The user's username.
     */
    private final String username;

    /**
     * The user's host.
     */
    private final String host;


    /**
     * Creates a new {@link IRCUser}.
     *
     * @param nick The user's nickname.
     * @param username The user's username.
     * @param host The user's host.
     */
    public DefaultIRCUser(String nick, String username, String host) {
        this.nick = nick;
        this.username = username;
        this.host = host;
    }


    /**
     * @return The nickname or the servername of the line or {@code null} if no nick is given
     */
    @Override
    public String getNick() {
        return nick;
    }

    /**
     * @return The username of the lineor {@code null} if it's not given.
     */
    @Override
    public String getUsername() {
        return username;
    }


    /**
     * @return The host of the lineor {@code null}  if it's not given.
     */
    @Override
    public String getHost() {
        return host;
    }


    /**
     * @return The nickname.
     */
    public String toString() {
        return getNick();
    }
}
