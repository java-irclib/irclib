/*
 * IRClib -- A Java Internet Relay Chat library -- class SSLSocketFactoryFactory
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLException;

/**
 * Factory for <code>javax.net.ssl.SSLSocketFactory</code>. Firstly tries to 
 * creates this object using exclusively the <code>javax.net.ssl.*</code> 
 * classes and then tries to do so using the <code>javax.net.ssl.*</code> 
 * classes plus the <code>com.sun.net.ssl.*</code> classes.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.00
 * @see SSLIRCConnection
 * @see SSLIRCConnection#connect()
 */
class SSLSocketFactoryFactory {
	/**
	 * Disallow instances.
	 */
	private SSLSocketFactoryFactory() {
	}
	
// ------------------------------
	
	/**
	 * Creates a new <code>SSLSocketFactory</code>. This method first tries to 
	 * create it using <code>javax.net.ssl.*</code> classes. If this fails, it 
	 * tries to access the old JSSE classes in <code>com.sun.net.ssl.*</code>.
	 * @throws SSLNotSupportedException If neither JSSE nor J2SE >= 1.4 are 
	 *                                  installed.
	 * @throws SSLException If any exception is thrown. It contains all the 
	 *                      thrown exception's information.
	 * @return A new <code>SSLSocketFactory</code>.
	 */
	public static SSLSocketFactory createSSLSocketFactory(SSLTrustManager[] tm) 
	throws SSLException, SSLNotSupportedException {
		boolean sslNotSupported = false;
		Exception exception = null;
		
		try {
			return createJava14SSLSocketFactory(tm);
		} catch (ClassNotFoundException cnfe) {
			// try JSSE after try/catch block
		} catch (NoSuchMethodException nsme) {
			// try JSSE after try/catch block
		} catch (InvocationTargetException ite) {
			// try JSSE after try/catch block
		} catch (IllegalAccessException eae) {
			// try JSSE after try/catch block
		} catch (Exception exc) {
			// try JSSE after try/catch block
		}
		
		try {
			return createJsseSSLSocketFactory(tm);
		} catch (ClassNotFoundException cnfe) {
			exception = cnfe;
			sslNotSupported = true;
		} catch (NoSuchMethodException nsme) {
			exception = nsme;
			sslNotSupported = true;
		} catch (InstantiationException ie) {
			exception = ie;
			sslNotSupported = true;
		} catch (InvocationTargetException ite) {
			exception = ite;
			sslNotSupported = true;
		} catch (IllegalAccessException eae) {
			exception = eae;
			sslNotSupported = true;
		} catch (Exception exc) {
			exception = exc;
		}
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		pw.close();
		
		if (sslNotSupported) {
			throw new SSLNotSupportedException("Neither JSSE nor J2SE " +
					">= 1.4 installed:\n---\n"+
			sw.toString() +"---");
		} else {
			throw new SSLException("Exception while creating "+
					"the SSLSocketFactory with JSSE:\n---\n"+ 
					sw.toString() + "---");
		}
	}
	
// ------------------------------
	
	/**
	 * Creates a new <code>SSLSocketFactory</code> using the 
	 * <code>javax.net.ssl.*</code> classes.
	 * @param tm The trustmanagers.
	 * @return A <code>javax.net.ssl.SSLSocketFactory</code>.
	 * @throws ClassNotFoundException If the classes could not be loaded and 
	 * accessed properly
	 * @throws NoSuchMethodException If the classes could not be loaded and 
	 * accessed properly
	 * @throws InvocationTargetException If the classes could not be loaded and 
	 * accessed properly
	 * @throws IllegalAccessException If the classes could not be loaded and 
	 * accessed properly
	 */
	private static SSLSocketFactory createJava14SSLSocketFactory(SSLTrustManager[] tm) 
	throws ClassNotFoundException, NoSuchMethodException, 
	InvocationTargetException, IllegalAccessException {
		/*
		 * The code below does the following:
		 * TrustManagerJava14Wrapper[] tmWrappers = TrustManagerJava14Wrapper.wrap(tm);
		 * javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("SSL");
		 * context.init(null, tmWrappers, null);
		 * SSLSocketFactory socketFactory = context.getSocketFactory();
		 */
		
		Class stringClass = String.class;
		Class contextClass = Class.forName("javax.net.ssl.SSLContext");
		Class keyManagerClass = Class.forName("javax.net.ssl.KeyManager");
		Class keyManagerArrayClass = java.lang.reflect.Array.newInstance(keyManagerClass, 0).getClass();
		Class trustManagerClass = Class.forName("javax.net.ssl.TrustManager");
		Class trustManagerArrayClass = java.lang.reflect.Array.newInstance(trustManagerClass, 0).getClass();
		Class secureRandomClass = java.security.SecureRandom.class;

		Method getInstanceMethod = contextClass.getMethod("getInstance", new Class[] { stringClass });
		Method initMethod = contextClass.getMethod("init", new Class[] { keyManagerArrayClass, trustManagerArrayClass, secureRandomClass });
		Method getSocketFactoryMethod = contextClass.getMethod("getSocketFactory", null);
		
		Class.forName("javax.net.ssl.X509TrustManager"); // check for availability
		TrustManagerJava14Wrapper[] tmWrappers = TrustManagerJava14Wrapper.wrap(tm);
		
		String protocol = SSLIRCConnection.protocol;
		Object context = getInstanceMethod.invoke(null, new Object[] { protocol });
		initMethod.invoke(context, new Object[] { null, tmWrappers, null });
		Object socketFactory = getSocketFactoryMethod.invoke(context, null);

		return (SSLSocketFactory)socketFactory;
	}
	
// ------------------------------

