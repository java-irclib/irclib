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

import java.util.Arrays;

import org.schwering.irc.lib.IRCServerConfig;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCServerConfig implements IRCServerConfig {

    /**
     * @param encoding
     * @param host
     * @param nick
     * @param pass
     * @param ports
     * @param realname
     * @param username
     */
    public DefaultIRCServerConfig(String host, int[] ports, String pass, String nick, String username, String realname,
            String encoding) {
        super();
        if (host == null || ports == null || ports.length == 0) {
            throw new IllegalArgumentException("Host and ports may not be null.");
        }
        this.host = host;
        this.ports = ports;
        this.pass = (pass != null && pass.length() == 0) ? null : pass;
        this.nick = nick;
        this.username = username;
        this.realname = realname;
        this.encoding = encoding;
    }

    /**
     * @param serverConfig
     */
    public DefaultIRCServerConfig(IRCServerConfig serverConfig) {
        this(serverConfig.getHost(), serverConfig.getPorts(), serverConfig.getPassword(), serverConfig.getNick(),
                serverConfig.getUsername(), serverConfig.getRealname(), serverConfig.getEncoding());
    }

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
    /**
     * The user's realname, which is indispensably to connect.
     */
    private final String realname;
    /**
     * The user's username, which is indispensable to connect.
     */
    private final String username;

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
     * @see org.schwering.irc.lib.IRCConfig#getRealname()
     */
    @Override
    public String getRealname() {
        return realname;
    }

    /**
     * @see org.schwering.irc.lib.IRCConfig#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

}
