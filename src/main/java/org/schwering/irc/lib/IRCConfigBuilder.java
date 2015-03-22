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
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public class IRCConfigBuilder {
    public static final boolean DEFAULT_AUTOPONG = true;
    public static final boolean DEFAULT_COLORS_ENABLED = false;
    public static final String DEFAULT_ENCODING = "utf-8";
    public static final int DEFAULT_TIMEOUT = 1000 * 60 * 15;

    /**
     * This <code>boolean</code> stands for enabled or disabled automatic PING?
     * PONG! support. It means, that if the server asks with PING for the ping,
     * the PONG is automatically sent. Default is automatic PONG enabled (
     * <code>true</code> ).
     */
    private boolean autoPong = DEFAULT_AUTOPONG;

    /**
     * This <code>boolean</code> stands for enabled (<code>true</code>) or
     * disabled (<code>false</code>) ColorCodes. Default is enabled (
     * <code>false</code>).
     */
    private boolean colorsEnabled = DEFAULT_COLORS_ENABLED;
    /**
     * The <code>String</code> contains the name of the character encoding used
     * to talk to the server. This can be ISO-8859-1 or UTF-8 for example. The
     * default is UTF-8.
     */
    private String encoding = DEFAULT_ENCODING;
    /**
     * The host of the IRC server.
     */
    private String host;
    /**
     * The user's nickname, which is indispensably to connect.
     */
    private String nick;
    /**
     * The password, which is needed to get access to the IRC server.
     */
    private String pass;
    /**
     * The <code>int[]</code> contains all ports to which we are going to try to
     * connect. This can be a portrange from port 6667 to 6669, for example.
     */
    private int[] ports;
    private Proxy proxy;
    /**
     * The user's realname, which is indispensably to connect.
     */
    private String realname;
    /**
     * This <code>int</code> is the connection's timeout in milliseconds. It's
     * used in the <code>Socket.setSoTimeout</code> method. The default is
     * <code>1000 * 60 * 15</code> millis which are 15 minutes.
     */
    private int timeout = DEFAULT_TIMEOUT;
    private IRCTrafficLogger trafficLogger;
    /**
     * The user's username, which is indispensable to connect.
     */
    private String username;

    /**
     * Enables or disables the automatic PING? PONG! support.
     *
     * @param pong
     *            <code>true</code> to enable automatic <code>PONG</code> reply,
     *            <code>false</code> makes the class fire <code>onPing</code>
     *            events.
     */
    public IRCConfigBuilder autoPong(boolean pong) {
        this.autoPong = pong;
        return this;
    }

    public IRCConfig build() {
        return new DefaultIRCConfig(host, ports == null ? new int[0] : Arrays.copyOf(ports, ports.length), pass, nick,
                username, realname, timeout, encoding, autoPong, colorsEnabled, proxy, trafficLogger);
    }

    // ------------------------------

    /**
     * Enables or disables the mIRC colorcodes.
     *
     * @param colors
     *            <code>true</code> to enable, <code>false</code> to disable
     *            colors.
     */
    public IRCConfigBuilder colorsEnabled(boolean colors) {
        colorsEnabled = colors;
        return this;
    }

    // ------------------------------

    /**
     * Changes the character encoding used to talk to the server. This can be
     * ISO-8859-1 or UTF-8 for example. This property must be set before a call
     * to the <code>connect()</code> method.
     *
     * @param encoding
     *            The new encoding string, e.g. <code>"UTF-8"</code>.
     */
    public IRCConfigBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    // ------------------------------

    /**
     * Converts a portrange which starts with a given <code>int</code> and ends
     * with a given <code>int</code> into an array which contains all
     * <code>int</code>s from the beginning to the ending (including beginning
     * and ending). If <code>portMin > portMax</code>, the portrange is turned
     * arount automatically.
     *
     * @param portMin
     *            The beginning port of the portrange.
     * @param portMax
     *            The ending port of the portrange.
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

    public IRCConfigBuilder socksProxy(String socksProxyHost, int socksProxyPort) {
        if (socksProxyHost == null) {
            throw new IllegalArgumentException("socksProxyHost must be non-null, non-empty");
        }
        proxy = new Proxy(Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPort));
        return this;
    }

    /**
     * Sets the connection's timeout in milliseconds. The default is
     * <code>1000 * 60 15</code> millis which are 15 minutes. The possibly
     * occuring <code>IOException</code> are handled according to the set
     * exception handling.
     *
     * @param millis
     *            The socket's timeout in milliseconds.
     */
    public IRCConfigBuilder timeout(int millis) {
        timeout = millis;
        return this;
    }
}
