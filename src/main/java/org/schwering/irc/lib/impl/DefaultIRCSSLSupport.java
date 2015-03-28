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

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.schwering.irc.lib.IRCSSLSupport;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCSSLSupport implements IRCSSLSupport {

    /**
     * @param sslSupport
     */
    public DefaultIRCSSLSupport(IRCSSLSupport sslSupport) {
    }

    /**
     * @see org.schwering.irc.lib.IRCSSLSupport#getKeyManagers()
     */
    @Override
    public KeyManager[] getKeyManagers() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.schwering.irc.lib.IRCSSLSupport#getTrustManagers()
     */
    @Override
    public TrustManager[] getTrustManagers() {
        // TODO Auto-generated method stub
        return null;
    }

}
