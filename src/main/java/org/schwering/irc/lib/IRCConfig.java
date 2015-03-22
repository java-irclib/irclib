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
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public interface IRCConfig {

    /**
     * @return the encoding to use when reading and writing IRC messages
     */
    public abstract String getEncoding();

    /**
     * @return hostname or IP address of the IRC server to connect to
     */
    public abstract String getHost();

    /**
     * @return the preffered nick for the connection
     */
    public abstract String getNick();

    /**
     * @return the password to connect with
     */
    public abstract String getPassword();

    /**
     * @param index the intex in the internal {@code ports} array
     * @return the port number at position {@code index} in the internal {@code ports} array
     */
    public abstract int getPortAt(int index);

    /**
     * @return a copy of internal {@code ports} array
     */
    public abstract int[] getPorts();

    /**
     * @return the number of ports in the internal {@code ports} array
     */
    public abstract int getPortsCount();

    /**
     * @return the {@link Proxy} to use when connecting
     */
    public abstract Proxy getProxy();

    /**
     * @return the real name of the user that is connecting
     */
    public abstract String getRealname();

    /**
     * @return the socket timeout in milliseconds
     */
    public abstract int getTimeout();

    /**
     * @return the {@link IRCTrafficLogger} that should be notified about incoming and outgoing messages
     */
    public abstract IRCTrafficLogger getTrafficLogger();

    /**
     * @return the username to connect with
     */
    public abstract String getUsername();

    /**
     * @return {@code true} if automatic PING? PONG! is enabled or {@code false} otherwise.
     */
    public abstract boolean isAutoPong();

    /**
     * @return {@code true} if mIRC colorcodes should be removed from incoming IRC messages
     */
    public abstract boolean isStripColorsEnabled();

}
