/*
 * IRClib -- A Java Internet Relay Chat library -- class IRCCommand
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
 * Adapted from <a
 * href="http://en.wikipedia.org/wiki/List_of_Internet_Relay_Chat_commands"
 * >http://en.wikipedia.org/wiki/List_of_Internet_Relay_Chat_commands</a>
 * Hopefully, the list is correct and complete.
 *
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 */
public enum IRCCommand {
    /**
     * {@code ADMIN}
     * <p>
     * Syntax:
     *
     * <pre>
     * ADMIN [&lt;servers&gt;]
     * </pre>
     *
     * Instructs the server to return information about the administrator of the
     * server specified by &lt;server&gt;, or the current server if target is
     * omitted.[1]
     * <p>
     */
    ADMIN,
    /**
     * Syntax:
     *
     * <pre>
     * AWAY [&lt;message&gt;]
     * </pre>
     *
     * Provides the server with a message to automatically send in reply to a
     * PRIVMSG directed at the user, but not to a channel they are on.[2] If
     * &lt;message&gt; is omitted, the away status is removed.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    AWAY,
    /**
     * Syntax:
     *
     * <pre>
     * CNOTICE &lt;nickname&gt; &lt;channel&gt; :&lt;message&gt;
     * </pre>
     *
     * Sends a channel NOTICE message to &lt;nickname&gt; on &lt;channel&gt;
     * that bypasses flood protection limits. The target nickname must be in the
     * same channel as the client issuing the command, and the client must be a
     * channel operator.
     * <p>
     * Normally an IRC server will limit the number of different targets a
     * client can send messages to within a certain time frame to prevent
     * spammers or bots from mass-messaging users on the network, however this
     * command can be used by channel operators to bypass that limit in their
     * channel. For example, it is often used by help operators that may be
     * communicating with a large number of users in a help channel at one time.
     * <p>
     * This command is not formally defined in an RFC, but is in use by some IRC
     * networks. Support is indicated in a RPL_ISUPPORT reply (numeric 005) with
     * the CNOTICE keyword
     * <p>
     */
    CNOTICE,
    /**
     * Syntax:
     *
     * <pre>
     * CPRIVMSG &lt;nickname&gt; &lt;channel&gt; :&lt;message&gt;
     * </pre>
     *
     * Sends a private message to &lt;nickname&gt; on &lt;channel&gt; that
     * bypasses flood protection limits. The target nickname must be in the same
     * channel as the client issuing the command, and the client must be a
     * channel operator.
     * <p>
     * Normally an IRC server will limit the number of different targets a
     * client can send messages to within a certain time frame to prevent
     * spammers or bots from mass-messaging users on the network, however this
     * command can be used by channel operators to bypass that limit in their
     * channel. For example, it is often used by help operators that may be
     * communicating with a large number of users in a help channel at one time.
     * <p>
     * This command is not formally defined in an RFC, but is in use by some IRC
     * networks. Support is indicated in a RPL_ISUPPORT reply (numeric 005) with
     * the CPRIVMSG keyword
     * <p>
     */
    CPRIVMSG,
    /**
     * Syntax:
     *
     * <pre>
     * CONNECT &lt;target server&gt; [&lt;port&gt; [&lt;remote server&gt;]] (RFC 1459)
     * </pre>
     *
     * <pre>
     * CONNECT &lt;target server&gt; &lt;port&gt; [&lt;remote server&gt;] (RFC 2812)
     * </pre>
     *
     * Instructs the server &lt;remote server&gt; (or the current server, if
     * &lt;remote server&gt; is omitted) to connect to &lt;target server&gt; on
     * port &lt;port&gt;.[3][4] This command should only be available to IRC
     * Operators.
     * <p>
     * Defined in RFC 1459; the &lt;port&gt; parameter became mandatory in RFC
     * 2812
     * <p>
     */
    CONNECT,
    /**
     * Syntax:
     *
     * <pre>
     * DIE
     * </pre>
     *
     * Instructs the server to shut down.[5]
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    DIE,
    /**
     * Syntax:
     * <p>
     * :&lt;source&gt; ENCAP &lt;destination&gt; &lt;subcommand&gt;
     * &lt;parameters&gt; This command is for use by servers to encapsulate
     * commands so that they will propagate across hub servers not yet updated
     * to support them, and indicates the subcommand and its parameters should
     * be passed unaltered to the destination, where it will be unencapsulated
     * and parsed. This facilitates implementation of new features without a
     * need to restart all servers before they are usable across the network.[6]
     * <p>
     */
    ENCAP,
    /**
     * Syntax:
     *
     * <pre>
     * ERROR &lt;error message&gt;
     * </pre>
     *
     * This command is for use by servers to report errors to other servers. It
     * is also used before terminating client connections.[7]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    ERROR,
    /**
     * Syntax:
     *
     * <pre>
     * HELP
     * </pre>
     *
     * Requests the server help file.
     * <p>
     * This command is not formally defined in an RFC, but is in use by most
     * major IRC daemons.
     * <p>
     */
    HELP,
    /**
     * Syntax:
     *
     * <pre>
     * INFO [&lt;target&gt;]
     * </pre>
     *
     * Returns information about the &lt;target&gt; server, or the current
     * server if &lt;target&gt; is omitted.[8] Information returned includes the
     * server's version, when it was compiled, the patch level, when it was
     * started, and any other information which may be considered to be
     * relevant.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    INFO,
    /**
     * Syntax:
     *
     * <pre>
     * INVITE &lt;nickname&gt; &lt;channel&gt;
     * </pre>
     *
     * Invites &lt;nickname&gt; to the channel &lt;channel&gt;.[9]
     * &lt;channel&gt; does not have to exist, but if it does, only members of
     * the channel are allowed to invite other clients. If the channel mode i is
     * set, only channel operators may invite other clients.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    INVITE,
    /**
     * Syntax:
     *
     * <pre>
     * ISON &lt;nicknames&gt;
     * </pre>
     *
     * Queries the server to see if the clients in the space-separated list
     * &lt;nicknames&gt; are currently on the network.[10] The server returns
     * only the nicknames that are on the network in a space-separated list. If
     * none of the clients are on the network the server returns an empty list.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    ISON,
    /**
     * Syntax:
     *
     * <pre>
     * JOIN &lt;channels&gt; [&lt;keys&gt;]
     * </pre>
     *
     * Makes the client join the channels in the comma-separated list
     * &lt;channels&gt;, specifying the passwords, if needed, in the
     * comma-separated list &lt;keys&gt;.[11] If the channel(s) do not exist
     * then they will be created.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    JOIN,
    /**
     * Syntax:
     *
     * <pre>
     * KICK &lt;channel&gt; &lt;client&gt; [&lt;message&gt;]
     * </pre>
     *
     * Forcibly removes &lt;client&gt; from &lt;channel&gt;.[12] This command
     * may only be issued by channel operators.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    KICK,
    /**
     * Syntax:
     *
     * <pre>
     * KILL &lt;client&gt; &lt;comment&gt;
     * </pre>
     *
     * Forcibly removes &lt;client&gt; from the network.[13] This command may
     * only be issued by IRC operators.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    KILL,
    /**
     * Syntax:
     *
     * <pre>
     * KNOCK &lt;channel&gt; [&lt;message&gt;]
     * </pre>
     *
     * Sends a NOTICE to an invitation-only &lt;channel&gt; with an optional
     * &lt;message&gt;, requesting an invite.
     * <p>
     * This command is not formally defined by an RFC, but is supported by most
     * major IRC daemons. Support is indicated in a RPL_ISUPPORT reply (numeric
     * 005) with the KNOCK keyword.
     * <p>
     */
    KNOCK,
    /**
     * Syntax:
     *
     * <pre>
     * LINKS [&lt;remote server&gt; [&lt;server mask&gt;]]
     * </pre>
     *
     * Lists all server links matching &lt;server mask&gt;, if given, on
     * &lt;remote server&gt;, or the current server if omitted.[14]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    LINKS,
    /**
     * Syntax:
     *
     * <pre>
     * LIST [&lt;channels&gt; [&lt;server&gt;]]
     * </pre>
     *
     * Lists all channels on the server.[15] If the comma-separated list
     * &lt;channels&gt; is given, it will return the channel topics. If
     * &lt;server&gt; is given, the command will be forwarded to &lt;server&gt;
     * for evaluation.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    LIST,
    /**
     * Syntax:
     *
     * <pre>
     * LUSERS [&lt;mask&gt; [&lt;server&gt;]]
     * </pre>
     *
     * Returns statistics about the size of the network.[16] If called with no
     * arguments, the statistics will reflect the entire network. If
     * &lt;mask&gt; is given, it will return only statistics reflecting the
     * masked subset of the network. If &lt;target&gt; is given, the command
     * will be forwarded to &lt;server&gt; for evaluation.
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    LUSERS,
    /**
     * Syntax:
     *
     * <pre>
     * MODE &lt;nickname&gt; &lt;flags&gt; (user)
     * </pre>
     *
     * <pre>
     * MODE &lt;channel&gt; &lt;flags&gt; [&lt;args&gt;]
     * </pre>
     *
     * The MODE command is dual-purpose. It can be used to set both user and
     * channel modes.[17]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    MODE,
    /**
     * Syntax:
     *
     * <pre>
     * MOTD [&lt;server&gt;]
     * </pre>
     *
     * Returns the message of the day on &lt;server&gt; or the current server if
     * it is omitted.[18]
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    MOTD,
    /**
     * Syntax:
     *
     * <pre>
     * NAMES [&lt;channels&gt;] (RFC 1459)
     * </pre>
     *
     * <pre>
     * NAMES [&lt;channels&gt; [&lt;server&gt;]] (RFC 2812)
     * </pre>
     *
     * Returns a list of who is on the comma-separated list of &lt;channels&gt;,
     * by channel name.[19] If &lt;channels&gt; is omitted, all users are shown,
     * grouped by channel name with all users who are not on a channel being
     * shown as part of channel "*". If &lt;server&gt; is specified, the command
     * is sent to &lt;server&gt; for evaluation.[20]
     * <p>
     * Defined in RFC 1459; the optional &lt;server&gt; parameter was added in
     * RFC 2812
     * <p>
     */
    NAMES,
    /**
     * Syntax:
     *
     * <pre>
     * PROTOCTL NAMESX
     * </pre>
     *
     * Instructs the server to send names in an RPL_NAMES reply prefixed with
     * their respective channel status. For example:
     * <p>
     * With NAMESX
     * <p>
     * :irc.server.net 353 Phyre = #SomeChannel :@+WiZ Without NAMESX
     * <p>
     * :irc.server.net 353 Phyre = #SomeChannel :WiZ This command can ONLY be
     * used if the NAMESX keyword is returned in an RPL_ISUPPORT (numeric 005)
     * reply. It may also be combined with the UHNAMES command.
     * <p>
     * This command is not formally defined in an RFC, but is recognized by most
     * major IRC daemons.
     * <p>
     */
    NAMESX,
    /**
     * Syntax:
     *
     * <pre>
     * NICK &lt;nickname&gt; [&lt;hopcount&gt;] (RFC 1459)
     * </pre>
     *
     * <pre>
     * NICK &lt;nickname&gt; (RFC 2812)
     * </pre>
     *
     * Allows a client to change their IRC nickname. Hopcount is for use between
     * servers to specify how far away a nickname is from its home
     * server.[21][22]
     * <p>
     * Defined in RFC 1459; the optional &lt;hopcount&gt; parameter was removed
     * in RFC 2812
     * <p>
     */
    NICK,
    /**
     * Syntax:
     *
     * <pre>
     * NOTICE &lt;msgtarget&gt; &lt;message&gt;
     * </pre>
     *
     * This command works similarly to PRIVMSG, except automatic replies must
     * never be sent in reply to NOTICE messages.[23]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    NOTICE,
    /**
     * Syntax:
     *
     * <pre>
     * OPER &lt;username&gt; &lt;password&gt;
     * </pre>
     *
     * Authenticates a user as an IRC operator on that server/network.[24]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    OPER,
    /**
     * Syntax:
     *
     * <pre>
     * PART &lt;channels&gt; [&lt;message&gt;]
     * </pre>
     *
     * Causes a user to leave the channels in the comma-separated list
     * &lt;channels&gt;.[25]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    PART,
    /**
     * Syntax:
     *
     * <pre>
     * PASS &lt;password&gt;
     * </pre>
     *
     * Sets a connection password.[26] This command must be sent before the
     * NICK/USER registration combination.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    PASS,
    /**
     * Syntax:
     *
     * <pre>
     * PING &lt;server1&gt; [&lt;server2&gt;]
     * </pre>
     *
     * Tests the presence of a connection.[27] A PING message results in a PONG
     * reply. If &lt;server2&gt; is specified, the message gets passed on to it.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    PING,
    /**
     * Syntax:
     *
     * <pre>
     * PONG &lt;server1&gt; [&lt;server2&gt;]
     * </pre>
     *
     * This command is a reply to the PING command and works in much the same
     * way.[28]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    PONG,
    /**
     * Syntax:
     *
     * <pre>
     * PRIVMSG &lt;msgtarget&gt; &lt;message&gt;
     * </pre>
     *
     * Sends &lt;message&gt; to &lt;msgtarget&gt;, which is usually a user or
     * channel.[29]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    PRIVMSG,
    /**
     * Syntax:
     *
     * <pre>
     * QUIT [&lt;message&gt;]
     * </pre>
     *
     * Disconnects the user from the server.[30]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    QUIT,
    /**
     * Syntax:
     *
     * <pre>
     * REHASH
     * </pre>
     *
     * Causes the server to re-read and re-process its configuration
     * file(s).[31] This command can only be sent by IRC Operators.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    REHASH,
    /**
     * Syntax:
     *
     * <pre>
     * RESTART
     * </pre>
     *
     * Restarts a server.[32] It may only be sent by IRC Operators.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    RESTART,
    /**
     * Syntax:
     *
     * <pre>
     * RULES
     * </pre>
     *
     * Requests the server rules.
     * <p>
     * This command is not formally defined in an RFC, but is used by
     * most[which?] major IRC daemons.
     * <p>
     */
    RULES,
    /**
     * Syntax:
     *
     * <pre>
     * SERVER &lt;servername&gt; &lt;hopcount&gt; &lt;info&gt;
     * </pre>
     *
     * The server message is used to tell a server that the other end of a new
     * connection is a server.[33] This message is also used to pass server data
     * over whole net. &lt;hopcount&gt; details how many hops (server
     * connections) away &lt;servername&gt; is. &lt;info&gt; contains addition
     * human-readable information about the server.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    SERVER,
    /**
     * Syntax:
     *
     * <pre>
     * SERVICE &lt;nickname&gt; &lt;reserved&gt; &lt;distribution&gt; &lt;type&gt; &lt;reserved&gt; &lt;info&gt;
     * </pre>
     *
     * Registers a new service on the network.[34]
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    SERVICE,
    /**
     * Syntax:
     *
     * <pre>
     * SERVLIST [&lt;mask&gt; [&lt;type&gt;]]
     * </pre>
     *
     * Lists the services currently on the network.[35]
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    SERVLIST,
    /**
     * Syntax:
     *
     * <pre>
     * SQUERY &lt;servicename&gt; &lt;text&gt;
     * </pre>
     *
     * Identical to PRIVMSG except the recipient must be a service.[36]
     * <p>
     * Defined in RFC 2812
     * <p>
     */
    SQUERY,
    /**
     * Syntax:
     *
     * <pre>
     * SQUIT &lt;server&gt; &lt;comment&gt;
     * </pre>
     *
     * Causes &lt;server&gt; to quit the network.[37]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    SQUIT,
    /**
     * Syntax:
     *
     * <pre>
     * SETNAME &lt;new real name&gt;
     * </pre>
     *
     * Allows a client to change the "real name" specified when registering a
     * connection.
     * <p>
     * This command is not formally defined by an RFC, but is in use by some IRC
     * daemons. Support is indicated in a RPL_ISUPPORT reply (numeric 005) with
     * the SETNAME keyword
     * <p>
     */
    SETNAME,
    /**
     * Syntax:
     *
     * <pre>
     * SILENCE [+/-&lt;hostmask&gt;]
     * </pre>
     *
     * Adds or removes a host mask to a server-side ignore list that prevents
     * matching users from sending the client messages. More than one mask may
     * be specified in a space-separated list, each item prefixed with a "+" or
     * "-" to designate whether it is being added or removed. Sending the
     * command with no parameters returns the entries in the client's ignore
     * list.
     * <p>
     * This command is not formally defined in an RFC, but is supported by
     * most[which?] major IRC daemons. Support is indicated in a RPL_ISUPPORT
     * reply (numeric 005) with the SILENCE keyword and the maximum number of
     * entries a client may have in its ignore list. For example:
     * <p>
     * :irc.server.net 005 WiZ WALLCHOPS WATCH=128 SILENCE=15 MODES=12
     * CHANTYPES=#
     */
    SILENCE,
    /**
     * Syntax:
     *
     * <pre>
     * STATS &lt;query&gt; [&lt;server&gt;]
     * </pre>
     *
     * Returns statistics about the current server, or &lt;server&gt; if it's
     * specified.[38]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    STATS,
    /**
     * Syntax:
     *
     * <pre>
     * SUMMON &lt;user&gt; [&lt;server&gt;] (RFC 1459)
     * </pre>
     *
     * <pre>
     * SUMMON &lt;user&gt; [&lt;server&gt; [&lt;channel&gt;]] (RFC 2812)
     * </pre>
     *
     * Gives users who are on the same host as &lt;server&gt; a message asking
     * them to join IRC.[39][40]
     * <p>
     * Defined in RFC 1459; the optional &lt;channel&gt; parameter was added in
     * RFC 2812
     * <p>
     */
    SUMMON,
    /**
     * Syntax:
     *
     * <pre>
     * TIME [&lt;server&gt;]
     * </pre>
     *
     * Returns the local time on the current server, or &lt;server&gt; if
     * specified.[41]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    TIME,
    /**
     * Syntax:
     *
     * <pre>
     * TOPIC &lt;channel&gt; [&lt;topic&gt;]
     * </pre>
     *
     * Allows the client to query or set the channel topic on
     * &lt;channel&gt;.[42] If &lt;topic&gt; is given, it sets the channel topic
     * to &lt;topic&gt;. If channel mode +t is set, only a channel operator may
     * set the topic.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    TOPIC,
    /**
     * Syntax:
     *
     * <pre>
     * TRACE [&lt;target&gt;]
     * </pre>
     *
     * Trace a path across the IRC network to a specific server or client, in a
     * similar method to traceroute.[43]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    TRACE,
    /**
     * Syntax:
     *
     * <pre>
     * PROTOCTL UHNAMES
     * </pre>
     *
     * Instructs the server to send names in an RPL_NAMES reply in the long
     * format:
     * <p>
     * With UHNAMES
     * <p>
     * :irc.server.net 353 Phyre = #SomeChannel :WiZ!user@somehost Without
     * UHNAMES
     * <p>
     * :irc.server.net 353 Phyre = #SomeChannel :WiZ This command can ONLY be
     * used if the UHNAMES keyword is returned in an RPL_ISUPPORT (numeric 005)
     * reply. It may also be combined with the NAMESX command.
     * <p>
     * This command is not formally defined in an RFC, but is recognized by most
     * major IRC daemons.
     * <p>
     */
    UHNAMES,
    /**
     * Syntax:
     *
     * <pre>
     * USER &lt;username&gt; &lt;hostname&gt; &lt;servername&gt; &lt;realname&gt; (RFC 1459)
     * </pre>
     *
     * <pre>
     * USER &lt;user&gt; &lt;mode&gt; &lt;unused&gt; &lt;realname&gt; (RFC 2812)
     * </pre>
     *
     * This command is used at the beginning of a connection to specify the
     * username, hostname, real name and initial user modes of the connecting
     * client.[44][45] &lt;realname&gt; may contain spaces, and thus must be
     * prefixed with a colon.
     * <p>
     * Defined in RFC 1459, modified in RFC 2812
     * <p>
     */
    USER,
    /**
     * Syntatx:
     * <p>
     *
     * <pre>
     * USERHOST &lt;nickname&gt; [&lt;nickname&gt; &lt;nickname&gt; ...]
     * </pre>
     *
     * Returns a list of information about the nicknames specified.[46]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    USERHOST,
    /**
     * Syntax:
     *
     * <pre>
     * USERIP &lt;nickname&gt;
     * </pre>
     *
     * Requests the direct IP address of the user with the specified nickname.
     * <p>
     * This command is often used to obtain the IP of an abusive user to more
     * effectively perform a ban. It is unclear what, if any, privileges are
     * required to execute this command on a server.
     * <p>
     * This command is not formally defined by an RFC, but is in use by some IRC
     * daemons. Support is indicated in a RPL_ISUPPORT reply (numeric 005) with
     * the USERIP keyword.
     * <p>
     */
    USERIP,
    /**
     * Syntax:
     *
     * <pre>
     * USERS [&lt;server&gt;]
     * </pre>
     *
     * Returns a list of users and information about those users in a format
     * similar to the UNIX commands who, rusers and finger.[47]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    USERS,
    /**
     * Syntax:
     *
     * <pre>
     * VERSION [&lt;server&gt;]
     * </pre>
     *
     * Returns the version of &lt;server&gt;, or the current server if
     * omitted.[48]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    VERSION,
    /**
     * Syntax:
     *
     * <pre>
     * WALLOPS &lt;message&gt;
     * </pre>
     *
     * Sends &lt;message&gt; to all operators connected to the server (RFC
     * 1459), or all users with user mode 'w' set (RFC 2812).[49][50]
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    WALLOPS,
    /**
     * Syntax:
     *
     * <pre>
     * WATCH [+/-&lt;nicknames&gt;]
     * </pre>
     *
     * Adds or removes a user to a client's server-side friends list. More than
     * one nickname may be specified in a space-separated list, each item
     * prefixed with a "+" or "-" to designate whether it is being added or
     * removed. Sending the command with no parameters returns the entries in
     * the client's friends list.
     * <p>
     * This command is not formally defined in an RFC, but is supported by
     * most[which?] major IRC daemons. Support is indicated in a RPL_ISUPPORT
     * reply (numeric 005) with the WATCH keyword and the maximum number of
     * entries a client may have in its friends list. For example:
     * <p>
     * :irc.server.net 005 WiZ WALLCHOPS WATCH=128 SILENCE=15 MODES=12
     * CHANTYPES=#
     */
    WATCH,
    /**
     * Syntax:
     *
     * <pre>
     * WHO [&lt;name&gt; ["o"]]
     * </pre>
     *
     * Returns a list of users who match &lt;name&gt;.[51] If the flag "o" is
     * given, the server will only return information about IRC Operators.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    WHO,
    /**
     * Syntax:
     *
     * <pre>
     * WHOIS [&lt;server&gt;] &lt;nicknames&gt;
     * </pre>
     *
     * Returns information about the comma-separated list of nicknames masks
     * &lt;nicknames&gt;.[52] If &lt;server&gt; is given, the command is
     * forwarded to it for processing.
     * <p>
     * Defined in RFC 1459
     * <p>
     */
    WHOIS,
    /**
     * Syntax:
     *
     * <pre>
     * WHOWAS &lt;nickname&gt; [&lt;count&gt; [&lt;server&gt;]]
     * </pre>
     *
     * Used to return information about a nickname that is no longer in use (due
     * to client disconnection, or nickname changes).[53] If given, the server
     * will return information from the last &lt;count&gt; times the nickname
     * has been used. If &lt;server&gt; is given, the command is forwarded to it
     * for processing. In RFC 2812, &lt;nickname&gt; can be a comma-separated
     * list of nicknames.[54]
     * <p>
     * Defined in RFC 1459
     */
    WHOWAS;

    private static final Map<String, IRCCommand> FAST_LOOKUP;
    static {
        Map<String, IRCCommand> fastLookUp = new HashMap<String, IRCCommand>(64);
        IRCCommand[] directives = values();
        for (IRCCommand directive : directives) {
            fastLookUp.put(directive.name(), directive);
        }
        FAST_LOOKUP = Collections.unmodifiableMap(fastLookUp);
    }

    /**
     * A {@link HashMap}-backed and {@code null}-tolerant alternative to
     * {@link #valueOf(String)}. The lookup is case-sensitive.
     *
     * @param command
     *            the command as a {@link String}
     * @return the {@link IRCCommand} that corresponds to the given string
     *         {@code command} or {@code null} if no such command exists
     */
    public static IRCCommand fastValueOf(String command) {
        return FAST_LOOKUP.get(command);
    }

}
