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

import org.schwering.irc.lib.util.IRCModeParser;

/**
 * A empty implementation if {@link IRCEventListener}.
 *
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @see IRCEventListener
 */
public class IRCEventAdapter implements IRCEventListener {

    /**
     * The default and only constructor does nothing.
     */
    public IRCEventAdapter() {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onRegistered()
     */
    public void onRegistered() {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onDisconnected()
     */
    public void onDisconnected() {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onError(String)
     */
    public void onError(String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onError(int, String)
     */
    public void onError(int num, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onInvite(String, IRCUser, String)
     */
    public void onInvite(String chan, IRCUser user, String passiveNick) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onJoin(String, IRCUser)
     */
    public void onJoin(String chan, IRCUser user) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onKick(String, IRCUser, String, String)
     */
    public void onKick(String chan, IRCUser user, String passiveNick,
            String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onMode(String, IRCUser, IRCModeParser)
     */
    public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onMode(IRCUser, String, String)
     */
    public void onMode(IRCUser user, String passiveNick, String mode) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onNick(IRCUser, String)
     */
    public void onNick(IRCUser user, String newNick) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onNotice(String, IRCUser, String)
     */
    public void onNotice(String target, IRCUser user, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onPart(String, IRCUser, String)
     */
    public void onPart(String chan, IRCUser user, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onPing(String)
     */
    public void onPing(String ping) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onPrivmsg(String, IRCUser, String)
     */
    public void onPrivmsg(String target, IRCUser user, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onQuit(IRCUser, String)
     */
    public void onQuit(IRCUser user, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onReply(int, String, String)
     */
    public void onReply(int num, String value, String msg) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#onTopic(String, IRCUser, String)
     */
    public void onTopic(String chan, IRCUser user, String topic) {
        // nothing
    }


    /**
     * Does nothing in this implementation.
     *
     * @see IRCEventListener#unknown(String, String, String, String)
     */
    public void unknown(String prefix, String command, String middle,
            String trailing) {
        // nothing
    }
}
