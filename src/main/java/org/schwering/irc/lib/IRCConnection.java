/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCConnection
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Creates a new connection to an IRC server. It's the main class of the
 * IRClib, the point everything starts.
 * <p>
 * The following sample code tries to establish an IRC connection to an 
 * IRC server:
 * <p>
 * <hr /><pre>
 * /&#42; 
 *  &#42; The following code of a class which imports org.schwering.irc.lib.*
 *  &#42; prepares an IRC connection and then tries to establish the connection.
 *  &#42; The server is "irc.somenetwork.com", the default portrange (6667 and 
 *  &#42; 6669) is set, no password is used (null). The nickname is "Foo" and 
 *  &#42; the realname is "Mr. Foobar". The username "foobar".
 *  &#42; Because of setDaemon(true), the JVM exits even if this thread is 
 *  &#42; running.
 *  &#42; An instance of the class MyListener which must implement 
 *  &#42; IRCActionListener is added as only event-listener for the connection. 
 *  &#42; The connection is told to parse out mIRC color codes and to enable
 *  &#42; automatic PING? PONG! replies.
 *  &#42;/
 * IRCConnection conn = new IRCConnection(
 *                            "irc.somenetwork.com", 
 *                            6667, 
 *                            6669, 
 *                            null, 
 *                            "Foo", 
 *                            "Mr. Foobar", 
 *                            "foo@bar.com" 
 *                          ); 
 * 
 * conn.addIRCEventListener(new MyListener()); 
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
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 3.05
 * @see IRCEventListener
 * @see IRCParser
 * @see IRCUtil
 * @see SSLIRCConnection
 */
public class IRCConnection extends Thread {
	
	/** 
	 * This <code>Socket</code> is a connection to the IRC server. 
	 */
	private Socket socket;
	
	/**
	 * This is like a UNIX-runlevel. Its value indicates the level of the 
	 * <code>IRCConnection</code> object. <code>0</code> means that the object 
	 * has not yet been connected, <code>1</code> means that it's connected but 
	 * not registered, <code>2</code> means that it's connected and registered 
	 * but still waiting to receive the nickname the first time, <code>3</code> 
	 * means that it's connected and registered, and <code>-1</code> means that 
	 * it was connected but is disconnected. 
	 * Therefore the default value is <code>0</code>.
	 */
	protected byte level = 0;
	
	/** 
	 * The host of the IRC server. 
	 */
	protected String host;
	
	/** 
	 * The <code>int[]</code> contains all ports to which we are going to try to
	 * connect. This can be a portrange from port 6667 to 6669, for example.
	 */
	protected int[] ports;
	
	/** 
	 * The <code>BufferedReader</code> receives Strings from the IRC server. 
	 */
	private BufferedReader in;
	
	/** 
	 * The <code>PrintWriter</code> sends Strings to the IRC server. 
	 */
	private PrintWriter out;
	
	/**
	 * The <code>String</code> contains the name of the character encoding used
	 * to talk to the server. This can be ISO-8859-1 or UTF-8 for example. The
	 * default is ISO-8859-1.
	 */
	protected String encoding = "ISO-8859-1";
	
	/**
	 * This array contains <code>IRCEventListener</code> objects.
	 */
	private IRCEventListener[] listeners = new IRCEventListener[0];
	
	/** 
	 * This <code>int</code> is the connection's timeout in milliseconds. It's 
	 * used in the <code>Socket.setSoTimeout</code> method. The default is 
	 * <code>1000 * 60 * 15</code> millis which are 15 minutes. 
	 */
	private int timeout = 1000 * 60 * 15;
	
	/** 
	 * This <code>boolean</code> stands for enabled (<code>true</code>) or 
	 * disabled (<code>false</code>) ColorCodes.<br />Default is enabled 
	 * (<code>false</code>).
	 */
	private boolean colorsEnabled = false;
	
	/** 
	 * This <code>boolean</code> stands for enabled or disabled automatic PING? 
	 * PONG! support. <br />It means, that if the server asks with PING for the 
	 * ping, the PONG is automatically sent. Default is automatic PONG enabled 
	 * (<code>true</code>). 
	 */
	private boolean pongAutomatic = true;
	
	/** 
	 * The password, which is needed to get access to the IRC server. 
	 */
	private String pass;
	
	/** 
	 * The user's nickname, which is indispensably to connect. 
	 */
	private String nick;
	
	/** 
	 * The user's realname, which is indispensably to connect. 
	 */
	private String realname;
	
	/** 
	 * The user's username, which is indispensable to connect. 
	 */
	private String username;
	
