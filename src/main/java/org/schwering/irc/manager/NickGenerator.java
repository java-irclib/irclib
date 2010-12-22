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

/**
 * Interface to the end user or application when the chosen nickname is
 * not available at the time of connection establishment.
 * A nickname generator is needed because the server might ask for a
 * new nickname when we try to establish the connection. This question
 * for a new nickname must be answered somehow, and this task is
 * delivered to the nickname generator.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public interface NickGenerator {
	String createNewNick();
}
