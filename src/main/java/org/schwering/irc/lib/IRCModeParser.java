/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCModeParser
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
 * Parses channel-modes.
 * <p>
 * An instance of this class is an argument of the <code>{@link 
 * org.schwering.irc.lib.IRCEventListener#onMode(String chan, IRCUser user,
 * IRCModeParser modeParser)}</code>.
 * It's intended to help the programmer to work with the modes.
 * <p>
 * Channelmodes are:
 * <ul>
 * <li> +/- o nick </li>
 * <li> +/- v nick </li>
 * <li> +/- b banmask </li>
 * <li> +/- l limit </li>
 * <li> +/- k key </li>
 * <li> +/- p </li>
 * <li> +/- s </li>
 * <li> +/- i </li>
 * <li> +/- t </li>
 * <li> +/- n </li>
 * <li> +/- m </li>
 * </ul>
 * <p>
 * These are all channel-modes defined in RFC1459. Nevertheless, most 
 * networks provide more channel-modes. This class can handle all modes; it's 
 * not restricted to the rights defined in RFC1459.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @version 1.22
 * @see IRCEventListener
 */
public class IRCModeParser {
	
	/** 
	 * Represents the operators, modes and nicks as they were sent from the IRC 
	 * server. 
	 */
	private String line;
	
	/** 
	 * Contains pluses (<code>+</code>) and minuses (<code>-</code>) which show 
	 * if the mode is taken or given. 
	 */
	private char[] operatorsArr;
	
	/** 
	 * This array contains the modes that are set with the operator of the 
	 * <code>operatorsArr</code>-array. *
	 */
	private char[] modesArr;
	
	/** 
	 * Represents the parsed nicks, hostnames, limits or keys in an array of 
	 * Strings. 
	 */
	private String[] argsArr;
	
// ------------------------------
	
	/** 
	 * Analyzes the modes and parses them into the parts operators (<code>+</code>
	 * or <code>-</code>), modes (one character) and optional arguments (one 
	 * word or number).
	 * @param line The modes and the arguments; nothing more.
	 */
	public IRCModeParser(String line) {
		line = line.trim();
		this.line = line;
		int index = line.indexOf(' ');
		if (index >= 2) { // with arguments
			String modes = line.substring(0, index);
			String args  = line.substring(index + 1);
			parse(modes, args); // call real constructor. 
		} else if (line.length() >= 2) { // no arguments
			String modes = line;
			String args = "";
			parse(modes, args);
		} else { // nothing
			argsArr = new String[0];
			operatorsArr = new char[0];
			modesArr = new char[0];
		}
	}
	
// ------------------------------
	
	/** 
	 * Analyzes the modes and parses them into the parts operators (<code>+</code>
	 * or <code>-</code>), modes (one character) and optional arguments (one 
	 * word or number).
	 * @param modes The modes (for example <code>+oo+m-v</code>).
	 * @param args The modes' arguments (for example <code>Heinz Hans 
	 *             Thomas</code>).
	 */
	public IRCModeParser(String modes, String args) {
		line = modes +" "+ args;
		parse(modes, args);
	}
	
// ------------------------------
	
	/** 
	 * Parses the modes into two <code>char</code>-arrays and one 
	 * <code>String</code>-array. <br />
	 * The first one contains the operator of the mode (<code>+</code> or 
	 * </code>-</code>) and the second one the mode (<code>w</code>, 
	 * <code>i</code>, <code>s</code>, <code>o</code> or any other mode). 
	 * The <code>String[]</code> contains the nicknames. 
	 * @param modes The modes (for example <code>+oo+m-v</code>).
	 * @param args The modes' arguments (for example <code>Heinz Hans 
	 *             Thomas</code>).
	 */
	private void parse(String modes, String args) {
		String[] argsTmp = IRCUtil.split(args, ' ');
		int modesLen = modes.length();
		int modesCount = getModesCount(modes);
		char c;
		char operator = '+'; // any value cause it must be initialized
		operatorsArr = new char[modesCount];
		modesArr = new char[modesCount];
		argsArr = new String[modesCount];
		// parse and fill the arrays
		for (int i = 0, j = 0, n = 0; i < modesLen; i++) {
			c = modes.charAt(i);
			if (c == '+' || c == '-') {
				operator = c;
			} else {
				// add the operator (which was found earlier in the loop)
				operatorsArr[n] = operator; 
				modesArr[n] = c; // add the mode
				if ((c == 'o' || c == 'v' || c == 'b' || c == 'k') // come with arg
						|| (c == 'l' && operator == '+')) { // key comes with arg if '+'
					argsArr[n] = (j < argsTmp.length) ? argsTmp[j++] : "";
				} else {
					argsArr[n] = ""; // null if mode has no argument (for example m, p, s)
				}
				n++; // increase n, not i. n is used to fill the arrays
			}
		}
	}
	
// ------------------------------
	