	/**
	 * Activiates or deactivates output of incoming and outgoing lines.
	 */
	private boolean debug = false;
	
	/**
	 * The writer used for debugging output.
	 */
	private PrintStream debugStream = System.out;
	
	/**
	 * Indicates how to handle unexpected exceptions.
	 */
	private int exceptionHandling = RETHROW_RUNTIME_EXCEPTION;
	
	/**
	 * A number of unexpected exceptions are simply ignored. Not recommended.
	 */
	public static final int IGNORE_EXCEPTION = 0;
	
	/**
	 * Some unexpected exceptions are rethrown as RuntimeExceptions.
	 */
	public static final int RETHROW_RUNTIME_EXCEPTION = 1;
	
	/**
	 * The stack trace of unexpected exceptions is printed to the debug stream.  
	 */
	public static final int PRINT_TO_DEBUG_STREAM = 2;
	
// ------------------------------
	
	/**
	 * Creates a new IRC connection. <br />
	 * The difference to the other constructor is, that it transmits the ports in
	 * an <code>int[]</code>. Thus, also ports like 1024, 2048, 6667 and 
	 * 6669 can be selected.<br /><br />
	 * The constructor prepares a new IRC connection which can be really started 
	 * by invoking the <code>connect</code> method. Before invoking it, you should
	 * set the <code>IRCEventListener</code> and other settings.<br />
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
	public IRCConnection(String host, int[] ports, String pass, String nick, 
			String username, String realname) {
		if (host == null || ports == null || ports.length == 0) 
			throw new IllegalArgumentException("Host and ports may not be null."); 
		this.host = host;
		this.ports = ports;
		this.pass = (pass != null && pass.length() == 0) ? null : pass;
		this.nick = nick;
		this.username = username;
		this.realname = realname;
	}
	
// ------------------------------
	
	/**
	 * Creates a new IRC connection. <br />
	 * The difference to the other constructor is, that it transmits the ports as
	 * two <code>int</code>s. Thus, only a portrange from port <code>x</code> to
	 * port <code>y</code> like from port 6667 to 6669 can be selected.<br />
	 * <br />
	 * The constructor prepares a new IRC connection which can be really started 
	 * by invoking the <code>connect</code> method. Before invoking it, you should
	 * set the <code>IRCEventListener</code> and other settings.<br />
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
	public IRCConnection(String host, int portMin, int portMax, String pass, 
			String nick, String username, String realname) {
		this(host, portRangeToArray(portMin, portMax), pass, nick, username, 
				realname);
	}
	
// ------------------------------
	
	/**
	 * Converts a portrange which starts with a given <code>int</code> and ends 
	 * with a given <code>int</code> into an array which contains all 
	 * <code>int</code>s from the beginning to the ending (including beginning
	 * and ending).<br />
	 * If <code>portMin > portMax</code>, the portrange is turned arount 
	 * automatically.
	 * @param portMin The beginning port of the portrange.
	 * @param portMax The ending port of the portrange.
	 */
	private static int[] portRangeToArray(int portMin, int portMax) {
		if (portMin > portMax) {
			int tmp = portMin;
			portMin = portMax;
			portMax = tmp;
		}
		int[] ports = new int[portMax - portMin + 1];
		for (int i = 0; i < ports.length; i++)
			ports[i] = portMin + i;
		return ports;
	}
	
// ------------------------------
	
	/** 
	 * Establish a connection to the server. <br />
	 * This method must be invoked to start a connection; the constructor doesn't 
	 * do that!<br />
	 * It tries all set ports until one is open. If all ports fail it throws an 
	 * <code>IOException</code>.<br />
	 * You can invoke <code>connect</code> only one time.
	 * @throws IOException If an I/O error occurs. 
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
		Socket s = null;
		for (int i = 0; i < ports.length && s == null; i++) {
			try {
				s = new Socket(host, ports[i]);
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
	 * Invoked by the <code>connect</code> method, this method prepares the 
	 * connection. <br />
	 * It initializes the class-vars for the inputstream and the outputstream of 
	 * the socket, starts the registration of at the IRC server by calling 
	 * <code>register()</code> and starts the receiving of lines from the server 
	 * by starting the thread with the <code>start</code> method.<br /><br />
	 * This method must be protected, because it is used by extending classes,
	 * which override the <code>connect</code> method. 
	 * @param s The socket which is used for the connection.
	 * @throws IOException If an I/O error occurs.
	 * @see #connect()
	 * @see #run()
	 */
	protected void prepare(Socket s) throws IOException {
		if (s == null)
			throw new SocketException("Socket s is null, not connected");
		socket = s;
		level = 1;
		s.setSoTimeout(timeout);
		in = new BufferedReader(new InputStreamReader(s.getInputStream(), 
				encoding));
		out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), 
				encoding));
		start();
		register();
	}
	
