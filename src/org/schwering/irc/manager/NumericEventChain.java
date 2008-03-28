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

package org.schwering.irc.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a mechanism to create a buffer in an IRC event handler that
 * collects subsequent IRC events and then firesa manager event. 
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
abstract class NumericEventChain {
	private int[] starterNums;
	private int[] bodyNums;
	private int finalNum;
	private Map map = new HashMap();
	private long millis = 1500;
	
	public NumericEventChain(int bodyNum, int finalNum) {
		this(new int[] { bodyNum }, finalNum);
	}
	
	public NumericEventChain(int[] bodyNums, int finalNum) {
		this(bodyNums, bodyNums, finalNum);
	}
	
	public NumericEventChain(int starterNum, int bodyNum, int finalNum) {
		this(new int[] { starterNum }, new int[] { bodyNum }, finalNum);
	}
	
	public NumericEventChain(int[] starterNums, int bodyNum, int finalNum) {
		this(starterNums, new int[] { bodyNum }, finalNum);
	}
	
	public NumericEventChain(int starterNum, int[] bodyNums, int finalNum) {
		this(new int[] { starterNum }, bodyNums, finalNum);
	}
	
	public NumericEventChain(int[] starterNums, int[] bodyNums, int finalNum) {
		this.starterNums = starterNums;
		this.bodyNums = bodyNums;
		this.finalNum = finalNum;
	}

	public boolean numericReceived(int num, String val, String msg) {
		synchronized (map) {
			boolean retval = false;
			String id = getID(num, val, msg);
			Container container;
			if (contains(starterNums, num) && !map.containsKey(id)) {
				container = new Container(id, getInitObject(id));
				map.put(id, container);
				retval = true;
			}
			if (contains(bodyNums, num) && (container = (Container)map.get(id)) != null) {
				handle(container.getObject(), num, val, msg);
				retval = true;
			}
			if (finalNum == num && (container = (Container)map.remove(id)) != null) {
				container.interrupt();
				tryFire(container.getObject());
				retval = true;
			}
			if (retval) {
				System.out.println("handled by: "+ this.toString());
				millis++;
			}
			return retval;
		}
	}
	
	private void tryFire(Object obj) {
		try {
			fire(obj);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	protected abstract Object getInitObject(String id);
	
	protected abstract void handle(Object obj, int num, String val, String msg);
	
	protected abstract void fire(Object obj);
	
	/**
	 * Returns the second token of val+msg in lower case. It's converted
	 * to lower case, because servers tend to a WHOIS q with replies 
	 * with 'Q' and a final RPL_ENDOFWHOIS with 'q'. A cleaner way would be
	 * to tell the list to apply equalsIgnoreCase() instead of equals(), but
	 * that's not possible (in a straghtforward way).
	 */
	protected String getID(int num, String val, String msg) {
		return getFirstToken(skipFirstToken(val)).toLowerCase();
	}
	
	private static String skipFirstToken(String str) {
		str = str.trim();
		boolean found = false;
		for (int i = 0; i < str.length(); i++) {
			if (!found && Character.isWhitespace(str.charAt(i))) {
				found = true;
			} else if (found && !Character.isWhitespace(str.charAt(i))) {
				return str.substring(i);
			}
		}
		return "";
	}
	
	protected static String getFirstToken(String str) {
		str = str.trim();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return str.substring(0, i);
			}
		}
		return str;
	}
	
	private static boolean contains(int[] arr, int i) {
		for (int j = 0; arr != null && j < arr.length; j++) {
			if (i == arr[j]) {
				return true;
			}
		}
		return false;
	}
	
	private class Container extends Thread {
		private String id;
		private Object obj;
		
		public Container(String id, Object obj) {
			this.id = id;
			this.obj = obj;
			setPriority(Thread.MIN_PRIORITY);
			setName("NumericEventChainContainer"+starterNums[0]);
			setDaemon(true);
			start();
		}
		
		public Object getObject() {
			return obj;
		}
		
		public void run() {
			long m;
			do {
				m = millis;
				try {
					Thread.sleep(millis);
				} catch (InterruptedException exc) {
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			} while (millis != m);
			synchronized (map) {
				if (map.remove(id) != null) {
					tryFire(getObject());
				}
			}
		}
	}
}
