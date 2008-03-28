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

package org.schwering.irc.manager.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.CtcpUtil;
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP DCC SEND request has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#dccSendReceived(CtcpDccSendEvent)
 */
public class CtcpDccSendEvent {
	private Connection connection;
	private User sender;
	private User destUser;
	private Channel destChannel;
	private String command;
	private String rest;
	private String file;
	private InetAddress addr;
	private int port;
	private long size;

	public CtcpDccSendEvent(Connection connection, User sender, User destUser,
			String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destUser = destUser;
		this.command = command;
		this.rest = rest;
		init(rest);
	}

	public CtcpDccSendEvent(Connection connection, User sender,
			Channel destChannel, String command, String rest) {
		this.connection = connection;
		this.sender = sender;
		this.destChannel = destChannel;
		this.command = command;
		this.rest = rest;
		init(rest);
	}
	
	private void init(String rest) {
		try {
			StringTokenizer st = new StringTokenizer(rest);
			st.nextToken(); // skip "SEND"
			String tmpfile = st.nextToken();
			if (tmpfile.charAt(0) == '\"') {
				do {
					tmpfile += " "+ st.nextToken();
				} while (tmpfile.charAt(tmpfile.length() - 1) != '\"');
				tmpfile = tmpfile.substring(1, tmpfile.length() - 1);
			}
			file = tmpfile;
			String tmphost = st.nextToken();
			if (tmphost.charAt(0) == '\"') {
				tmphost = tmphost.substring(1);
			}
			if (tmphost.charAt(tmphost.length() - 1) == '\"') {
				tmphost = tmphost.substring(0, tmphost.length() - 1);
			}
	  		addr = CtcpUtil.convertLongToInetAddress(Long.parseLong(tmphost));
	  		port = Integer.parseInt(st.nextToken());
	  		try {
	  			size = Integer.parseInt(st.nextToken());
	  		} catch (Exception exc) {
	  			size = 0;
	  		}
		} catch (Exception exc) {
			exc.printStackTrace();
			file = null;
			addr = null;
			port = -1;
			size = -1;
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public User getSender() {
		return sender;
	}
	
	public Channel getDestinationChannel() {
		return destChannel;
	}
	
	public User getDestinationUser() {
		return destUser;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getArguments() {
		return rest;
	}
	
	/**
	 * Returns the file name. If the DCC line is invalid, <code>null</code>
	 * is returned.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Returns the host. If the DCC line is invalid, <code>null</code>
	 * is returned.
	 */
	public InetAddress getAddress() {
		return addr;
	}

	/**
	 * Returns the port. If the DCC line was invalid, <code>-1</code> is
	 * returned.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns the file size in bytes or <code>0</code> if the size was not
	 * specified. If the DCC line was invalid, <code>-1</code> is returned.
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Connects to the sender and returns the <code>InputStream</code>
	 * of the socket.
	 */
	public InputStream accept() 
	throws NullPointerException, IOException, SecurityException {
		Socket sock = new Socket(addr, port);
		return sock.getInputStream();
	}
	
	/**
	 * Connects the the sender and writes the received data to 
	 * <code>dest</code>.
	 * @param dest The destination file (which is truncated).
	 * @return The number of transfered and written bytes.
	 * @throws NullPointerException If the DCC line is invalid or if some
	 * other NPE occurs.
	 * @throws IOException If some IO error occurs.
	 * @throws SecurityException If the application is not allowed to 
	 * connect or read/write the file.
	 */
	public long accept(File dest)
	throws NullPointerException, IOException, SecurityException {
		Socket sock = new Socket(addr, port);
		InputStream is = sock.getInputStream();
		FileOutputStream fos = new FileOutputStream(dest);
		int b;
		long cnt = 0;
		while ((b = is.read()) != -1) {
			fos.write(b);
			cnt++;
		}
		fos.close();
		is.close();
		return cnt;
	}
}
