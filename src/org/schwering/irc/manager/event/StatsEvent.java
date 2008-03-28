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

import org.schwering.irc.manager.Connection;

/**
 * Fired when a response of a STATS request was received.
 * <p>
 * Note: (1) The complete STATS handling is untested, because I haven't found
 * a server that supports STATS. (2) The COMMAND replies can't be accessed
 * in this object yet (the handling in BasicListener is prepared).
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#statsReceived(StatsEvent)
 */
public class StatsEvent {
	private Connection connection;
	private String linkName;
	private int queuedSendData;
	private int sentMessages;
	private int sentBytes;
	private int receivedMessages;
	private int receivedBytes;
	private long millisOpen;
	private String uptime;
	private String cLineHost, cLineName, cLineClass; int cLinePort;
	private String nLineHost, nLineName, nLineClass; int nLinePort;
	private String iLineHost, iLineHost2, iLineClass; int iLinePort;
	private String kLineHost, kLineUsername, kLineClass; int kLinePort;
	private String lLineHostmask, lLineServername; int lLineMaxDepth;
	private String yLineClass; int yLinePingFrequency, yLineConnectionFrequency, yLineMaxQueuedSendData;
	private String oLineHostMask, oLineName;
	private String hLineHostMask, hLineServerName;
	
	public StatsEvent(Connection connection, String linkName,
			int queuedSendData, int sentMessages, int sentBytes, int receivedMessages,
			int receivedBytes, long millisOpen, String uptime, String lineHost,
			String lineName, String lineClass, int linePort, String lineHost2,
			String lineName2, String lineClass2, int linePort2,
			String lineHost3, String lineHost22, String lineClass3,
			int linePort3, String lineHost4, String lineUsername,
			String lineClass4, int linePort4, String lineHostmask,
			String lineServername, int lineMaxDepth, String lineClass5,
			int linePingFrequency, int lineConnectionFrequency,
			int lineMaxQueuedSendData, String lineHostMask2, String lineName3,
			String lineHostMask3, String lineServerName2) {
		super();
		this.connection = connection;
		this.linkName = linkName;
		this.queuedSendData = queuedSendData;
		this.sentMessages = sentMessages;
		this.sentBytes = sentBytes;
		this.receivedMessages = receivedMessages;
		this.receivedBytes = receivedBytes;
		this.millisOpen = millisOpen;
		this.uptime = uptime;
		cLineHost = lineHost;
		cLineName = lineName;
		cLineClass = lineClass;
		cLinePort = linePort;
		nLineHost = lineHost2;
		nLineName = lineName2;
		nLineClass = lineClass2;
		nLinePort = linePort2;
		iLineHost = lineHost3;
		iLineHost2 = lineHost22;
		iLineClass = lineClass3;
		iLinePort = linePort3;
		kLineHost = lineHost4;
		kLineUsername = lineUsername;
		kLineClass = lineClass4;
		kLinePort = linePort4;
		lLineHostmask = lineHostmask;
		lLineServername = lineServername;
		lLineMaxDepth = lineMaxDepth;
		yLineClass = lineClass5;
		yLinePingFrequency = linePingFrequency;
		yLineConnectionFrequency = lineConnectionFrequency;
		yLineMaxQueuedSendData = lineMaxQueuedSendData;
		oLineHostMask = lineHostMask2;
		oLineName = lineName3;
		hLineHostMask = lineHostMask3;
		hLineServerName = lineServerName2;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getLinkName() {
		return linkName;
	}

	public int getQueuedSendData() {
		return queuedSendData;
	}

	public int getSentMessages() {
		return sentMessages;
	}
	
	public int getSentBytes() {
		return sentBytes;
	}

	public int getReceivedMessages() {
		return receivedMessages;
	}

	public int getReceivedBytes() {
		return receivedBytes;
	}

	public long getMillisOpen() {
		return millisOpen;
	}

	public String getOpenTime() {
		if (millisOpen == -1) {
			return null;
		}
		
		long x = millisOpen;
		final long SECOND = 1000;
		final long MINUTE = 60*SECOND;
		final long HOUR = 60*MINUTE;
		final long DAY = 24*HOUR;
		int days = (int)(x / DAY);
		x -= days * DAY;
		int hours = (int)(x / HOUR);
		x -= hours * HOUR;
		int minutes = (int)(x / MINUTE);
		x -= minutes * MINUTE;
		int seconds = (int)(x / SECOND);
		StringBuffer time = new StringBuffer();
		if (days > 0) {
			time.append(days +" days, ");
		}
		if (days > 0 || hours > 0) {
			time.append(hours +" hours, ");
		}
		if (days > 0 || hours > 0 || minutes > 0) {
			time.append(minutes +" minutes, ");
		}
		if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
			time.append(seconds +" seconds");
		}
		return time.toString();
	}
	
	public String getUptime() {
		return uptime;
	}

	public String getCLineHost() {
		return cLineHost;
	}

	public String getCLineName() {
		return cLineName;
	}

	public String getCLineClass() {
		return cLineClass;
	}

	public int getCLinePort() {
		return cLinePort;
	}

	public String getNLineHost() {
		return nLineHost;
	}

	public String getNLineName() {
		return nLineName;
	}

	public String getNLineClass() {
		return nLineClass;
	}

	public int getNLinePort() {
		return nLinePort;
	}

	public String getILineHost() {
		return iLineHost;
	}

	public String getILineHost2() {
		return iLineHost2;
	}

	public String getILineClass() {
		return iLineClass;
	}

	public int getILinePort() {
		return iLinePort;
	}

	public String getKLineHost() {
		return kLineHost;
	}

	public String getKLineUsername() {
		return kLineUsername;
	}

	public String getKLineClass() {
		return kLineClass;
	}

	public int getKLinePort() {
		return kLinePort;
	}

	public String getLLineHostmask() {
		return lLineHostmask;
	}

	public String getLLineServername() {
		return lLineServername;
	}

	public int getLLineMaxDepth() {
		return lLineMaxDepth;
	}

	public String getYLineClass() {
		return yLineClass;
	}

	public int getYLinePingFrequency() {
		return yLinePingFrequency;
	}

	public int getYLineConnectionFrequency() {
		return yLineConnectionFrequency;
	}

	public int getYLineMaxQueuedSendData() {
		return yLineMaxQueuedSendData;
	}

	public String getOLineHostMask() {
		return oLineHostMask;
	}

	public String getOLineName() {
		return oLineName;
	}

	public String getHLineHostMask() {
		return hLineHostMask;
	}

	public String getHLineServerName() {
		return hLineServerName;
	}

}
