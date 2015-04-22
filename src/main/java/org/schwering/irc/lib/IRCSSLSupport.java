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

import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * A bunch of things necessary to connect using SSL.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCSSLSupport {

    /**
     * @return the {@link KeyManager}s to initialize {@link SSLContext} with.
     */
    KeyManager[] getKeyManagers();

    /**
     * @return the {@link TrustManager}s to initialize {@link SSLContext} with.
     */
    TrustManager[] getTrustManagers();

    /**
     * @return the {@link SecureRandom} to initialize {@link SSLContext} with.
     */
    SecureRandom getSecureRandom();

}
