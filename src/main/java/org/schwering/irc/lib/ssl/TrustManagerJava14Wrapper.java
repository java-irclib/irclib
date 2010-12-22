/*
 * IRClib -- A Java Internet Relay Chat library -- class TrustManagerJava14Wrapper
 * Copyright (C) 2002 - 2006 Christoph Schwering <schwering@gmail.com>
 * 
 * This library and the accompanying materials are made available under the
 * terms of the
 * 	- GNU Lesser General Public License,
 * 	- Apache License, Version 2.0 and
 * 	- Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */

package org.schwering.irc.lib.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Wraps a <code>SSLTrustManager</code> in a 
 * <code>javax.net.ssl.X509TrustManager</code>.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.00
 * @see SSLTrustManager
 * @see javax.net.ssl.X509TrustManager
 */
class TrustManagerJava14Wrapper implements javax.net.ssl.X509TrustManager {
	/**
	 * The trust manager that is wrapped.
	 */
	private SSLTrustManager trustManager;
	
// ------------------------------

	public static TrustManagerJava14Wrapper[] wrap(SSLTrustManager[] tm) {
		TrustManagerJava14Wrapper[] w = new TrustManagerJava14Wrapper[tm.length];
		for (int i = 0; i < tm.length; i++) {
			w[i] = new TrustManagerJava14Wrapper(tm[i]);
		}
		return w;
	}
	
// ------------------------------
	
	/**
	 * Creates a new trust manager wrapper.
	 * @param trustManager The <code>SSLTrustManager</code> that should be 
	 * wrapped by a <code>javax.net.ssl.X509TrustManager</code>.
	 */
	public TrustManagerJava14Wrapper(SSLTrustManager trustManager) {
		if (trustManager == null) {
			throw new IllegalArgumentException("trustManager == null");
		}
		this.trustManager = trustManager;
	}

// ------------------------------

	/**
	 * Always throws a <code>CertificateException</code>.
	 * @param chain The peer certificate chain.
	 * @param authType The authentication type based on the client certificate.
	 * @throws CertificateException Always.
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType) 
	throws CertificateException {
		throw new CertificateException("This trust manager _is_ for clients. "+
				"What other client should be trusted?");
	}

// ------------------------------

	/**
	 * Does nothing if the server is trusted, throws a 
	 * <code>CertificateException</code> otherwise. This decision is made by 
	 * the <code>trustManager</code>.
	 * @throws CertificateException If the server is not trusted.
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType) 
	throws CertificateException {
		if (!trustManager.isTrusted(chain)) {
			throw new CertificateException("The certificate chain is not "+
					"trusted!");
		}
	}

// ------------------------------

	/**
	 * Returns <code>true</code> if the server is trusted. This decision is 
	 * made by the <code>trustManager</code>.
	 * @return <code>trustManager.isTrusted(chain)</code>.
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return trustManager.getAcceptedIssuers();
	}
}