// ------------------------------
	
	/** 
	 * Registers the connection with the IRC server. <br />
	 * In fact, it sends a password (if set, else nothing), the nickname and the 
	 * user, the realname and the host which we're connecting to.<br />
	 * The action synchronizes <code>code> so that no important messages 
	 * (like the first PING) come in before this registration is finished.<br />
	 * The <code>USER</code> command's format is:<br /><code>
	 * &lt;username&gt; &lt;localhost&gt; &lt;irchost&gt; &lt;realname&gt;
	 * </code>
	 */
	private void register() {
		if (pass != null)
			send("PASS "+ pass); 
		send("NICK "+ nick); 
		send("USER "+ username +" "+ socket.getLocalAddress().getHostAddress()
				+" "+ host +" :"+ realname); 
	}
	
// ------------------------------
	
	/** 
	 * The <code>Thread</code> is started by the <code>connect</code> method.
	 * It's task is to receive strings from the IRC server and hand them over 
	 * to the <code>get</code> method.<br />
	 * <br />
	 * Possibly occuring <code>IOException</code>s are handled according to the
	 * set exception handling.
	 */
	public void run() {
		try {
			String line;
			while ((line = in.readLine()) != null) {
				if (debug) {
					synchronized (debugStream) {
						debugStream.println("IN:  "+ line);
					}
				}
				get(line);
			}
		} catch (IOException exc) {
			if (debug) {
				handleException(exc);
			}
			close();
		} finally {
			close();
		}
	}
	
// ------------------------------
	
	/** 
	 * Sends a String to the server. 
	 * You should use this method only, if you must do it. For most purposes, 
	 * there are <code>do*</code> methods (like <code>doJoin</code>). A carriage 
	 * return line feed (<code>\r\n</code>) is appended automatically.
	 * @param line The line which should be send to the server without the 
	 *             trailing carriage return line feed (<code>\r\n</code>).
	 */
	public void send(String line) {
		try {
			if (debug) {
				synchronized (debugStream) {
					debugStream.println("OUT: "+ line);
				}
			}
			out.write(line +"\r\n");
			out.flush();
			if (level == 1) { // not registered
				IRCParser p = new IRCParser(line);
				if ("NICK".equalsIgnoreCase(p.getCommand()))
					nick = p.getParameter(1).trim();
			}
		} catch (Exception exc) {
			if (debug) {
				synchronized (debugStream) {
					debugStream.println("OUT: "+ line);
				}
			}
			throw new RuntimeException(exc);
		}
	}
	
