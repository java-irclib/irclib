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
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public interface IRCConfig {

    public abstract boolean getColors();

    public abstract String getEncoding();

    public abstract String getHost();

    public abstract String getNick();

    public abstract String getPassword();

    public abstract boolean getPong();

    public abstract int getPortAt(int index);

    public abstract int[] getPorts();

    public abstract int getPortsCount();

    public abstract Proxy getProxy();

    public abstract String getRealname();

    public abstract int getTimeout();

    public abstract IRCTrafficLogger getTrafficLogger();

    public abstract String getUsername();

    public abstract boolean isAutoPong();

}
