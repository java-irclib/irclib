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

import java.util.Collections;
import java.util.List;

import org.schwering.irc.manager.Connection;

/**
 * Fired when a response of an LINKS request was received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see ConnectionListener#linksReceived(LinksEvent)
 */
public class LinksEvent {
	private Connection connection;
	private String mask;
	private List servers;
	private List serverInfos;
	private List hopCounts;

	public LinksEvent(Connection connection, String mask, List servers,
			List serverInfos, List hopCounts) {
		if (servers.size() != serverInfos.size() || serverInfos.size() != hopCounts.size()) {
			throw new IllegalArgumentException("Lists have different sizes");
		}
		this.connection = connection;
		this.mask = mask;
		this.servers = servers;
		this.serverInfos = serverInfos;
		this.hopCounts = hopCounts;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getMask() {
		return mask;
	}

	public List getServers() {
		return Collections.unmodifiableList(servers);
	}

	public List getServerInfos() {
		return Collections.unmodifiableList(serverInfos);
	}

	public List getHopCounts() {
		return Collections.unmodifiableList(hopCounts);
	}
	
	public int getCount() {
		return servers.size();
	}
	
	public String getServer(int i) {
		return (String)servers.get(i);
	}
	
	public String getServerInfo(int i) {
		return (String)serverInfos.get(i);
	}
	
	public String getHopCount(int i) {
		return (String)hopCounts.get(i);
	}
}
