/*
 * IRClib -- A Java Internet Relay Chat library -- class SSLIRCConnection
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

import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.internal.ssl.Provider;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.security.Security;
import java.util.Vector;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * The SSL extension of the <code>IRCConnection</code> class.
 * <p>
 * Note that this class is <b>deprecated</b>. The SSL supporting classes moved 
 * to <code>org.schwering.irc.lib.ssl</code> since IRClib 1.10.
 * <p>
 * The IRC server you want to connect to must accept SSL connections. 
 * Otherwise you cannot connect to it with an instance of 
 * <code>SSLIRCConnection</code>. IRC servers which accept SSL connections are 
 * really very rare, because SSL means a high load for the server.
 * <p>
 * The following sample code tries to establish an IRC connection to an
 * IRC server which must support SSL. Differences to the code which 
 * demonstrates the use of of the {@link org.schwering.irc.lib.IRCConnection} 
 * class are printed in bold font:
 * <p>
 * <hr /><pre>
 * /&#42; 
 *  &#42; The following code of a class which imports org.schwering.irc.lib.*
 *  &#42; prepares an SSL IRC connection and then tries to establish the 
 *  &#42; connection. The server is "irc.somenetwork.com", the ports are 
 *  &#42; the default SSL port (443) and the port used on most SSL IRC servers
 *  &#42; (994). No password is used (null). The nickname is "Foo" and 
 *  &#42; the realname is "Mr. Foobar". The username "foobar".
 *  &#42; Because of setDaemon(true), the JVM exits even if this thread is 
 *  &#42; running.
 *  &#42; By setting an instance of SSLDefaultTrustManager as TrustManager
 *  &#42; (which is also done implicitely by the SSLIRCConnection class if no
 *  &#42; TrustManager is set until the connect method is invoked), the
 *  &#42; X509Certificate is accepted automatically. Of course, you can write
 *  &#42; your own TrustManager. For example, you could write a class which
 *  &#42; extends SSLDefaultTrustManager and overrides its checkServerTrusted
 *  &#42; method. In the new checkServerTrusted method, you could ask the user
 *  &#42; to accept or reject the certificate.
 *  &#42; An instance of the class MyListener which must implement 
 *  &#42; IRCActionListener is set as event-listener for the connection. 
 *  &#42; The connection is told to parse out mIRC color codes and to enable
 *  &#42; automatic PING? PONG! replies.
 *  &#42;/
 * <b>SSL</b>IRCConnection conn = new <b>SSL</b>IRCConnection(
 *                               "irc.somenetwork.com", 
 *                               new int[] { 443, 994 },  
 *                               null, 
 *                               "Foo", 
 *                               "Mr. Foobar", 
 *                               "foo@bar.com" 
 *                             ); 
 * 
 * conn.addIRCEventListener(new MyListener()); 
 * <b>conn.addTrustManager(new SSLDefaultTrustManager());</b>
 * conn.setDaemon(true);
 * conn.setColors(false); 
 * conn.setPong(true); 
 *   
 * try {
 *   conn.connect(); // Try to connect!!! Don't forget this!!!
 * } catch (IOException ioexc) {
 *   ioexc.printStackTrace(); 
 * }
 * </pre><hr />
 * <p>
 * The serverpassword isn't needed in most cases. You can give 
 * <code>null</code> or <code>""</code> instead as done in this example.
 * <p>
 * <code>TrustManager</code>s can be added and removed until the 
 * <code>connect</code> method is invoked. If no <code>TrustManager</code>s are
 * set until then, an {@link org.schwering.irc.lib.SSLDefaultTrustManager} is 
 * set automatically. It accepts all X509 certificates.
 * @deprecated This class has been replaced with 
 * <code>org.schwering.irc.lib.ssl.SSLDefaultTrustManager</code>.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.33
 * @see IRCConnection
 * @see SSLDefaultTrustManager
 * @see com.sun.net.ssl.TrustManager
 */
public class SSLIRCConnection extends IRCConnection {
	
	/**
	 * The list of <code>TrustManager</code>s. <code>TrustManager</code>s can be
	 * added with the <code>addTrustManager</code> method and removed with the
	 * <code>removeTrustManager</code> method. If no <code>TrustManager</code>
	 * is included, the <code>getTrustManagers</code> method sets an instance of
	 * <code>SSLDefaultTrustManager</code> as only <code>TrustManager</code>.
	 */
	private Vector trustManagers = new Vector(1);
	
// ------------------------------
	