// ------------------------------
	
	/** 
	 * Just parses a String given as the only argument with the help of the 
	 * <code>IRCParser</code> class. Then it controls the command and fires events
	 * through the <code>IRCEventListener</code>.<br />
	 * @param line The line which is sent from the server.
	 */
	private synchronized void get(String line) {
		IRCParser p;
		try {
			p = new IRCParser(line, colorsEnabled);
		} catch (Exception exc) {
			return;
		}
		String command = p.getCommand();
		int reply; // 3-digit reply will be parsed in the later if-condition
		
		if ("PRIVMSG".equalsIgnoreCase(command)) { // MESSAGE
			
			IRCUser user = p.getUser();
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onPrivmsg(middle, user, trailing);

        } else if ("MODE".equalsIgnoreCase(command)) { // MODE
			
			String chan = p.getParameter(1);
			if (IRCUtil.isChan(chan)) {
				IRCUser user = p.getUser();
				String param2 = p.getParameter(2);
				String paramsFrom3 = p.getParametersFrom(3);
				for (int i = listeners.length - 1; i >= 0; i--)
					listeners[i].onMode(chan, user,
							new IRCModeParser(param2, paramsFrom3));
			} else {
				IRCUser user = p.getUser();
				String paramsFrom2 = p.getParametersFrom(2);
				for (int i = listeners.length - 1; i >= 0; i--)
					listeners[i].onMode(user, chan, paramsFrom2);
			}
			
		} else if ("PING".equalsIgnoreCase(command)) { // PING
			
			String ping = p.getTrailing(); // no int cause sometimes it's text
			if (pongAutomatic)
				doPong(ping);
			else
				for (int i = listeners.length - 1; i >= 0; i--)
					listeners[i].onPing(ping);
			
			if (level == 1) { // not registered
				level = 2; // first PING received -> connection
				for (int i = listeners.length - 1; i >= 0; i--)
					listeners[i].onRegistered();
			}
			
		} else if ("JOIN".equalsIgnoreCase(command)) { // JOIN
			
			IRCUser user = p.getUser();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onJoin(trailing, user);
			
		} else if ("NICK".equalsIgnoreCase(command)) { // NICK
			
			IRCUser user = p.getUser();
			String changingNick = p.getNick();
			String newNick = p.getTrailing();
			if (changingNick.equalsIgnoreCase(nick))
				nick = newNick;
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onNick(user, newNick);
			
		} else if ("QUIT".equalsIgnoreCase(command)) { // QUIT
			
			IRCUser user = p.getUser();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onQuit(user, trailing);
			
		} else if ("PART".equalsIgnoreCase(command)) { // PART
			
			IRCUser user = p.getUser();
			String chan = p.getParameter(1);
			String msg = p.getParameterCount() > 1 ? p.getTrailing() : "";
			// not logic: "PART :#zentrum" is without msg, "PART #zentrum :cjo all"
			// is with msg. so we cannot use getMiddle and getTrailing :-/
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onPart(chan, user, msg);
			
		} else if ("NOTICE".equalsIgnoreCase(command)) { // NOTICE
			
			IRCUser user = p.getUser();
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onNotice(middle, user, trailing);
			
		} else if ((reply = IRCUtil.parseInt(command)) >= 1 && reply < 400) { // RPL
			
			String potNick = p.getParameter(1);
			if ((level == 1 || level == 2) && nick.length() > potNick.length() &&
					nick.substring(0, potNick.length()).equalsIgnoreCase(potNick)) {
				nick = potNick;
				if (level == 2)
					level = 3;
			}
			
			if (level == 1 && nick.equals(potNick)) { // not registered
				level = 2; // if first PING wasn't received, we're
				for (int i = listeners.length - 1; i >= 0; i--)
					listeners[i].onRegistered(); // connected now for sure
			}
			
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onReply(reply, middle, trailing);
			
		} else if (reply >= 400 && reply < 600) { // ERROR
			
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onError(reply, trailing);
			
		} else if ("KICK".equalsIgnoreCase(command)) { // KICK
			
			IRCUser user = p.getUser();
			String param1 = p.getParameter(1);
			String param2 = p.getParameter(2);
			String msg = (p.getParameterCount() > 2) ? p.getTrailing() : "";
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onKick(param1, user, param2, msg);
			
		} else if ("INVITE".equalsIgnoreCase(command)) { // INVITE
			
			IRCUser user = p.getUser();
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onInvite(trailing, user, middle);
			
		} else if ("TOPIC".equalsIgnoreCase(command)) { // TOPIC
			
			IRCUser user = p.getUser();
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onTopic(middle, user, trailing);
			
		} else if ("ERROR".equalsIgnoreCase(command)) { // ERROR
			
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onError(trailing);
			
		} else { // OTHER
			
			String prefix = p.getPrefix();
			String middle = p.getMiddle();
			String trailing = p.getTrailing();
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].unknown(prefix, command, middle, trailing);
			
		}
	}

// ------------------------------
	
	/** 
	 * Close down the connection brutally. <br />
	 * It does *NOT* send the proper IRC command <code>QUIT</code>. You should 
	 * always use the <code>doQuit</code> methods or <code>send("QUIT")</code> 
	 * instead of this method. <br />
	 * You should use this method to close down the connection only when the IRC
	 * server doesn't react to the <code>QUIT</code> command.
	 * <br />
	 * Possibly occuring <code>IOException</code>s are handled according to the
	 * set exception handling.
	 * @see #connect()
	 * @see #doQuit
	 * @see #doQuit(String)
	 */
	public synchronized void close() {
		try {
			if (!isInterrupted())
				interrupt();
		} catch (Exception exc) {
			handleException(exc);
		}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception exc) {
			handleException(exc);
		}
		try {
			if (out != null)
				out.close();
		} catch (Exception exc) {
			handleException(exc);
		}
		try {
			if (in != null)
				in.close();
		} catch (Exception exc) {
			handleException(exc);
		}
		if (this.level != -1) {
			this.level = -1;
			for (int i = listeners.length - 1; i >= 0; i--)
				listeners[i].onDisconnected(); 
		}
		socket = null;
		in = null;
		out = null;
		listeners = new IRCEventListener[0];
	}