	/**
	 * Returns the amount of modes in the string. This is done by counting all 
	 * chars which are not <code>+</code> or <code>-</code>.
	 * @param modes The modes which are to analyze.
	 * @return The count of modes without operators.
	 */
	private int getModesCount(String modes) {
		int count = 0;
		for (int i = 0, c, len = modes.length(); i < len; i++)
			if ((c = modes.charAt(i)) != '+' && c != '-')
				count++;
		return count;
	}
	
// ------------------------------
	
	/** 
	 * Returns count of modes. 
	 * @return The count of modes.
	 * @see #getOperatorAt(int)
	 * @see #getModeAt(int)
	 * @see #getArgAt(int)
	 */
	public int getCount() {
		return operatorsArr.length;
	}
	
// ------------------------------
	
	/** 
	 * Returns the operator (<code>+</code> or <code>-</code>) of a given index.
	 * @param i The index of the operator you want to get. The index starts 
	 *          with <code>1</code> and not with <code>0</code>.
	 * @return The operator at the given index (<code>+</code> or <code>-</code>).
	 * @see #getCount()
	 * @see #getModeAt(int)
	 * @see #getArgAt(int)
	 */
	public char getOperatorAt(int i) {
		return operatorsArr[i - 1];
	}
	
// ------------------------------
	
	/** 
	 * Returns the mode (for example <code>o</code>, <code>v</code>, 
	 * <code>m</code>, <code>i</code>) of a given index. 
	 * @param i The index of the mode you want to get. The index starts with 
	 *          <code>1</code> and not with <code>0</code>.
	 * @return The mode of the given index (for example <code>o</code>, 
	 *         <code>v</code>, <code>m</code>, <code>i</code>)
	 * @see #getCount()
	 * @see #getOperatorAt(int)
	 * @see #getArgAt(int)
	 */
	public char getModeAt(int i) {
		return modesArr[i - 1];
	}
	
// ------------------------------
	
	/** 
	 * Returns the nick of a given index. 
	 * @param i The index of the argument you want to get. The index starts with 
	 *          <code>1</code> and not with <code>0</code>.
	 * @return The argument you requested. It's <code>""</code> if there's no 
	 *         argument at this index (for example <code>+m</code> for moderated 
	 *         has never an argument).
	 * @see #getCount()
	 * @see #getOperatorAt(int)
	 * @see #getModeAt(int)
	 */
	public String getArgAt(int i) {
		return argsArr[i - 1];
	}
	
// ------------------------------
	
	/** 
	 * Returns the line as it was sent from the IRC server.
	 * The line contains the the operators, the modes and the nicknames, but not 
	 * the channel or the nickname who executed the MODE command! 
	 * @return The line which was set as argument when the parser was initialized.
	 */
	public String getLine() {
		return line;
	}
	
// ------------------------------
	
	/**
	 * Generates a <code>String</code> with some information about the instance of
	 * <code>IRCModeParser</code>.
	 * Its format is: <code>classname[line]</code>.
	 * @return A <code>String</code> with information about the instance.
	 */
	public String toString() {
		return getClass().getName() +"["+ getLine() +"]";
	}
}
