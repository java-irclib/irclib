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
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCRuntimeConfig {

    /**
     * @return the {@link IRCExceptionHandler}
     */
    IRCExceptionHandler getExceptionHandler();

    /**
     * @return the {@link Proxy} to use when connecting
     */
    Proxy getProxy();

    /**
     * @return a {@link IRCSSLSupport} if the {@link IRCConnection} should use
     *         SSL, otherwise {@code null}
     */
    IRCSSLSupport getSSLSupport();

    /**
     * @return the socket timeout in milliseconds
     */
    int getTimeout();

    /**
     * @return the {@link IRCTrafficLogger} that should be notified about
     *         incoming and outgoing messages or {@code null} if no traffic
     *         logger should be attached to the {@link IRCConnection}.
     */
    IRCTrafficLogger getTrafficLogger();

    /**
     * @return {@code true} if automatic PING? PONG! is enabled or {@code false}
     *         otherwise.
     */
    boolean isAutoPong();

    /**
     * @return {@code true} if mIRC colorcodes should be removed from incoming
     *         IRC messages
     */
    boolean isStripColorsEnabled();

}
