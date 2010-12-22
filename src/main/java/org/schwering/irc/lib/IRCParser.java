/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCParser
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

/**
 * Parses a line sent from the IRC server.
 * <p>
 * Note: Probably this class is unimportant for you. It's used by the
 * <code>IRCConnection</code> to parse incoming lines. Nevertheless I declared
 * it as <code>public</code> because you might want to use it to parse IRC 
 * command-shortcuts like <code>MSG</code> instead of <code>PRIVMSG</code> in 
 * your client.<br />
 * The following text goes on with the description of the class and what it
 * does.
 * <p>
 * According with RFC1459 it divides the line into a prefix, a command and 
 * its parameters.
 * <p>
 * The prefix is only given if a line starts with a <code>:</code> (colon) 
 * and is used to indicate from where the line is send.
 * <p>
 * The next word in the line (if no prefix exists it is the first, else the 
 * second word) is the command.
 * The command is eiter a valid IRC command or a three-digit number which 
 * represents a numeric reply or a numeric error.
 * <p>
 * The parameters are divided into a middle and a trailing part. 
 * In the middle part one word means one parameter while the trailing part is 
 * just one parameter independent from the amount of words in it. 
 * If there is a  "<code>&nbsp;:</code>" (space+colon) in the line, this point 
 * means the beginning of the trailing.
 * If there is no such space+colon, the trailing is just the last word. 
 * All words behind the space+colon mean just one parameter.
 * If there is only one parameter given (the parameter is the first, the last 
 * and the only one), the parameter is available as trailing (with 
 * <code>getTrailing</code>), not as middle!
 * <p>
 * One line may have up to 15 parameters. Therefore up to 14 are middle and 
 * one is the trailing.
 * <p>
 * The line may have up to 510 characters plus the CR-LF (carriage return - 
 * line feed) which trails the incoming line. 
 * <p>
 * The following extract of the RFC1459 shows the message format in BNF:
 * <blockquote cite="RFC1459"><code>
 * &lt;message&gt;&nbsp;&nbsp;::=
 * [':' &lt;prefix&gt; &lt;SPACE&gt; ] &lt;command&gt; &lt;params&gt; 
 * &lt;crlf&gt; <br />
 * &lt;prefix&gt;&nbsp;&nbsp;&nbsp;::=
 * &lt;servername&gt; | &lt;nick&gt; 
 * [ '!' &lt;username&gt; ] [ '@' &lt;host&gt; ]
 * <br />
 * &lt;command&gt;&nbsp;&nbsp;::=
 * &lt;letter&gt; { &lt;letter&gt; } | &lt;number&gt; &lt;number&gt; 
 * &lt;number&gt; <br />
 * &lt;SPACE&gt;&nbsp;&nbsp;&nbsp;&nbsp;::=
 * ' ' { ' ' }<br />
 * &lt;params&gt;&nbsp;&nbsp;&nbsp;::=
 * &lt;SPACE&gt; [ ':' &lt;trailing&gt; | &lt;middle&gt; &lt;params&gt; ] <br />
 * &lt;middle&gt;&nbsp;&nbsp;&nbsp;::=
 * &lt;Any *non-empty* sequence of octets not including SPACE or NUL or CR or 
 * LF, the first of which may not be ':'&gt; <br />
 * &lt;trailing&gt;&nbsp;::=
 * &lt;Any, possibly *empty*, sequence of octets not including NUL or CR or 
 * LF&gt; <br />
 * &lt;crlf&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;::=
 * CR LF<br />
 * </code></blockquote>
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 3.22
 * @see IRCConnection
 */
public class IRCParser {
	
	/** 
	 * The <code>StringBuffer</code> contains the line which was analyzed. 
	 */
	private StringBuffer buf;
	
	/**
	 * The length of the line. 
	 */
	private int len;
	
	/**
	 * The prefix, which is parsed out in the constructor. 
	 */
	private String prefix;
	
	/**
	 * The command, which is parsed out in the constructor. 
	 */
	private String command;
	
	/**
	 * The middle, which is parsed out in the constructor. 
	 */
	private String middle;
	
	/**
	 * The trailing, which is parsed out in the constructor. The trailing is the 
	 * part behind the colon (<code>:</code>) or the last parameter. 
	 */
	private String trailing;
	
	/**
	 * The parameters' array. It's not initialized in the constructor because of 
	 * rare access. The methods which use and need this parameter-array 
	 * (getParametersCount, getParameter, getParameterFrom etc.) initialize this 
	 * array by calling initParameters, if the array isn't initialized yet.
	 */
	private String[] parameters;
	
// ------------------------------
	
