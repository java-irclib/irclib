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

import java.io.PrintWriter;
import java.io.Writer;

import org.schwering.irc.lib.IRCTrafficLogger;

/**
 * A {@link PrintWriter} that sends all written character also to its {@link #trafficLogger}.
 */
public class LoggingWriter extends PrintWriter {
    final IRCTrafficLogger trafficLogger;

    /**
     * @param out the {@link Writer} to write to
     * @param trafficLogger the logger to notify about the written characters
     */
    public LoggingWriter(Writer out, IRCTrafficLogger trafficLogger) {
        super(out);
        this.trafficLogger = trafficLogger;
    }

    /**
     * @see java.io.PrintWriter#write(java.lang.String)
     */
    @Override
    public void write(String s) {
        String trimmedLine = s;
        if (s != null && s.endsWith("\r\n")) {
            trimmedLine = s.substring(0, s.length() - 2);
        }
        trafficLogger.out(trimmedLine);
        super.write(s);
    }

}