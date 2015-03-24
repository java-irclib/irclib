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
import java.util.Arrays;

import org.schwering.irc.lib.IRCConfig;
import org.schwering.irc.lib.IRCSSLSupport;
import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCConfig implements IRCConfig {

    /**
     * This <code>boolean</code> stands for enabled or disabled automatic PING?
     * PONG! support. It means, that if the server asks with PING for the ping,
     * the PONG is automatically sent. Default is automatic PONG enabled (
     * <code>true</code> ).
     */
    private final boolean autoPong;
    /**
     * The <code>String</code> contains the name of the character encoding used
     * to talk to the server. This can be ISO-8859-1 or UTF-8 for example. The
     * default is UTF-8.
     */
    private final String encoding;
    /**
     * The host of the IRC server.
     */
    private final String host;
    /**
     * The user's nickname, which is indispensably to connect.
     */
    private final String nick;
    /**
     * The password, which is needed to get access to the IRC server.
     */
    private final String pass;
    /**
     * The <code>int[]</code> contains all ports to which we are going to try to
     * connect. This can be a portrange from port 6667 to 6669, for example.
     */
    private final int[] ports;
    private final Proxy proxy;
    /**
     * The user's realname, which is indispensably to connect.
     */
    private final String realname;

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
    /**
     * The user's username, which is indispensable to connect.
     */
    private final String username;

    /**
     * @param config
     */
    public DefaultIRCConfig(IRCConfig config) {
        this(config.getHost(), config.getPorts(), config.getPassword(), config.getNick(), config.getUsername(),
                config.getRealname(), config.getTimeout(), config.getEncoding(), config.isAutoPong(),
                config.isStripColorsEnabled(), new DefaultIRCSSLSupport(config.getSSLSupport()), config.getProxy(),
                config.getTrafficLogger());
    }

    public DefaultIRCConfig(String host, int[] ports, String pass, String nick, String username, String realname,
            int timeout, String encoding, boolean autoPong, boolean stripColorsEnabled, IRCSSLSupport sslSupport,
            Proxy proxy, IRCTrafficLogger trafficLogger) {
        if (host == null || ports == null || ports.length == 0)
            throw new IllegalArgumentException("Host and ports may not be null.");
        this.host = host;
        this.ports = ports;
        this.pass = (pass != null && pass.length() == 0) ? null : pass;
        this.nick = nick;
        this.username = username;
        this.realname = realname;
        this.timeout = timeout;
        this.encoding = encoding;
        this.autoPong = autoPong;
        this.stripColorsEnabled = stripColorsEnabled;
        this.sslSupport = sslSupport;
        this.proxy = proxy;
        this.trafficLogger = trafficLogger;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getEncoding()
     */
    @Override
    public String getEncoding() {
        return encoding;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getHost()
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getNick()
     */
    @Override
    public String getNick() {
        return nick;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getPassword()
     */
    @Override
    public String getPassword() {
        return pass;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getPortAt(int)
     */
    @Override
    public int getPortAt(int index) {
        return ports[index];
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getPorts()
     */
    @Override
    public int[] getPorts() {
        return ports == null ? new int[0] : Arrays.copyOf(ports, ports.length);
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getPortsCount()
     */
    @Override
    public int getPortsCount() {
        return ports != null ? ports.length : 0;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getProxy()
     */
    @Override
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getRealname()
     */
    @Override
    public String getRealname() {
        return realname;
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
     * @see org.schwering.irc.lib.IRCConfig#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
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
