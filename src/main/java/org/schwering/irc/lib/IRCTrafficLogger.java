/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCUtil
 * Copyright (C) 2002 - 2006 Christoph Schwering <schwering@gmail.com>
 *
 * This library and the accompanying materials are made available under the
 * terms of the
 *     - GNU Lesser General Public License,
 *     - Apache License, Version 2.0 and
 *     - Eclipse Public License v1.0.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY.
 */
package org.schwering.irc.lib;

/**
 * A logger for both ingoing and outgoing IRC messages that the IRC client sends and receives.
 *
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public interface IRCTrafficLogger {
    /** A {@link IRCTrafficLogger} implementation using {@code System.out} to output the traffic */
    IRCTrafficLogger SYSTEM_OUT = new IRCTrafficLogger() {
        @Override
        public void out(String line) {
            System.out.println("< " + line);
        }

        @Override
        public void in(String line) {
            System.out.println("> " + line);
        }

        @Override
        public void exception(Throwable e) {
            e.printStackTrace(System.out);
        }
    };

    /**
     * Called when a {@code line} is received from the IRC server.
     * @param line the line received from the server
     */
    void in(String line);

    /**
     * Called when the {@code line} is being sent to the IRC server.
     * @param line the line being sent to the server
     */
    void out(String line);

    void exception(Throwable e);

}