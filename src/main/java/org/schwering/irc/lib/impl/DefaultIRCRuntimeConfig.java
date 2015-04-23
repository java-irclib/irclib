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

import java.net.Proxy;

import org.schwering.irc.lib.IRCConfigBuilder;
import org.schwering.irc.lib.IRCExceptionHandler;
import org.schwering.irc.lib.IRCRuntimeConfig;
import org.schwering.irc.lib.IRCSSLSupport;
import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * An immutable {@link IRCRuntimeConfig}. Typically created via
 * {@link IRCConfigBuilder}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCRuntimeConfig implements IRCRuntimeConfig {
    /**
     * @see org.schwering.irc.lib.IRCConfig#isAutoPong()
     */
    private final boolean autoPong;
    /**
     * @see org.schwering.irc.lib.IRCRuntimeConfig#getExceptionHandler()
     */
    private final IRCExceptionHandler exceptionHandler;
    /**
     * @see org.schwering.irc.lib.IRCConfig#getProxy()
     */
    private final Proxy proxy;
    /**
     * @see org.schwering.irc.lib.IRCConfig#getSSLSupport()
     */
    private final IRCSSLSupport sslSupport;
    /**
     * @see org.schwering.irc.lib.IRCConfig#isStripColorsEnabled()
     */
    private final boolean stripColorsEnabled;
    /**
     * @see org.schwering.irc.lib.IRCConfig#getTimeout()
     */
    private final int timeout;
    /**
     * @see org.schwering.irc.lib.IRCConfig#getTrafficLogger()
     */
    private final IRCTrafficLogger trafficLogger;

    /**
     * Creates a new {@link DefaultIRCRuntimeConfig} out of the individual field
     * values. For meanings of the parameters, see the the respective getter
     * methods in {@link IRCRuntimeConfig}.
     *
     * @param timeout the timeout
     * @param autoPong {@code PONG} will be sent authomatically if {@code true}
     * @param stripColorsEnabled if {@code true} color codes will be stripped
     * @param sslSupport the {@link IRCSSLSupport}
     * @param proxy the {@link Proxy}
     * @param trafficLogger the {@code IRCTrafficLogger}
     * @param exceptionHandler the {@link IRCExceptionHandler}
     */
    public DefaultIRCRuntimeConfig(int timeout, boolean autoPong, boolean stripColorsEnabled, IRCSSLSupport sslSupport,
            Proxy proxy, IRCTrafficLogger trafficLogger, IRCExceptionHandler exceptionHandler) {
        this.timeout = timeout;
        this.autoPong = autoPong;
        this.stripColorsEnabled = stripColorsEnabled;
        this.sslSupport = sslSupport;
        this.proxy = proxy;
        this.trafficLogger = trafficLogger;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Creates a new {@link DefaultIRCRuntimeConfig} using data from the given
     * {@link IRCRuntimeConfig}.
     *
     * @param runtimeConfig
     *            the {@link IRCRuntimeConfig} to read field values from
     */
    public DefaultIRCRuntimeConfig(IRCRuntimeConfig runtimeConfig) {
        this(runtimeConfig.getTimeout(), runtimeConfig.isAutoPong(), runtimeConfig.isStripColorsEnabled(),
                runtimeConfig.getSSLSupport(), runtimeConfig.getProxy(), runtimeConfig.getTrafficLogger(),
                runtimeConfig.getExceptionHandler());
    }

    /**
     * @see org.schwering.irc.lib.IRCRuntimeConfig#getExceptionHandler()
     */
    public IRCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getProxy()
     */
    @Override
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getSSLSupport()
     */
    @Override
    public IRCSSLSupport getSSLSupport() {
        return sslSupport;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getTimeout()
     */
    @Override
    public int getTimeout() {
        return timeout;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getTrafficLogger()
     */
    @Override
    public IRCTrafficLogger getTrafficLogger() {
        return trafficLogger;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#isAutoPong()
     */
    @Override
    public boolean isAutoPong() {
        return autoPong;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#isStripColorsEnabled()
     */
    @Override
    public boolean isStripColorsEnabled() {
        return stripColorsEnabled;
    }

}
