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

import java.net.Proxy;

/**
 * A configuration to use when creating a new {@link IRCConnection}.
 *
 * @see IRCConnectionFactory#newConnection(IRCConfig)
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCConfig {

    /**
     * @return the encoding to use when reading and writing IRC messages
     */
    String getEncoding();

    /**
     * @return hostname or IP address of the IRC server to connect to
     */
    String getHost();

    /**
     * @return the preffered nick for the connection
     */
    String getNick();

    /**
     * @return the password to connect with
     */
    String getPassword();

    /**
     * @param index the intex in the internal {@code ports} array
     * @return the port number at position {@code index} in the internal {@code ports} array
     */
    int getPortAt(int index);

    /**
     * @return a copy of internal {@code ports} array
     */
    int[] getPorts();

    /**
     * @return the number of ports in the internal {@code ports} array
     */
    int getPortsCount();

    /**
     * @return the {@link Proxy} to use when connecting
     */
    Proxy getProxy();

    /**
     * @return the real name of the user that is connecting
     */
    String getRealname();

    /**
     * @return the socket timeout in milliseconds
     */
    int getTimeout();

    /**
     * @return the {@link IRCTrafficLogger} that should be notified about incoming and outgoing messages
     */
    IRCTrafficLogger getTrafficLogger();

    /**
     * @return the username to connect with
     */
    String getUsername();

    /**
     * @return {@code true} if automatic PING? PONG! is enabled or {@code false} otherwise.
     */
    boolean isAutoPong();

    /**
     * @return {@code true} if mIRC colorcodes should be removed from incoming IRC messages
     */
    boolean isStripColorsEnabled();

}
