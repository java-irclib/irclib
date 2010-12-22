/*
 * IRClib -- A Java Internet Relay Chat library -- class SSLDefaultTrustManager
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

package org.schwering.irc.lib;

import com.sun.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * The default <code>TrustManager</code> of the 
 * <code>SSLIRCConnection</code>.
 * <p>
 * Note that this class is <b>deprecated</b>. The SSL supporting classes moved 
 * to <code>org.schwering.irc.lib.ssl</code> since IRClib 1.10.
 * <p>
 * It automatically accepts the X509 certificate.
 * <p>
 * In many cases you should change the <code>SSLIRCConnection</code>'s 
 * <code>TrustManager</code>. For examle if you write an IRC client for human
 * users, you may want to ask the user whether he accepts the server's 
 * certificate or not. You could do this by a new class which extends the
 * <code>SSLDefaultTrustManager</code> class and overrides the 
 * <code>checkServerTrusted</code> method and asks the user whether he wants to
 * accept the certification or not.
 * @deprecated This class has been replaced with 
 * <code>org.schwering.irc.lib.ssl.SSLDefaultTrustManager</code>.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.13
 * @see SSLIRCConnection
 * @see com.sun.net.ssl.TrustManager
 */
public class SSLDefaultTrustManager implements X509TrustManager {
	
	/**
	 * The <code>X509Certificate</code>s which are accepted.
	 */
	protected X509Certificate[] accepted = new X509Certificate[0];
	
// ------------------------------
	
	/**
	 * Creates a new instance of the <code>SSLDefaultTrustManager</code> class.
	 */
	public SSLDefaultTrustManager() {
		// nothing
	}
	
// ------------------------------
	
	/**
	 * Does nothing. This method would check whether we (the server) trust the 
	 * client. But we are the client and not the server. <br />
	 * It's final so that nobody can override it; it would make no sense.
	 * @param chain The peer certificate chain.
	 * @return Always <code>false</code>.
	 */
	public final boolean isClientTrusted(X509Certificate chain[]) {
		return false;
	}
	
// ------------------------------
	
	/**
	 * Invoked when the client should check whether he trusts the server or not.
	 * This method trusts the server. But this method can be overriden and then
	 * ask the user whether he truts the client or not.
	 * @param chain The peer certificate chain.
	 * @return Always <code>true</code>.
	 */
	public boolean isServerTrusted(X509Certificate chain[]) {
		accepted = chain;
		return true;
	}
	
// ------------------------------
	
	/**
	 * Returns the accepted certificates. They are set in the 
	 * <code>checkServerTrusted</code> method.
	 * @return A non-null (possibly empty) array of acceptable CA issuer 
	 *         certificates.
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return accepted;
	}
}
