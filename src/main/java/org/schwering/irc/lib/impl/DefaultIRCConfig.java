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

import org.schwering.irc.lib.IRCConfig;
import org.schwering.irc.lib.IRCConfigBuilder;
import org.schwering.irc.lib.IRCExceptionHandler;
import org.schwering.irc.lib.IRCRuntimeConfig;
import org.schwering.irc.lib.IRCSSLSupport;
import org.schwering.irc.lib.IRCServerConfig;
import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * An immutable {@link IRCConfig}. Typically created via
 * {@link IRCConfigBuilder}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCConfig extends DefaultIRCServerConfig implements IRCConfig {

    /**
     * @see org.schwering.irc.lib.IRCConfig#isAutoPong()
     */
    private final boolean autoPong;
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
     * @see org.schwering.irc.lib.IRCRuntimeConfig#getExceptionHandler()
     */
    private final IRCExceptionHandler exceptionHandler;

    /**
     * Creates a new {@link DefaultIRCConfig} using data from the given
     * {@link IRCConfig}.
     *
     * @param config
     *            the {@link IRCConfig} to read field values from
     */
    public DefaultIRCConfig(IRCConfig config) {
        this(config.getHost(), config.getPorts(), config.getPassword(), config.getNick(), config.getUsername(), config
                .getRealname(), config.getEncoding(), config.getTimeout(), config.isAutoPong(), config
                .isStripColorsEnabled(), new DefaultIRCSSLSupport(config.getSSLSupport()), config.getProxy(), config
                .getTrafficLogger(), config.getExceptionHandler());
    }

    /**
     * Creates a new {@link DefaultIRCConfig} using data from the given
     * {@link IRCConfig}.
     *
     * @param serverConfig
     *            the {@link IRCServerConfig} to read field values from
     * @param runtimeConfig
     *            the {@link IRCRuntimeConfig} to read field values from
     */
    public DefaultIRCConfig(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
        this(serverConfig.getHost(), serverConfig.getPorts(), serverConfig.getPassword(), serverConfig.getNick(),
                serverConfig.getUsername(), serverConfig.getRealname(), serverConfig.getEncoding(), runtimeConfig
                        .getTimeout(), runtimeConfig.isAutoPong(), runtimeConfig.isStripColorsEnabled(),
                new DefaultIRCSSLSupport(runtimeConfig.getSSLSupport()), runtimeConfig.getProxy(), runtimeConfig
                        .getTrafficLogger(), runtimeConfig.getExceptionHandler());
    }

    /**
     * Creates a new {@link DefaultIRCConfig} out of the individual field
     * values. For meanings of the parameters, see the the respective getter
     * methods in {@link IRCConfig}.
     *
     * @param host
     * @param ports
     * @param pass
     * @param nick
     * @param username
     * @param realname
     * @param encoding
     * @param timeout
     * @param autoPong
     * @param stripColorsEnabled
     * @param sslSupport
     * @param proxy
     * @param trafficLogger
     * @param exceptionHandler
     */
    public DefaultIRCConfig(String host, int[] ports, String pass, String nick, String username, String realname,
            String encoding, int timeout, boolean autoPong, boolean stripColorsEnabled, IRCSSLSupport sslSupport,
            Proxy proxy, IRCTrafficLogger trafficLogger, IRCExceptionHandler exceptionHandler) {
        super(host, ports, pass, nick, username, realname, encoding);
        this.timeout = timeout;
        this.autoPong = autoPong;
        this.stripColorsEnabled = stripColorsEnabled;
        this.sslSupport = sslSupport;
        this.proxy = proxy;
        this.trafficLogger = trafficLogger;
        this.exceptionHandler = exceptionHandler;
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

    /**
     * @see org.schwering.irc.lib.IRCRuntimeConfig#getExceptionHandler()
     */
    public IRCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

}