	/**
	 * Parses the line after erasing all mIRC color codes.
	 * This constructor is a shorthand for <code>IRCParser(line, false)</code>.
	 * @param line The line which will be parsed. 
	 */
	public IRCParser(String line) {
		this(line, false);
	}
	
// ------------------------------
	
	/**
	 * The main constructor. 
	 * Parses prefix, command, middle and trailing.
	 * @param line The line which will be parsed. 
	 * @param colorsEnabled If <code>false</code>, mIRC color codes are parsed out
	 *                      by using <code>IRCUtil.parseColors</code> method. 
	 */
	public IRCParser(String line, boolean colorsEnabled) {
		int index = 0;
		int trail;
		
		buf = new StringBuffer(line);
		if (!colorsEnabled)
			buf = IRCUtil.parseColors(buf);
		len = buf.length();
		
		// prefix
		if (buf.charAt(0) == ':') {
			prefix = buf.substring(1, (index = indexOf(' ', index)));
			index++;
		}
		
		while (buf.charAt(index) == ' ')
			index++;
		
		// command
		command = buf.substring(index, ((index = indexOf(' ', index)) != -1) 
				? index : (index = len));
		
		while (index < len && buf.charAt(index) == ' ')
			index++;
		index--;
		
		// middle & trailing
		if ((trail = indexOf(" :", index)) != -1)
			trailing = buf.substring(trail + 2, len);
		else if ((trail = lastIndexOf(' ')) != -1 && trail >= index) 
			trailing = buf.substring(trail + 1, len);
		middle = (index < trail) ? buf.substring(index + 1, trail) : "";
		
		// save
		this.prefix = (prefix != null) ?  prefix : "";
		this.command = (command != null) ?  command : "";
		this.middle = (middle != null) ?  middle : "";
		this.trailing = (trailing != null) ?  trailing : "";
	}
	
// ------------------------------
	
	/**
	 * Searches for a char in the <code>StringBuffer buf</code> from a given index
	 * and returns its index.
	 * @param c The char to search.
	 * @param i The index the method will start searching at.
	 * @return The index of the searched char. 
	 */
	private int indexOf(int c, int i) {
		while (i < len)
			if (buf.charAt(i++) == c)
				return --i;
		return -1;
	}
	
// ------------------------------
	
	/**
	 * Searches for a string in the <code>StringBuffer buf</code> from a given 
	 * index and returns its beginning index.
	 * @param str The string to search.
	 * @param i The index the method will start searching at.
	 * @return The index of the searched string.
	 */
	private int indexOf(String str, int i) {
		int sublen = str.length();
		int index = -1;
		int j;
		for ( ; i < len; i++) 
			for (index = i, j = 0; i < len && j < sublen; i++, j++)
				if (buf.charAt(i) != str.charAt(j))
					break;
				else if (j + 1 == sublen) 
					return index;
		return -1;
	}
	
// ------------------------------
	
	/**
	 * Searches for a given char in the <code>StringBuffer buf</code>. <br />
	 * It starts at the end. <br />
	 * Note: The method expects a character which is not <code>c</code> before
	 * it can return an index. Thus in a string like "<code>nick moor&nbsp;&nbsp;
	 * &nbsp;&nbsp;</code>" with four trailing spaces 
	 * <code>lastIndexOf(' ')</code> does not return the the index of the last 
	 * space. It first waits for characters which are not a space (r, o, o, m) and
	 * then returns the index of the next space: the space between 
	 * <code>nick</code> and <code>moor</code>. By this, also in lines with 
	 * trailing whitespace the trailing-part is correctly recognized.
	 * @param c The char to search.
	 * @return The last index of the searched char.
	 */
	private int lastIndexOf(int c) {
		int i = len;
		boolean ok = false;
		while (i > 0)
			if (buf.charAt(--i) != c)
				ok = true;
			else if (ok)
				return i;
		return -1;
	}
	
// ------------------------------
	
	/**
	 * Initializes the <code>parameters[]</code>. 
	 * This method is called by <code>getParam</code>, <code>getParamFrom</code> 
	 * and <code>getParamTo</code>, if the <code>parameters[]</code> aren't 
	 * initialized yet.<br />
	 * The method splits the <code>middle</code> into all words using and appends 
	 * the trailing as last parameter. It uses the <code>IRCUtil.split</code>
	 * method.
	 */
	private void initParameters() {
		parameters = IRCUtil.split(middle, ' ', trailing);
	}
	
// ------------------------------
	
	/**
	 * Returns the line's prefix. A prefix is the part which contains information
	 * about the sender of the line. If no prefix is set, <code>""</code> is 
	 * returned; but in fact there's always a prefix.
	 * @return The line's prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	
// ------------------------------
	
	/**
	 * Returns the line's command.
	 * @return The line's command.
	 */
	public String getCommand() {
		return command;
	}
	
// ------------------------------
	