	/**
	 * Creates a new <code>SSLSocketFactory</code> using the 
	 * <code>com.sun.net.ssl.*</code> and <code>javax.net.ssl.*</code> classes.
	 * @param tm The trustmanagers.
	 * @return A <code>javax.net.ssl.SSLSocketFactory</code>.
	 * @throws ClassNotFoundException If the classes could not be loaded and 
	 * accessed properly
	 * @throws NoSuchMethodException If the classes could not be loaded and 
	 * accessed properly
	 * @throws InvocationTargetException If the classes could not be loaded and 
	 * accessed properly
	 * @throws IllegalAccessException If the classes could not be loaded and 
	 * accessed properly
	 */
	private static SSLSocketFactory createJsseSSLSocketFactory(SSLTrustManager[] tm) 
	throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
	InvocationTargetException, IllegalAccessException {
		/*
		 * The code below does the following:
		 * java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		 * 
		 * TrustManagerJsseWrapper[] tmWrappers = TrustManagerJsseWrapper.wrap(tm);
		 * com.sun.net.ssl.SSLContext context = com.sun.net.ssl.SSLContext.getInstance("SSL");
		 * context.init(null, tmWrappers, null);
		 * SSLSocketFactory socketFactory = context.getSocketFactory();
		 */
		
		Class securityClass = Class.forName("java.security.Security");
		Class providerClass = Class.forName("java.security.Provider");
		Class sslProviderClass = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
		
		Method addProvider = securityClass.getMethod("addProvider", new Class[] { providerClass });
		
		Object provider = sslProviderClass.newInstance();
		addProvider.invoke(null, new Object[] { provider });
		
		
		Class stringClass = String.class;
		Class contextClass = Class.forName("com.sun.net.ssl.SSLContext");
		Class keyManagerClass = Class.forName("com.sun.net.ssl.KeyManager");
		Class keyManagerArrayClass = java.lang.reflect.Array.newInstance(keyManagerClass, 0).getClass();
		Class trustManagerClass = Class.forName("com.sun.net.ssl.TrustManager");
		Class trustManagerArrayClass = java.lang.reflect.Array.newInstance(trustManagerClass, 0).getClass();
		Class secureRandomClass = java.security.SecureRandom.class;

		Method getInstanceMethod = contextClass.getMethod("getInstance", new Class[] { stringClass });
		Method initMethod = contextClass.getMethod("init", new Class[] { keyManagerArrayClass, trustManagerArrayClass, secureRandomClass });
		Method getSocketFactoryMethod = contextClass.getMethod("getSocketFactory", null);
		
		Class.forName("com.sun.net.ssl.X509TrustManager"); // check for availability
		TrustManagerJsseWrapper[] tmWrappers = TrustManagerJsseWrapper.wrap(tm);
		
		String protocol = SSLIRCConnection.protocol;
		Object context = getInstanceMethod.invoke(null, new Object[] { protocol });
		initMethod.invoke(context, new Object[] { null, tmWrappers, null });
		Object socketFactory = getSocketFactoryMethod.invoke(context, null);

		return (SSLSocketFactory)socketFactory;
	}
}
