/**
 * IRClib - A Java Internet Relay Chat library
 * Copyright (C) 2006-2015 Christoph Schwering <schwering@gmail.com>
 * and/or other contributors as indicated by the @author tags.
 *
 * This library and the accompanying materials are made available under the
 * terms of the
 *  - GNU Lesser General Public License,
 *  - Apache License, Version 2.0 and
 *  - Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */
package org.schwering.irc.lib;

/**
 * An IRC user refered to in many IRC relies.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCUser {

    /**
     * @return The host of the line or {@code null}  if it's not given.
     */
    String getHost();

    /**
     * @return The nickname or the servername of the line or {@code null} if no nick is given
     */
    String getNick();

    /**
     * @return The username of the lineor {@code null} if it's not given.
     */
    String getUsername();

}
