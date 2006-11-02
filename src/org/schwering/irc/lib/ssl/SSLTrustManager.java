/*
 * IRClib -- A Java Internet Relay Chat library -- class SSLTrustManager
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

import java.security.cert.X509Certificate;

/**
 * A trust manager decides whether the server is trusted or not.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.00
 * @see SSLIRCConnection
 * @see SSLDefaultTrustManager
 */
public interface SSLTrustManager {
	/**
	 * Checks whether the server is trusted or not. <br />
	 * Given the partial or complete certificate chain provided by the peer,
	 * build a certificate path to a trusted root and return true if it can 
	 * be validated and is trusted for server SSL authentication.
	 * @param chain The peer certificate chain.
	 * @return <code>true</code> if the server is trusted, <code>false</code>
	 * if the server is not trusted.
	 */
	public boolean isTrusted(X509Certificate[] chain);
	
// ------------------------------
	
	/**
	 * Return an array of certificate authority certificates which are trusted 
	 * for authenticating peers.
	 * @return A non-null (possibly empty) array of acceptable CA issuer 
	 * certificates.
	 */
	public X509Certificate[] getAcceptedIssuers();
}
