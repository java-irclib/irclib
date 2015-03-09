/*
 * IRClib -- A Java Internet Relay Chat library -- class CTCPCommand
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An enum of Client-to-client protocol (CTCP) commands.
 * See <a href="http://www.irchelp.org/irchelp/rfc/ctcpspec.html">http://www.irchelp.org/irchelp/rfc/ctcpspec.html</a>
 *
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public enum CTCPCommand {
    ACTION,
    DCC,
    SED,
    FINGER,
    VERSION,
    SOURCE,
    USERINFO,
    CLIENTINFO,
    ERRMSG,
    PING,
    TIME;

    private static final Map<String, CTCPCommand> FAST_LOOKUP;
    public static final int SHORTEST_COMMAND_LENGTH;
    public static final char QUOTE_CHAR = '\u0001';
    static {
        int shortestLength = Integer.MAX_VALUE;
        Map<String, CTCPCommand> fastLookUp = new HashMap<String, CTCPCommand>(64);
        CTCPCommand[] values = values();
        for (CTCPCommand value : values) {
            String name = value.name();
            int len = name.length();
            if (shortestLength > len) {
                shortestLength = len;
            }
            fastLookUp.put(name, value);
        }
        FAST_LOOKUP = Collections.unmodifiableMap(fastLookUp);
        SHORTEST_COMMAND_LENGTH = shortestLength;
    }

    /**
     * A {@link HashMap}-backed and {@code null}-tolerant alternative to
     * {@link #valueOf(String)}. The lookup is case-sensitive.
     *
     * @param command
     *            the command as a {@link String}
     * @return the {@link CTCPCommand} that corresponds to the given string
     *         {@code command} or {@code null} if no such command exists
     */
    public static CTCPCommand fastValueOf(String command) {
        return FAST_LOOKUP.get(command);
    }
}