// ------------------------------

	/**
	 * Sets the current exception handling mode.
	 * This should be either <code>RETHROW_RUNTIME_EXCEPTION</code> or
	 * <code>PRINT_TO_DEBUG_STREAM</code> or <code>IGNORE_EXCEPTION<code>.<br />
	 * <br />
	 * The default value is <code>RETHROW_RUNTIME_EXCEPTION</code>.
	 * @see #IGNORE_EXCEPTION
	 * @see #RETHROW_RUNTIME_EXCEPTION
	 * @see #PRINT_TO_DEBUG_STREAM
	 */
	public void setExceptionHandling(int exceptionHandling) {
		this.exceptionHandling = exceptionHandling;
	}

// ------------------------------

	/**
	 * Returns the current exception handling mode.
	 * This should be either <code>RETHROW_RUNTIME_EXCEPTION</code> or
	 * <code>PRINT_TO_DEBUG_STREAM</code> or <code>IGNORE_EXCEPTION<code>, but
	 * all other integer values might also occur when you set the respective
	 * value.<br />
	 * <br />
	 * The default value is <code>RETHROW_RUNTIME_EXCEPTION</code>.
	 * @see #setExceptionHandling(int)
	 */
	public int getExceptionHandling() {
		return exceptionHandling;
	}

// ------------------------------

	/**
	 * Handles the exception according to the current exception handling mode.
	 */
	private void handleException(Exception exc) {
		switch (exceptionHandling) {
		case PRINT_TO_DEBUG_STREAM:
			synchronized (debugStream) {
				exc.printStackTrace(debugStream);
			}
			break;
		case RETHROW_RUNTIME_EXCEPTION:
			throw new RuntimeException(exc);
		case IGNORE_EXCEPTION:
			break;
		}
	}
	
// ------------------------------
	
	/** 
	 * Adds a new {@link IRCEventListener} which listens 
	 * for actions coming from the IRC server. 
	 * @param l An instance of the 
	 *          {@link IRCEventListener} interface.
	 * @throws IllegalArgumentException If <code>listener</code> is 
	 *                                  <code>null</code>.
	 */
	public synchronized void addIRCEventListener(IRCEventListener l) {
		if (l == null)
			throw new IllegalArgumentException("Listener is null.");
		int len = listeners.length;
		IRCEventListener[] oldListeners = listeners;
		listeners = new IRCEventListener[len + 1];
		System.arraycopy(oldListeners, 0, listeners, 0, len);
		listeners[len] = l;
	}
	
// ------------------------------
	
	/** 
	 * Adds a new {@link IRCEventListener} which listens 
	 * for actions coming from the IRC server at a given index. 
	 * @param l An instance of the 
	 *          {@link IRCEventListener} interface.
	 * @param i The designated index of the listener.
	 * @throws IllegalArgumentException If <code>listener</code> is 
	 *                                  <code>null</code>.
	 * @throws IndexOutOfBoundsException If <code>i</code> is not greater than
	 *                                   0 and less or equal than 
	 *                                   <code>listeners.length</code>.
	 */
	public synchronized void addIRCEventListener(IRCEventListener l, int i) {
		if (l == null)
			throw new IllegalArgumentException("Listener is null.");
		if (i < 0 || i > listeners.length)
			throw new IndexOutOfBoundsException("i is not in range");
		int len = listeners.length;
		IRCEventListener[] oldListeners = listeners;
		listeners = new IRCEventListener[len + 1];
		if (i > 0)
			System.arraycopy(oldListeners, 0, listeners, 0, i);
		if (i < listeners.length)
			System.arraycopy(oldListeners, i, listeners, i+1, len-i);
		listeners[i] = l;
	}
	
// ------------------------------
	
	/** 
	 * Removes the first occurence of the given 
	 * {@link IRCEventListener} from the listener-vector.
	 * @param l An instance of the 
	 *          {@link IRCEventListener} interface. 
	 * @return <code>true</code> if the listener was successfully removed;
	 *         <code>false</code> if it was not found.
	 */
	public synchronized boolean removeIRCEventListener(IRCEventListener l) {
		if (l == null)
			return false;
		int index = -1;
		for (int i = 0; i < listeners.length; i++)
			if (listeners[i].equals(l)) {
				index = i;
				break;
			}
		if (index == -1)
			return false;
		listeners[index] = null;
		int len = listeners.length - 1;
		IRCEventListener[] newListeners = new IRCEventListener[len];
		for (int i = 0, j = 0; i < len; j++)
			if (listeners[j] != null)
				newListeners[i++] = listeners[j];
		listeners = newListeners;
		return true;
	}
	
// ------------------------------
	
	/** 
	 * Enables or disables the mIRC colorcodes. 
	 * @param colors <code>true</code> to enable, <code>false</code> to disable 
	 *               colors. 
	 */
	public void setColors(boolean colors) {
		colorsEnabled = colors;
	}
	
