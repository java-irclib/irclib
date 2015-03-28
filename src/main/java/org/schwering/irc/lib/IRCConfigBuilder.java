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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.schwering.irc.lib.impl.DefaultIRCConfig;
import org.schwering.irc.lib.util.IRCUtil;

/**
 * A fluent builder for {@link IRCConfig}s.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public final class IRCConfigBuilder {

    /** Default {@link #autoPong} is {@value IRCConfigBuilder#DEFAULT_AUTOPONG} */
    public static final boolean DEFAULT_AUTOPONG = true;

    /** Default {@link #encoding} is {@value IRCConfigBuilder#DEFAULT_ENCODING} */
    public static final String DEFAULT_ENCODING = "utf-8";

    /**
     * Default {@link #stripColors} is
     * {@value IRCConfigBuilder#DEFAULT_STRIP_COLORS}
     */
    public static final boolean DEFAULT_STRIP_COLORS = false;

    /**
     * Default {@link #timeout} is {@value IRCConfigBuilder#DEFAULT_TIMEOUT}
     * milliseconds which is 15 minutes
     */
    public static final int DEFAULT_TIMEOUT = 1000 * 60 * 15;

    /**
     * Creates a new {@link IRCConfigBuilder} initializing the following fields
     * with defaults:
     * <ul>
     * <li>{@link #autoPong(boolean)}</li>
     * <li>{@link #encoding(String)}</li>
     * <li>{@link #stripColors(boolean)}</li>
     * <li>{@link #timeout(int)}</li>
     * </ul>
     * Other fields are left {@code null}.
     *
     * @return new {@link IRCConfigBuilder}
     */
    public static IRCConfigBuilder newBuilder() {
        return new IRCConfigBuilder();
    }

    /** @see #autoPong(boolean) */
    private boolean autoPong = DEFAULT_AUTOPONG;

    /** @see #encoding(String) */
    private String encoding = DEFAULT_ENCODING;

    /** @see #exceptionHandler(IRCExceptionHandler) */
    private IRCExceptionHandler exceptionHandler;

    /** @see #host(String) */
    private String host;

    /** @see #nick(String) */
    private String nick;

    /** @see #password(String) */
    private String password;

    /**
     * Remote port numbers to try when connecting.
     *
     * @see #portRange(int, int)
     * @see #port(int)
     * @see #ports(int...)
     */
    private final List<Integer> ports = new ArrayList<Integer>();

    /** @see #socksProxy(String, int) */
    private Proxy proxy;

    /** @see #realname(String) */
    private String realname;

    /** @see #sslSupport(IRCSSLSupport) */
    private IRCSSLSupport sslSupport;

    /** @see #stripColors(boolean) */
    private boolean stripColors = DEFAULT_STRIP_COLORS;

    /** @see #timeout(int) */
    private int timeout = DEFAULT_TIMEOUT;

    /** @see #trafficLogger(IRCTrafficLogger) */
    private IRCTrafficLogger trafficLogger;

    /** @see #username(String) */
    private String username;

    /**
     * @see #newBuilder()
     */
    private IRCConfigBuilder() {
        super();
    }

    /**
     * Enables or disables the automatic PING? PONG! support. If not set through
     * this method, the default is {@value #DEFAULT_AUTOPONG}.
     *
     * @param autoPong
     *            <code>true</code> to enable automatic <code>PONG</code> reply,
     *            <code>false</code> makes the class fire <code>onPing</code>
     *            events.
     * @return this builder
     */
    public IRCConfigBuilder autoPong(boolean autoPong) {
        this.autoPong = autoPong;
        return this;
    }

    /**
     * @return a new {@link DefaultIRCConfig} instance based on the values
     *         stored in fields of this {@link IRCConfigBuilder}.
     */
    public IRCConfig build() {
        return new DefaultIRCConfig(host, IRCUtil.toArray(ports), password, nick, username, realname, encoding,
                timeout, autoPong, stripColors, sslSupport, proxy, trafficLogger, exceptionHandler);
    }

    /**
     * Copies all available fields from the given {@code config} to this
     * {@link IRCConfigBuilder}.
     *
     * @param config
     *            the {@link IRCConfig} to take the values from
     * @return this builder
     */
    public IRCConfigBuilder config(IRCConfig config) {
        serverConfig(config);
        runtimeConfig(config);
        return this;
    }

    /**
     * Changes the character encoding (such as {@code "UTF-8"} or
     * {@code "ISO-8859-1"}) used to talk to the server. If not set through this
     * method, the default is {@value #DEFAULT_ENCODING}.
     *
     * @param encoding
     *            The new encoding string, e.g. <code>"UTF-8"</code>
     * @return this builder
     */
    public IRCConfigBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Sets the {@link IRCExceptionHandler} that should be notified by
     * {@link IRCConnection} when an exception during send or receive of IRC
     * messages occurs.
     *
     * @param exceptionHandler
     * @return this builder
     */
    public IRCConfigBuilder exceptionHandler(IRCExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Sets the hostname or IP address of the IRC server to connect to.
     *
     * @param host
     * @return this builder
     */
    public IRCConfigBuilder host(String host) {
        this.host = host;
        return this;
    }

    /**
     * Sets the nick name preferred by the user who is connecting.
     *
     * @param nick
     *            the nick name
     * @return this builder
     */
    public IRCConfigBuilder nick(String nick) {
        this.nick = nick;
        return this;
    }

    /**
     * Sets the password of the user who is connecting.
     *
     * @param password
     * @return this builder
     */
    public IRCConfigBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Adds the given {@code port} to the internal list of ports.
     *
     * @param port
     *            the port or the {@link #host(String)} to connect to
     * @return this builder
     */
    public IRCConfigBuilder port(int port) {
        this.ports.add(port);
        return this;
    }

    /**
     * Adds the port numbers from the given range to the internal list of ports.
     * {@code portMin} is the first (lowest) port to add whereas {@code portMax}
     * is the last port to add.
     *
     * @param portMin
     *            The beginning port of the port range.
     * @param portMax
     *            The ending port of the port range.
     * @return this builder
     */
    public IRCConfigBuilder portRange(int portMin, int portMax) {
        if (portMin > portMax) {
            int tmp = portMin;
            portMin = portMax;
            portMax = tmp;
        }
        for (int port = portMin; port <= portMax; port++) {
            ports.add(port);
        }
        return this;
    }

    /**
     * Adds the given port numbers to the internal list of ports.
     *
     * @param port
     *            the port numbers to add
     * @return this builder
     */
    public IRCConfigBuilder ports(int... port) {
        for (int p : port) {
            ports.add(p);
        }
        return this;
    }

    /**
     * Sets the real name (e.g. {@code"John Doe"}) of the user who is
     * connecting.
     *
     * @param realname
     *            the real name
     * @return this builder
     */
    public IRCConfigBuilder realname(String realname) {
        this.realname = realname;
        return this;
    }

    /**
     * Copies all available fields from the given {@code runtimeConfig} to this
     * {@link IRCConfigBuilder}.
     *
     * @param runtimeConfig
     *            the {@link IRCRuntimeConfig} to take the values from
     * @return this builder
     */
    public IRCConfigBuilder runtimeConfig(IRCRuntimeConfig runtimeConfig) {
        this.timeout = runtimeConfig.getTimeout();
        this.autoPong = runtimeConfig.isAutoPong();
        this.stripColors = runtimeConfig.isStripColorsEnabled();
        this.sslSupport = runtimeConfig.getSSLSupport();
        this.proxy = runtimeConfig.getProxy();
        this.trafficLogger = runtimeConfig.getTrafficLogger();
        this.exceptionHandler = runtimeConfig.getExceptionHandler();
        return this;
    }

    /**
     * Copies all available fields from the given {@code serverConfig} to this
     * {@link IRCConfigBuilder}.
     *
     * @param serverConfig
     *            the {@link IRCServerConfig} to take the values from
     * @return this builder
     */
    public IRCConfigBuilder serverConfig(IRCServerConfig serverConfig) {
        this.host = serverConfig.getHost();
        ports(serverConfig.getPorts());
        this.password = serverConfig.getPassword();
        this.nick = serverConfig.getNick();
        this.username = serverConfig.getUsername();
        this.realname = serverConfig.getRealname();
        this.encoding = serverConfig.getEncoding();
        return this;
    }

    /**
     * Instructs the connection to use a SOCKS proxy with given {@code host} and
     * {@code port}.
     *
     * @param socksProxyHost
     *            the hostname or IP address of the SOCKS proxy
     * @param socksProxyPort
     *            the port of the SOCKS proxy
     * @return this builder
     */
    public IRCConfigBuilder socksProxy(String socksProxyHost, int socksProxyPort) {
        if (socksProxyHost == null) {
            throw new IllegalArgumentException("socksProxyHost must be non-null, non-empty");
        }
        proxy = new Proxy(Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPort));
        return this;
    }

    /**
     * Sets the {@link IRCSSLSupport} containing the information the
     * {@link IRCConnection} should use to connect using SSL.
     *
     * @param sslSupport
     *            the username of the user connecting to the IRC server
     * @return this builder
     */
    public IRCConfigBuilder sslSupport(IRCSSLSupport sslSupport) {
        this.sslSupport = sslSupport;
        return this;
    }

    /**
     * Enables or disables the stripping of mIRC color codes. If not set through
     * this method, the default is {@value #DEFAULT_STRIP_COLORS}.
     *
     * @param stripColors
     *            <code>true</code> to enable, <code>false</code> to disable
     *            colors
     * @return this builder
     */
    public IRCConfigBuilder stripColors(boolean stripColors) {
        this.stripColors = stripColors;
        return this;
    }

    /**
     * Sets the preferred connection's timeout in milliseconds. If not set
     * through this method, the default is {@value #DEFAULT_TIMEOUT}.
     *
     * @param millis
     *            The socket's timeout in milliseconds.
     * @return this builder
     */
    public IRCConfigBuilder timeout(int millis) {
        this.timeout = millis;
        return this;
    }

    /**
     * Sets the {@link IRCTrafficLogger} that should be notified by
     * {@link IRCConnection} about incoming and outgoing messages.
     *
     * @param trafficLogger
     *            the {@link IRCTrafficLogger} the connection should notify
     * @return this builder
     */
    public IRCConfigBuilder trafficLogger(IRCTrafficLogger trafficLogger) {
        this.trafficLogger = trafficLogger;
        return this;
    }

    /**
     * Sets the username of the user connecting to the IRC server.
     *
     * @param username
     *            the username of the user connecting to the IRC server
     * @return this builder
     */
    public IRCConfigBuilder username(String username) {
        this.username = username;
        return this;
    }
}