	/**
	 * Creates a new IRC connection with secure sockets (SSL).<br />
	 * The difference to the other constructor is, that it transmits the ports in
	 * an <code>int[]</code>. Thus, also ports like 994, 6000 and 6697 can be 
	 * selected.<br /><br />
	 * The constructor prepares a new IRC connection with secure sockets which 
	 * can be really started by invoking the <code>connect</code> method. Before 
	 * invoking it, you should set the <code>IRCEventListener</code>, optionally
	 * the <code>TrustManager</code>, if you don't want to use the 
	 * <code>SSLDefaultTrustManager</code> which accepts the X509 certificate 
	 * automatically, and other settings.<br />
	 * Note that you do not need to set a password to connect to the large public
	 * IRC networks like QuakeNet, EFNet etc. To use no password in your IRC
	 * connection, use <code>""</code> or <code>null</code> for the password
	 * argument in the constructor.
	 * @param host The hostname of the server we want to connect to.
	 * @param ports The portrange to which we want to connect.
	 * @param pass The password of the IRC server. If your server isn't 
	 *             secured by a password (that's normal), use 
	 *             <code>null</code> or <code>""</code>.
	 * @param nick The nickname for the connection. Is used to register the 
	 *             connection. 
	 * @param username The username. Is used to register the connection. 
	 * @param realname The realname. Is used to register the connection. 
	 * @throws IllegalArgumentException If the <code>host</code> or 
	 *                                  <code>ports</code> is <code>null</code> or
	 *                                  <code>ports</code>' length is 
	 *                                  <code>0</code>.
	 * @see #connect()
	 */
	public SSLIRCConnection(String host, int[] ports, String pass, String nick, 
			String username, String realname) {
		super(host, ports, pass, nick, username, realname);
	}
	
// ------------------------------
	
	/**
	 * Creates a new IRC connection with secure sockets (SSL).<br />
	 * The difference to the other constructor is, that it transmits the ports as
	 * two <code>int</code>s. Thus, only a portrange from port <code>x</code> to
	 * port <code>y</code> like from port 6000 to 6010 can be selected.<br />
	 * <br />
	 * The constructor prepares a new IRC connection with secure sockets which 
	 * can be really started by invoking the <code>connect</code> method. Before 
	 * invoking it, you should set the <code>IRCEventListener</code>, optionally
	 * the <code>TrustManager</code>, if you don't want to use the 
	 * <code>SSLDefaultTrustManager</code> which accepts the X509 certificate 
	 * automatically, and other settings.<br />
	 * Note that you do not need to set a password to connect to the large public
	 * IRC networks like QuakeNet, EFNet etc. To use no password in your IRC
	 * connection, use <code>""</code> or <code>null</code> for the password
	 * argument in the constructor.
	 * @param host The hostname of the server we want to connect to.
	 * @param portMin The beginning of the port range we are going to connect 
	 *                to.
	 * @param portMax The ending of the port range we are going to connect to.
	 * @param pass The password of the IRC server. If your server isn't 
	 *             secured by a password (that's normal), use 
	 *             <code>null</code> or <code>""</code>.
	 * @param nick The nickname for the connection. Is used to register the 
	 *             connection. 
	 * @param username The username. Is used to register the connection. 
	 * @param realname The realname. Is used to register the connection. 
	 * @throws IllegalArgumentException If the <code>host</code> is 
	 *                                  <code>null</code>.
	 * @see #connect()
	 */
	public SSLIRCConnection(String host, int portMin, int portMax, String pass, 
			String nick, String username, String realname) {
		super(host, portMin, portMax, pass, nick, username, realname);
	}
	
// ------------------------------
	
