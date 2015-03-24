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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.schwering.irc.lib.IRCSSLSupport;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class SocketFactory {

    private final Proxy proxy;
    private SSLSocketFactory sslSocketFactory;
    private final int timeout;

    /**
     * @param timeout
     * @param proxy
     * @param sslSupport
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public SocketFactory(int timeout, Proxy proxy, IRCSSLSupport sslSupport) throws KeyManagementException,
            NoSuchAlgorithmException {
        super();
        this.timeout = timeout;
        this.proxy = proxy == null ? Proxy.NO_PROXY : proxy;
        if (sslSupport != null) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(sslSupport.getKeyManagers(), sslSupport.getTrustManagers(),
                    new java.security.SecureRandom());
            this.sslSocketFactory = sslContext.getSocketFactory();
        } else {
            this.sslSocketFactory = null;
        }
    }

    @SuppressWarnings("resource")
    public Socket createSocket(String host, int port) throws IOException {
        final Socket result;
        if (sslSocketFactory == null) {
            /* plain, optionally with proxy */
            result = new Socket(proxy);
            result.connect(new InetSocketAddress(host, port), timeout);
        } else if (proxy == Proxy.NO_PROXY) {
            /* SSL without proxy */
            SSLSocket sslResult = (SSLSocket) sslSocketFactory.createSocket(host, port);
            sslResult.startHandshake();
            result = sslResult;
        } else {
            /* SSL with proxy */
            Socket proxySocket = new Socket(proxy);
            SSLSocket sslResult = (SSLSocket) sslSocketFactory.createSocket(proxySocket, host, port, true);
            sslResult.startHandshake();
            result = sslResult;
        }
        result.setSoTimeout(timeout);
        return result;
    }

}
