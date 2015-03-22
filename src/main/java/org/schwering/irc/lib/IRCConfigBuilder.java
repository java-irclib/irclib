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
import java.util.Arrays;

import org.schwering.irc.lib.impl.DefaultIRCConfig;

/**
 * A fluent builder for {@link IRCConfig}s.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class IRCConfigBuilder {
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

    /** @see #autoPong(boolean) */
    private boolean autoPong = DEFAULT_AUTOPONG;

    /** @see #encoding(String) */
    private String encoding = DEFAULT_ENCODING;

    /** @see #host(String) */
    private String host;

    /** @see #nick(String) */
    private String nick;

    /** @see #password(String) */
    private String pass;

    /**
     * An array of remote ports to try when connecting.
     *
     * @see #portRange(int, int)
     */
    private int[] ports;

    /** @see #socksProxy(String, int) */
    private Proxy proxy;

    /** @see #realname(String) */
    private String realname;

    /** @see #stripColors(boolean) */
    private boolean stripColors = DEFAULT_STRIP_COLORS;

    /** @see #timeout(int) */
    private int timeout = DEFAULT_TIMEOUT;

    private IRCTrafficLogger trafficLogger;
    /**
     * The user's username, which is indispensable to connect.
     */
    private String username;

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
        return new DefaultIRCConfig(host, ports == null ? new int[0] : Arrays.copyOf(ports, ports.length), pass, nick,
                username, realname, timeout, encoding, autoPong, stripColors, proxy, trafficLogger);
    }

    /**
     * Changes the character encoding used to talk to the server. This can be
     * ISO-8859-1 or UTF-8 for example. If not set through this method, the
     * default is {@value #DEFAULT_ENCODING}.
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
     * Sets the nick name prefered by the user who is connecting.
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
        this.pass = password;
        return this;
    }

    /**
     * Populates the internal ports array with port numbers starting with the
     * given {@code portMin} and ending with (inlc.) the given {@code portMax}.
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
        int[] ports = new int[portMax - portMin + 1];
        for (int i = 0; i < ports.length; i++) {
            ports[i] = portMin + i;
        }
        this.ports = ports;
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
        timeout = millis;
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
}