	/** 
	 * Establish a connection to the server.<br />
	 * This method must be invoked to start a connection; the constructor doesn't 
	 * do that!<br />
	 * It tries all set ports until one is open. If all ports fail it throws an 
	 * <code>IOException</code>. If anything SSL related fails (for example 
	 * conflicts with the algorithms or during the handshaking), a 
	 * <code>SSLException</code> is thrown. <br />
	 * You can invoke <code>connect</code> only one time.
	 * @throws IOException If an I/O error occurs. 
	 * @throws SSLException If anything with the secure sockets fails. 
	 * @throws SocketException If the <code>connect</code> method was already
	 *                         invoked.
	 * @see #isConnected()
	 * @see #doQuit()
	 * @see #doQuit(String)
	 * @see #close()
	 */
	public void connect() throws IOException {
		if (level != 0) // otherwise disconnected or connect
			throw new SocketException("Socket closed or already open ("+ level +")");
		IOException exception = null;
		SSLSocketFactory sf = null;
		SSLSocket s = null;
		for (int i = 0; i < ports.length && s == null; i++) {
			try {
				if (sf == null)
					sf = createSocketFactory();
				s = (SSLSocket)sf.createSocket(host, ports[i]);
				s.startHandshake();
				exception = null; 
			} catch (IOException exc) {
				if (s != null)
					s.close();
				s = null;
				exception = exc; 
			}
		}
		if (exception != null)
			throw exception; // connection wasn't successful at any port
		
		prepare(s);
	}
	
// ------------------------------
	
	/**
	 * Creates a new <code>SSLSocketFactory</code> on whose base a new 
	 * <code>SSLSocket</code> can be created. This method is used by the 
	 * <code>connect</code> method. If a <code>NoSuchAlgorithmException</code>,
	 * <code>KeyManagementException</code>, or any other exception is thrown,
	 * it's catched a <code>SSLException</code> is thrown. This 
	 * <code>SSLException</code> contains all information about the exception
	 * which was really thrown.<br /><br />
	 * If there are now <code>TrustManager</code>s set at the moment, an instance 
	 * of <code>SSLDefultTrustManager</code> is set automatically.
	 * @throws SSLException If any exception is thrown. It contains all the thrown
	 *                      exception's information.
	 * @return A new <code>SSLSocketFactory on which base a new 
	 *         <code>SSLSocket</code> can be created.
	 */
	private SSLSocketFactory createSocketFactory() throws SSLException {
		if (trustManagers.size() == 0)
			addTrustManager(new SSLDefaultTrustManager());
		try {
			Security.addProvider(new Provider());
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, getTrustManagers(), null);
			SSLSocketFactory socketFactory = context.getSocketFactory();
			return socketFactory;
		} catch (Exception exc) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exc.printStackTrace(pw);
			pw.close();
			throw new SSLException("Exception while preparing "+
					"the SSLSocket:\n---\n"+ sw.toString() + "---");
		}
	}
	
// ------------------------------
	
	/**
	 * Adds a new <code>TrustManager</code>. <br />
	 * Please remind that we're talking here about 
	 * <code>com.sun.net.ssl.TrustManager</code>s of the JSSE API and not the 
	 * <code>javax.net.ssl.TrustManager</code>s of the Java 1.4 API!
	 * @param trustManager The <code>TrustManager</code> object which is to add.
	 * @see #removeTrustManager(TrustManager)
	 * @see #getTrustManagers()
	 */
	public void addTrustManager(TrustManager trustManager) {
		trustManagers.add(trustManager);
	}
	
// ------------------------------
	
	/**
	 * Removes one <code>TrustManager</code>.<br />
	 * Please remind that we're talking here about 
	 * <code>com.sun.net.ssl.TrustManager</code>s of the JSSE API and not the 
	 * <code>javax.net.ssl.TrustManager</code>s of the Java 1.4 API!
	 * @param trustManager The <code>TrustManager</code> object which is to 
	 *                     remove.
	 * @return <code>true</code> if a <code>TrustManager</code> was removed.
	 * @see #addTrustManager(TrustManager)
	 * @see #getTrustManagers()
	 */
	public boolean removeTrustManager(TrustManager trustManager) {
		return trustManagers.remove(trustManager);
	}
	
// ------------------------------
	
	/**
	 * Returns the set <code>TrustManager</code>s. The default 
	 * <code>TrustManager</code> is an instance of 
	 * <code>SSLDefaultTrustManager</code>, which is set when you invoke the
	 * <code>connect</code> method without having set another 
	 * <code>TrustManager</code>.<br />
	 * Please remind that we're talking here about 
	 * <code>com.sun.net.ssl.TrustManager</code>s of the JSSE API and not the 
	 * <code>javax.net.ssl.TrustManager</code>s of the Java 1.4 API!
	 * @return The set <code>TrustManager</code>s.
	 * @see #addTrustManager(TrustManager)
	 * @see #removeTrustManager(TrustManager)
	 */
	public TrustManager[] getTrustManagers() {
		TrustManager[] tm = new TrustManager[trustManagers.size()];
		trustManagers.toArray(tm);
		return tm;
	}
}