// ------------------------------
	
	/** 
	 * Enables or disables the automatic PING? PONG! support. 
	 * @param pong <code>true</code> to enable automatic <code>PONG</code> 
	 *             reply, <code>false</code> makes the class fire 
	 *             <code>onPing</code> events.
	 */
	public void setPong(boolean pong) {
		pongAutomatic = pong;
	}
	
// ------------------------------
	
	/** 
	 * Changes the character encoding used to talk to the server. 
	 * This can be ISO-8859-1 or UTF-8 for example.
	 * This property must be set before a call to the <code>connect()</code> 
	 * method.
	 * @param encoding The new encoding string, e.g. <code>"UTF-8"</code>.
	 */
	public void setEncoding(String encoding) {
		this.encoding	= encoding;
	}
	
// ------------------------------
	
	/**
	 * Sets the connection's timeout in milliseconds. <br />
	 * The default is <code>1000 * 60 15</code> millis which are 15 minutes.
	 * <br />
	 * The possibly occuring <code>IOException</code> are handled according to
	 * the set exception handling.
	 * @param millis The socket's timeout in milliseconds. 
	 */
	public void setTimeout(int millis) {
		if (socket != null) {
			try {
				socket.setSoTimeout(millis);
			} catch (IOException exc) {
				handleException(exc);
			}
		}
		timeout = millis;
	}
	
// ------------------------------
	
	/**
	 * Enables or disables debugging output. By default, it's disabled.
	 * If debugging is enabled, each incoming and outgoing line is printed
	 * rawly.
	 * @param debug <code>true</code> enables debugging mode.
	 * @see #setDebugStream(PrintStream)
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
// ------------------------------
	
	/**
	 * Sets the debug writer. If <code>debugStream</code> is <code>null</code>,
	 * the debugging lines are printed to <code>System.out</code>.
	 * @param debugStream The stream used to print the incoming and outgoing
	 *                    lines if debugging is enabled.
	 * @see #setDebug(boolean)
	 */
	public void setDebugStream(PrintStream debugStream) {
		this.debugStream = debugStream != null ? debugStream : System.out;
	}
	
// ------------------------------
	
	/** 
	 * Tells whether there's a connection to the IRC network or not. <br />
	 * If <code>connect</code> wasn't called yet, it returns <code>false</code>.
	 * @return The status of the connection; <code>true</code> if it's connected. 
	 * @see #connect()
	 * @see #doQuit()
	 * @see #doQuit(String)
	 * @see #close()
	 */
	public boolean isConnected() {
		return level >= 1;
	}
	
// ------------------------------
	
	/** 
	 * Returns the nickname of this instance. 
	 * @return The nickname.
	 */
	public String getNick() {
		return nick;
	}
	
// ------------------------------
	
	/** 
	 * Returns the realname of this instance. 
	 * @return The realname.
	 */
	public String getRealname() {
		return realname;
	}
	
// ------------------------------
	
	/** 
	 * Returns the username of this instance. 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}
	
// ------------------------------
	
	/** 
	 * Returns the server of this instance. 
	 * @return The server's hostname.
	 */
	public String getHost() {
		return host;
	}
	
// ------------------------------
	
	/** 
	 * Returns the password of this instance. If no password is set, 
	 * <code>null</code> is returned.
	 * @return The password. If no password is set, <code>null</code> is
	 *         returned.
	 */
	public String getPassword() {
		return pass;
	}
	
// ------------------------------
	
	/** 
	 * Returns all ports to which the <code>IRCConnection</code> is going to try
	 * or has tried to connect to.
	 * @return The ports in an <code>int[]</code> array.
	 */
	public int[] getPorts() {
		return ports;
	}  
	
// ------------------------------
	
	/** 
	 * Returns the port to which the <code>IRCConnection</code> connected, or 
	 * <code>0</code> if the connection failed or wasn't tried yet.
	 * @return The port to which the <code>IRCConnection</code>, or 
	 *         <code>0</code> if the connection failed or wasn't tried yet.
	 */
	public int getPort() {
		return (socket != null) ? socket.getPort() : 0;
	}
	
// ------------------------------
	
	/**
	 * Indicates whether colors are stripped out or not.
	 * @return <code>true</code> if colors are disabled.
	 */
	public boolean getColors() {
		return colorsEnabled;
	}
	
// ------------------------------
	
	/**
	 * Indicates whether automatic PING? PONG! is enabled or not.
	 * @return <code>true</code> if PING? PONG! is done automatically.
	 */
	public boolean getPong() {
		return pongAutomatic;
	}
	
