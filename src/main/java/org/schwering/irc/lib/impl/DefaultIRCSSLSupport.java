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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.schwering.irc.lib.IRCSSLSupport;

/**
 * A default {@link IRCSSLSupport} with configurable {@link KeyManager}s,
 * {@link TrustManager}s and {@link SecureRandom}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultIRCSSLSupport implements IRCSSLSupport {

    protected static final X509Certificate[] EMPTY_X509_CERTIFICATES = new X509Certificate[0];

    public static IRCSSLSupport INSECURE;
    public static final X509TrustManager INSECURE_TRUST_MANAGER;

    static {
        INSECURE_TRUST_MANAGER = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return EMPTY_X509_CERTIFICATES;
            }
        };
        try {
            INSECURE = new DefaultIRCSSLSupport(new KeyManager[0], new TrustManager[] { INSECURE_TRUST_MANAGER },
                    SecureRandom.getInstanceStrong());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private final KeyManager[] keyManagers;
    private final SecureRandom secureRandom;

    private final TrustManager[] trustManagers;

    public DefaultIRCSSLSupport(IRCSSLSupport sslSupport) {
        this(sslSupport.getKeyManagers(), sslSupport.getTrustManagers(), sslSupport.getSecureRandom());
    }

    /**
     * @param keyManagers
     * @param trustManagers
     * @param secureRandom
     */
    public DefaultIRCSSLSupport(KeyManager[] keyManagers, TrustManager[] trustManagers, SecureRandom secureRandom) {
        super();
        this.keyManagers = Arrays.copyOf(keyManagers, keyManagers.length);
        this.trustManagers = Arrays.copyOf(trustManagers, trustManagers.length);
        this.secureRandom = secureRandom;
    }

    /**
     * @see org.schwering.irc.lib.IRCSSLSupport#getKeyManagers()
     */
    @Override
    public KeyManager[] getKeyManagers() {
        return Arrays.copyOf(this.keyManagers, this.keyManagers.length);
    }

    /**
     * @see org.schwering.irc.lib.IRCSSLSupport#getSecureRandom()
     */
    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    /**
     * @see org.schwering.irc.lib.IRCSSLSupport#getTrustManagers()
     */
    @Override
    public TrustManager[] getTrustManagers() {
        return Arrays.copyOf(this.trustManagers, this.trustManagers.length);
    }

}
