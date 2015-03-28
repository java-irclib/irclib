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
 * A handler to be notified when there occurs any exception in {@link IRCConnection}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCExceptionHandler {

    /** A handler that just prints the given exception's stack trace to stderr. */
    IRCExceptionHandler PRINT_STACK_TRACE = new IRCExceptionHandler() {
        /**
         * @see org.schwering.irc.lib.IRCExceptionHandler#exception(org.schwering.irc.lib.IRCConnection,
         *      java.lang.Throwable)
         */
        @Override
        public void exception(IRCConnection connection, Throwable e) {
            e.printStackTrace();
        }
    };

    /**
     * Notified upon occurence of the given {@code exception} in the given {@code exception}.
     *
     * @param connection the connection in which the exception occured
     * @param exception the exception that occured
     */
    void exception(IRCConnection connection, Throwable exception);

}