// ------------------------------
	
	/**
	 * Returns the encoding of the socket. 
	 * @return The socket's encoding.
	 */
	public String getEncoding() {
		return encoding;
	}
	
// ------------------------------
	
	/**
	 * Returns the timeout of the socket. <br />
	 * If an error occurs, which is never the case, <code>-1</code> is returned.
	 * <br />
	 * The possibly occuring <code>IOException</code> are handled according to
	 * the set exception handling.
	 * @return The timeout.
	 */
	public int getTimeout() {
		if (socket != null)
			try {
				return socket.getSoTimeout();
			} catch (IOException exc) {
				handleException(exc);
				return -1;
			}
			else 
				return timeout;
	}
	
// ------------------------------
	
	/**
	 * Returns the local address of the connection socket.
	 * If the connection is not yet connected, <code>null</code> is
	 * returned.
	 */
	public InetAddress getLocalAddress() {
		return (socket != null) ? socket.getLocalAddress() : null;
	}
	
// ------------------------------
	
	/**
	 * Generates a <code>String</code> with some information about the instance of
	 * <code>IRCConnection</code>.
	 * Its format is: <code>
	 * classname[host,portMin,portMax,username,nick,realname,pass,connected]
	 * </code>.
	 * @return A <code>String</code> with information about the instance.
	 */
	public String toString() {
		return getClass().getName() +"["+ host +","+ getPort() +","+ username +","+ 
		nick +","+ realname +","+ pass +","+ isConnected() +"]";
	}

// ------------------------------
	
	/** 
	 * Removes away message. 
	 */
	public void doAway() {
		send("AWAY");
	}  
	
// ------------------------------
	
	/** 
	 * Sets away message. 
	 * @param msg The away message.
	 */
	public void doAway(String msg) {
		send("AWAY :"+ msg);
	}  
	
// ------------------------------
	
	/** 
	 * Invites a user to a channel. 
	 * @param nick The nickname of the user who should be invited. 
	 * @param chan The channel the user should be invited to.
	 */
	public void doInvite(String nick, String chan) {
		send("INVITE "+ nick +" "+ chan);
	}  
	
// ------------------------------
	
	/** 
	 * Checks if one or more nicks are used on the server. 
	 * @param nick The nickname of the user we search for.
	 */
	public void doIson(String nick) {
		send("ISON "+ nick);
	}  
	
// ------------------------------
	
	/** 
	 * Joins a channel without a key. 
	 * @param chan The channel which is to join.
	 */
	public void doJoin(String chan) {
		send("JOIN "+ chan);
	}  
	
// ------------------------------
	
	/** 
	 * Joins a channel with a key. 
	 * @param chan The channel which is to join.
	 * @param key The key of the channel.
	 */
	public void doJoin(String chan, String key) {
		send("JOIN "+ chan +" "+ key);
	}  
	
// ------------------------------
	
	/** 
	 * Kicks a user from a channel. 
	 * @param chan The channel somebody should be kicked from. 
	 * @param nick The nickname of the user who should be kicked. 
	 */
	public void doKick(String chan, String nick) {
		send("KICK "+ chan +" "+ nick);
	}
	
// ------------------------------
	
	/** 
	 * Kicks a user from a channel with a comment. 
	 * @param chan The channel somebody should be kicked from.
	 * @param nick The nickname of the user who should be kicked.
	 * @param msg The optional kickmessage.
	 */
	public void doKick(String chan, String nick, String msg) {
		send("KICK "+ chan +" "+ nick +" :"+ msg);
	}  
	
// ------------------------------
	
	/** 
	 * Lists all channels with their topic and status. 
	 */
	public void doList() {
		send("LIST");
	}  
	
// ------------------------------
	
	/** 
	 * Lists channel(s) with their topic and status. 
	 * @param chan The channel the <code>LIST</code> refers to.
	 */
	public void doList(String chan) {
		send("LIST "+ chan);
	}  
	
// ------------------------------
	
	/** 
	 * Lists all visible users.
	 */
	public void doNames() {
		send("NAMES");
	}  
	
// ------------------------------
	
	/** 
	 * Lists all visible users of (a) channel(s). 
	 * @param chan The channel the <code>NAMES</code> command is refering to.
	 */
	public void doNames(String chan) {
		send("NAMES "+ chan);
	}  
	
// ------------------------------
	
	/** 
	 * Sends a message to a person or a channel. 
	 * @param target The nickname or channel the message should be sent to.
	 * @param msg The message which should be transmitted.
	 */
	public void doPrivmsg(String target, String msg) {
		send("PRIVMSG "+ target +" :"+ msg);
	} 
	
