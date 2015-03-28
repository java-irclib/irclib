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
import org.schwering.irc.lib.IRCExceptionHandler;
import org.schwering.irc.lib.IRCRuntimeConfig;
import org.schwering.irc.lib.IRCSSLSupport;
import org.schwering.irc.lib.IRCServerConfig;
import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCConfig extends DefaultIRCServerConfig implements IRCConfig {

    /**
     * This <code>boolean</code> stands for enabled or disabled automatic PING?
     * PONG! support. It means, that if the server asks with PING for the ping,
     * the PONG is automatically sent. Default is automatic PONG enabled (
     * <code>true</code> ).
     */
    private final boolean autoPong;
    private final Proxy proxy;
    private final IRCSSLSupport sslSupport;

    /**
     * This <code>boolean</code> stands for enabled (<code>true</code>) or
     * disabled (<code>false</code>) ColorCodes. Default is enabled (
     * <code>false</code>).
     */
    private final boolean stripColorsEnabled;
    /**
     * This <code>int</code> is the connection's timeout in milliseconds. It's
     * used in the <code>Socket.setSoTimeout</code> method. The default is
     * <code>1000 * 60 * 15</code> millis which are 15 minutes.
     */
    private final int timeout;
    private final IRCTrafficLogger trafficLogger;
    private final IRCExceptionHandler exceptionHandler;

    /**
     * @param config
     */
    public DefaultIRCConfig(IRCConfig config) {
        this(config.getHost(), config.getPorts(), config.getPassword(), config.getNick(), config.getUsername(), config
                .getRealname(), config.getEncoding(), config.getTimeout(), config.isAutoPong(), config
                .isStripColorsEnabled(), new DefaultIRCSSLSupport(config.getSSLSupport()), config.getProxy(), config
                .getTrafficLogger(), config.getExceptionHandler());
    }

    public DefaultIRCConfig(IRCServerConfig config, IRCRuntimeConfig runtimeConfig) {
        this(config.getHost(), config.getPorts(), config.getPassword(), config.getNick(), config.getUsername(), config
                .getRealname(), config.getEncoding(), runtimeConfig.getTimeout(), runtimeConfig.isAutoPong(),
                runtimeConfig.isStripColorsEnabled(), new DefaultIRCSSLSupport(runtimeConfig.getSSLSupport()),
                runtimeConfig.getProxy(), runtimeConfig.getTrafficLogger(), runtimeConfig.getExceptionHandler());
    }

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

    public IRCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

}