	/**
	 * Returns the line's middle.
	 * @return The line's middle.
	 */
	public String getMiddle() {
		return middle;
	}
	
// ------------------------------
	
	/**
	 * Returns the line's trailing.
	 * @return The line's trailing.
	 */
	public String getTrailing() {
		return trailing;
	}
	
// ------------------------------
	
	/**
	 * Returns the unparsed line. It looks exacttly as the server sent it, but
	 * if colors are disabled and therefore already parsed out by 
	 * IRCUtil.parseColors, the colors are not included in here.
	 * @return The line.
	 */
	public String getLine() {
		return buf.toString();
	}
	
// ------------------------------
	
	/**
	 * Returns the line's parameters which consists of the middle and the 
	 * trailing.
	 * @return The line's parameters.
	 */
	public String getParameters() {
		return middle + 
		((middle.length() != 0 && trailing.length() != 0) ? " " : "") + 
		trailing;
	}
	
// ------------------------------
	
	/** 
	 * Returns the nickname of the person who sent the line 
	 * or the servername of the server which sent the line. <br />
	 * It is found in the prefix which always looks like that:<br />
	 * <code>
	 * &lt;servername&gt; | &lt;nick&gt; 
	 * [ '!' &lt;username&gt; ] [ '@' &lt;host&gt; ]
	 * </code><br /><br />
	 * If no prefix is given in the whole line, <code>null</code> is returned.
	 * <br /><br />
	 * <b>Note:</b> This method is totally equal to <code>getServername</code>!
	 * <br />
	 * <b>Note:</b> There is also the method <code>getUser</code> which returns
	 * an <code>IRCUser</code> object which holds the nickname, username and host.
	 * By the way, the <code>getUser</code> uses the <code>getNick</code>, 
	 * <code>getUsername</code> and <code>getHost</code> methods to create this
	 * object.
	 * @return The nickname or the servername of the line. If no prefix is given,
	 *         <code>null</code> is returned.
	 * @see #getServername()
	 * @see #getUsername()
	 * @see #getHost()
	 * @see #getUser()
	 */
	public String getNick() {
		int i = prefix.indexOf('!');
		if (i != -1 || (i = prefix.indexOf('@')) != -1)
			return prefix.substring(0, i); 
		return (prefix.length() != 0) ? prefix : null;
	}
	
// ------------------------------
	
	/** 
	 * Returns the servername of the server which sent the line 
	 * or the nickname of the person who sent the line. <br />
	 * It is found in the prefix which always looks like that:<br />
	 * <code>
	 * &lt;servername&gt; | &lt;nick&gt; 
	 * [ '!' &lt;username&gt; ] [ '@' &lt;host&gt; ]
	 * </code><br /><br />
	 * If no prefix is given in the whole line, <code>null</code> is returned.
	 * <br /><br />
	 * <b>Note:</b> This method is totally equal to <code>getNick</code>!
	 * <br />
	 * <b>Note:</b> There is also the method <code>getUser</code> which returns
	 * an <code>IRCUser</code> object which holds the nickname, username and host.
	 * By the way, the <code>getUser</code> uses the <code>getNick</code>, 
	 * <code>getUsername</code> and <code>getHost</code> methods to create this
	 * object.
	 * @return The servername or the nickname of the line. If no prefix is given,
	 *         <code>null</code> is returned.
	 * @see #getNick()
	 * @see #getUser()
	 */
	public String getServername() {
		return getNick();
	}
	
// ------------------------------
	
	/** 
	 * Returns the username of the person who sent the line.<br />
	 * It is found in the prefix which always looks like that:<br />
	 * <code>
	 * &lt;servername&gt; | &lt;nick&gt; 
	 * [ '!' &lt;username&gt; ] [ '@' &lt;host&gt; ]
	 * </code><br /><br />
	 * If the username is not specified, this method returns <code>null</code>.
	 * <br />
	 * <b>Note:</b> There is also the method <code>getUser</code> which returns
	 * an <code>IRCUser</code> object which holds the nickname, username and host.
	 * By the way, the <code>getUser</code> uses the <code>getNick</code>, 
	 * <code>getUsername</code> and <code>getHost</code> methods to create this
	 * object.
	 * @return The username of the line; <code>null</code> if it's not given.
	 * @see #getNick()
	 * @see #getHost()
	 * @see #getUser()
	 */
	public String getUsername() {
		int i = prefix.indexOf('!') + 1;
		if (i != 0) {
			int j = prefix.indexOf('@', i); 
			return prefix.substring(i, (j != -1) ? j : prefix.length()); 
		}
		return null;
	}
	
// ------------------------------
	
