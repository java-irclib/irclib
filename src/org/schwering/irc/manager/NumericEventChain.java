package org.schwering.irc.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class NumericEventChain {
	private int[] starterNums;
	private int[] bodyNums;
	private int finalNum;
	private Map map = Collections.synchronizedMap(new HashMap());
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
	
	protected String getID(int num, String val, String msg) {
		return getFirstToken(getFirstToken(val));
	}
	
	protected static String getFirstToken(String str) {
		str = str.trim();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return str.substring(0, i-1);
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
			start();
		}
		
		public Object getObject() {
			return obj;
		}
		
		public void run() {
			try {
				Thread.sleep(millis);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			System.out.println("expired, firing");
			map.remove(id);
			tryFire(getObject());
		}
	}
}
