/*
 * IRClib -- A Java Internet Relay Chat library -- class SSLNotSupportedException
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

/**
 * Indicates that SSL is not supported. However, a 
 * <code>NoClassDefFoundError</code> is probably thrown before a
 * <code>SSLNotSupportedException</code> can be thrown, because the 
 * <code>javax.net.SocketFactory</code> will not be found (among others).
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.00
 * @see SSLIRCConnection
 */
public class SSLNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = -5108810948951810903L;
	
	/**
	 * Empty exception.
	 */
	public SSLNotSupportedException() {
		super();
	}
	
// ------------------------------

	/**
	 * Creates an exception with description.
	 * @param s The description.
	 */
	public SSLNotSupportedException(String s) {
		super(s);
	}
}