// ------------------------------
	
	/** 
	 * Requests a Reply 324 for the modes of a given channel. 
	 * @param chan The channel the <code>MODE</code> request is refering to.
	 */
	public void doMode(String chan) {
		send("MODE "+ chan);
	}
	
// ------------------------------
	
	/** 
	 * Sends a mode to the server. <br />
	 * The first argument is a nickname (user-mode) or a channel (channel-mode). 
	 * <code>String mode</code> must contain the operators (+/-), the modes 
	 * (o/v/i/k/l/p/s/w) and the possibly values (nicks/banmask/limit/key). 
	 * @param target The nickname or channel of the user whose modes will be 
	 *               changed.
	 * @param mode The new modes.
	 */
	public void doMode(String target, String mode) {
		send("MODE "+ target +" "+ mode);
	} 
	
// ------------------------------
	
	/** 
	 * Changes the nickname. 
	 * @param nick The new nickname.
	 */
	public void doNick(String nick) {
		send("NICK "+ nick);
	}  
	
// ------------------------------
	
	/** 
	 * Notices a message to a person or a channel. 
	 * @param target The nickname or channel (group) the message should be 
	 *               sent to.
	 * @param msg The message which should be transmitted.
	 */
	public void doNotice(String target, String msg) {
		send("NOTICE "+ target +" :"+ msg);
	}
	
// ------------------------------
	
	/** 
	 * Parts from a given channel. 
	 * @param chan The channel you want to part from.
	 */
	public void doPart(String chan) {
		send("PART "+ chan);
	}
	
// ------------------------------
	
	/** 
	 * Parts from a given channel with a given parg-msg. 
	 * @param chan The channel you want to part from.
	 * @param msg The optional partmessage.
	 */
	public void doPart(String chan, String msg) {
		send("PART "+ chan +" :"+ msg);
	}
	
// ------------------------------
	
	/** 
	 * Quits from the IRC server with a quit-msg.
	 * @param ping The ping which was received in <code>onPing</code>. It's a 
	 *             <code>String</code>, because sometimes on some networks 
	 *             the server-hostname (for example splatterworld.quakenet.org) is
	 *             given as parameter which would throw an Exception if we 
	 *             gave the ping as long.
	 */
	public void doPong(String ping) {
		send("PONG :"+ ping);
	} 
	
// ------------------------------
	
	/** 
	 * Quits from the IRC server. 
	 * Calls the <code>disconnect</code>-method which does the work actually. 
	 * @see #isConnected() 
	 * @see #connect()
	 * @see #doQuit(String)
	 * @see #close()
	 */
	public void doQuit() {
		send("QUIT"); 
	} 
	
// ------------------------------
	
	/** 
	 * Quits from the IRC server with a quit-msg. 
	 * Calls the <code>disconnect</code>-method which does the work actually. 
	 * @param msg The optional quitmessage.
	 * @see #isConnected() 
	 * @see #connect()
	 * @see #doQuit()
	 * @see #close()
	 */
	public void doQuit(String msg) {
		send("QUIT :"+ msg); 
	}
	
// ------------------------------
	
	/** 
	 * Requests the topic of a chan. The topic is given in a numeric reply. 
	 * @param chan The channel which topic should be requested. 
	 */
	public void doTopic(String chan) {
		send("TOPIC "+ chan);
	}  
	
// ------------------------------
	
	/** 
	 * Changes the topic of a chan. 
	 * @param chan The channel which topic is changed. 
	 * @param topic The new topic.
	 */
	public void doTopic(String chan, String topic) {
		send("TOPIC "+ chan +" :"+ topic);
	}  
	
// ------------------------------
	
	/** 
	 * Requests information about users matching the given criteric, 
	 * for example a channel they are on. 
	 * @param criteric The criterics of the <code>WHO</code> query.
	 */
	public void doWho(String criteric) {
		send("WHO "+ criteric);
	}  
	
// ------------------------------
	
	/** 
	 * Requires information about an existing user. 
	 * @param nick The nickname of the user the query is refering to.
	 */
	public void doWhois(String nick) {
		send("WHOIS "+ nick);
	}  
	
// ------------------------------
	
	/** 
	 * Requires host-information about a user, who is not connected anymore. 
	 * @param nick The nickname of the user the query is refering to.
	 */
	public void doWhowas(String nick) {
		send("WHOWAS "+ nick);
	}
	
// ------------------------------
	
	/** 
	 * Requires host-information about up to 5 users which must be listed and 
	 * divided by spaces. 
	 * @param nick The nickname of the user the query is refering to.
	 */
	public void doUserhost(String nick) {
		send("USERHOST "+ nick);
	}
}