	/** 
	 * Returns the host of the person who sent the line.<br />
	 * It is found in the prefix which always looks like that:<br />
	 * <code>
	 * &lt;servername&gt; | &lt;nick&gt; 
	 * [ '!' &lt;username&gt; ] [ '@' &lt;host&gt; ]
	 * </code><br /><br />
	 * If the host is not specified, this method returns <code>null</code>.
	 * <br />
	 * <b>Note:</b> There is also the method <code>getUser</code> which returns
	 * an <code>IRCUser</code> object which holds the nickname, username and host.
	 * By the way, the <code>getUser</code> uses the <code>getNick</code>, 
	 * <code>getUsername</code> and <code>getHost</code> methods to create this
	 * object.
	 * @return The host of the line; <code>null</code> if it's not given.
	 * @see #getNick()
	 * @see #getUsername()
	 * @see #getUser()
	 */
	public String getHost() {
		int i = prefix.indexOf('@') + 1;
		if (i != 0)
			return prefix.substring(i, prefix.length()); 
		return null;
	}
	
// ------------------------------
	
	/** 
	 * Returns a new <code>IRCUser</code> object.
	 * This method is equal to <code>new IRCUser(IRCParser.getNick(), 
	 * IRCParser.getUsername(), IRCParser.getHost())</code>. See those methods to
	 * learn which value they return if they are not set. 
	 * @return A new <code>IRCUser</code> object with exactly those values which
	 *         are returned by the <code>getNick</code>, <code>getUsername</code> 
	 *         and <code>getHost</code> methods.
	 * @see #getNick()
	 * @see #getUsername()
	 * @see #getHost()
	 */
	public IRCUser getUser() {
		return new IRCUser(getNick(), getUsername(), getHost());
	}
	
// ------------------------------
	
	/** 
	 * Gets count of parameters.
	 * If <code>parameters</code> isn't initialized yet, it calls 
	 * <code>initParameters</code> to do that.
	 * @return The number of parameters. 
	 */
	public int getParameterCount() {
		if (parameters == null) 
			initParameters();
		return parameters.length;
	}
	
// ------------------------------
	
	/** 
	 * Get one parameter of the line.
	 * If <code>parameters</code> isn't initialized yet, it calls 
	 * <code>initParameters</code> to do that.
	 * @param i The index of the parameter you want to get. The index starts with 
	 *          1 and not with 0.
	 * @return The <code>i</code>th parameter. If <code>i</code> is out of bounds,
	 *         <code>""</code> is returned. 
	 */
	public String getParameter(int i) {
		if (parameters == null) 
			initParameters();
		--i;
		if (i >= 0 && i < parameters.length)
			return parameters[i];
		else
			return "";
	}
	
// ------------------------------
	
	/** 
	 * Grabs the line's parameters from the <code>i</code>th to the last 
	 * parameter (including the <code>i</code>th).
	 * If <code>parameters</code> isn't initialized yet, it calls 
	 * <code>initParameters</code> to do that.
	 * @param i The index of the first parameter you want to get. 
	 * @return All parameters behind another beginning at the <code>i</code>th. 
	 *         If <code>i</code> is out of bounds, <code>""</code> is returned. 
	 */
	public String getParametersFrom(int i) {
		if (parameters == null) 
			initParameters();
		StringBuffer params = new StringBuffer();
		for (i--; i < parameters.length; i++)
			params.append(parameters[i] +" ");
		return params.toString();
	}
	
// ------------------------------
	
	/** 
	 * Grabs the line's parameters from the first to the <code>i</code>th 
	 * parameters (including the <code>i</code>th).
	 * If <code>parameters</code> isn't initialized yet, it calls 
	 * <code>initParameters</code> to do that.
	 * @param i The index of the last parameter you want to get. 
	 * @return All parameters beginning at the first and ending at the 
	 *         <code>i</code>th.  If <code>i</code> is out of bounds, 
	 *         <code>""</code> is returned. 
	 */
	public String getParametersTo(int i) {
		if (parameters == null) 
			initParameters();
		StringBuffer params = new StringBuffer();
		int max = (i < parameters.length) ? i : parameters.length;
		for (i = 0; i < max; i++)
			params.append(parameters[i] +" ");
		return params.toString();
	}
	
// ------------------------------
	
	/**
	 * Generates a <code>String</code> with some information about the instance of
	 * <code>IRCParser</code>.<br />
	 * Its format is: <code>classname[prefix,command,middle,trailing]</code>.
	 * @return A <code>String</code> with information about the instance.
	 */
	public String toString() {
		return getClass().getName() +"["+ prefix +","+ command +","+ middle +","+ 
		trailing +"]";
	}
}
