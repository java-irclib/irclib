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

import org.schwering.irc.lib.impl.DefaultIRCConnection;

/**
 * A factory to create new {@link IRCConnection}s. For the typical usage, see {@link IRCConnection}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class IRCConnectionFactory {
    public static IRCConnection newConnection(IRCConfig config) {
        return new DefaultIRCConnection(config, config);
    }
    public static IRCConnection newConnection(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
        return new DefaultIRCConnection(serverConfig, runtimeConfig);
    }
}
