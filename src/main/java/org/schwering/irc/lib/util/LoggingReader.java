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
package org.schwering.irc.lib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * A {@link BufferedReader} that sends all read character to its {@link #trafficLogger}.
 */
public class LoggingReader extends BufferedReader {
    final IRCTrafficLogger trafficLogger;

    /**
     * @param in the reader to read from.
     * @param trafficLogger a logger to notify about read characters
     */
    public LoggingReader(Reader in, IRCTrafficLogger trafficLogger) {
        super(in);
        this.trafficLogger = trafficLogger;
    }

    /**
     * @see java.io.BufferedReader#readLine()
     */
    @Override
    public String readLine() throws IOException {
        String line = super.readLine();
        trafficLogger.in(line);
        return line;
    }
}