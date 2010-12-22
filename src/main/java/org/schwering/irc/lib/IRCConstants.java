package org.schwering.irc.lib;

/**
 * Contains constants: reply codes, error codes and mIRC color codes.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.01
 * @see IRCEventListener#onError(int, String)
 * @see IRCEventListener#onReply(int, String, String)
 */
@SuppressWarnings({"UnusedDeclaration"})
public interface IRCConstants {
	
	/**
	 * Usually the first replies when you're connected.
	 */
    int RPL_WELCOME = 001;
	
	/**
	 * Usually the first replies when you're connected.
	 */
    int RPL_YOURHOST = 002;
	
	/**
	 * Usually the first replies when you're connected.
	 */
    int RPL_CREATED = 003;
	
	/**
	 * Usually the first replies when you're connected.
	 */
    int RPL_MYINFO = 004;
	
	/**
	 * Usually the first replies when you're connected.
	 */
    int RPL_ISUPPORT = 005;
	
	
	/**
	 * Used to indicate the nickname parameter supplied to 
	 * a command is currently unused.
	 */
    int ERR_NOSUCHNICK = 401;
	
	/**
	 * Format: "&lt;server name&gt; No such server". <br />
	 * Used to indicate the server name given currently
	 * doesn't exist.
	 */
    int ERR_NOSUCHSERVER = 402;
	
	/**
	 * Format: "&lt;channel name&gt; No such channel". <br />
	 * Used to indicate the given channel name is invalid.
	 */
    int ERR_NOSUCHCHANNEL = 403;
	
	/**
	 * Format: "&lt;channel name&gt; Cannot send to channel". <br />
	 * Sent to a user who is either (a) not on a channel
	 * which is mode +n or (b) not a chanop (or mode +v) on
	 * a channel which has mode +m set and is trying to send
	 * a PRIVMSG message to that channel.
	 */
    int ERR_CANNOTSENDTOCHAN = 404;
	
	/**
	 * Format: "&lt;channel name&gt; You have joined too many
	 * channels". <br />
	 * Sent to a user when they have joined the maximum
	 * number of allowed channels and they try to join
	 * another channel.
	 */
    int ERR_TOOMANYCHANNELS = 405;
	
	/**
	 * Format: "&lt;nickname&gt; There was no such nickname". <br />
	 * Returned by WHOWAS to indicate there is no history
	 * information for that nickname.
	 */
    int ERR_WASNOSUCHNICK = 406;
	
	
	/**
	 * Format: "&lt;target&gt; Duplicate recipients. No message delivered". <br />
	 * Returned to a client which is attempting to send a PRIVMSG/NOTICE using 
	 * the user@host destination format and for a user@host which has several 
	 * occurrences.
	 */
    int ERR_TOOMANYTARGETS = 407;
	
	/**
	 * Format: "No origin specified". <br />
	 * PING or PONG message missing the originator parameter
	 * which is required since these commands must work
	 * without valid prefixes.
	 */
    int ERR_NOORIGIN = 409;
	
	/**
	 * Format: "No recipient given (&lt;command&gt;)".
	 */
    int ERR_NORECIPIENT = 411;
	
	/**
	 * Format: "No text to send". <br />
	 * 412 - 414 are returned by PRIVMSG to indicate that
	 * the message wasn't delivered for some reason.
	 * ERR_NOTOPLEVEL and ERR_WILDTOPLEVEL are errors that
	 * are returned when an invalid use of
	 * "PRIVMSG $&lt;server&gt;" or "PRIVMSG #&lt;host&gt;" is attempted.
	 */
    int ERR_NOTEXTTOSEND = 412;
	
	/**
	 * Format: "&lt;mask&gt; No toplevel domain specified". <br />
	 * 412 - 414 are returned by PRIVMSG to indicate that
	 * the message wasn't delivered for some reason.
	 * ERR_NOTOPLEVEL and ERR_WILDTOPLEVEL are errors that
	 * are returned when an invalid use of
	 * "PRIVMSG $&lt;server&gt;" or "PRIVMSG #&lt;host&gt;" is attempted.
	 */
    int ERR_NOTOPLEVEL = 413;
	
	/**
	 * Format: "&lt;mask&gt; Wildcard in toplevel domain". <br />
	 * 412 - 414 are returned by PRIVMSG to indicate that
	 * the message wasn't delivered for some reason.
	 * ERR_NOTOPLEVEL and ERR_WILDTOPLEVEL are errors that
	 * are returned when an invalid use of
	 * "PRIVMSG $&lt;server&gt;" or "PRIVMSG #&lt;host&gt;" is attempted.
	 */
    int ERR_WILDTOPLEVEL = 414;
	
	/**
	 * Format: "&lt;command&gt; Unknown command". <br />
	 * Returned to a registered client to indicate that the
	 * command sent is unknown by the server.
	 */
    int ERR_UNKNOWNCOMMAND = 421;
	
	/**
	 * Format: "MOTD File is missing". <br />
	 * Server's MOTD file could not be opened by the server.
	 */
    int ERR_NOMOTD = 422;
	
	/**
	 * Format: "&lt;server&gt; No administrative info available". <br />
	 * Returned by a server in response to an ADMIN message
	 * when there is an error in finding the appropriate
	 * information.
	 */
    int ERR_NOADMININFO = 423;
	
	/**
	 * Format: "File error doing &lt;file op&gt; on &lt;file&gt;". <br />
	 * Generic error message used to report a failed file
	 * operation during the processing of a message.
	 */
    int ERR_FILEERROR = 424;
	
	/**
	 * Format: "No nickname given". <br />
	 * Returned when a nickname parameter expected for a
	 * command and isn't found.
	 */
    int ERR_NONICKNAMEGIVEN = 431;
	
	/**
	 * Format: "&lt;nick&gt; Erroneus nickname". <br />
	 * Returned after receiving a NICK message which contains
	 * characters which do not fall in the defined set.  See
	 * section x.x.x for details on valid nicknames.
	 */
    int ERR_ERRONEUSNICKNAME = 432;
	
	/**
	 * Format: "&lt;nick&gt; Nickname is already in use". <br />
	 * Returned when a NICK message is processed that results
	 * in an attempt to change to a currently existing
	 * nickname.
	 */
    int ERR_NICKNAMEINUSE = 433;
	
	/**
	 * Format: "&lt;nick&gt; Nickname collision KILL". <br />
	 * Returned by a server to a client when it detects a
	 * nickname collision (registered of a NICK that
	 * already exists by another server).
	 */
    int ERR_NICKCOLLISION = 436;
	
	/**
	 * Format: "&lt;nick&gt; &lt;channel&gt; They aren't on that channel". <br />
	 * Returned by the server to indicate that the target
	 * user of the command is not on the given channel.
	 */
    int ERR_USERNOTINCHANNEL = 441;
	
	/**
	 * Format: "&lt;channel&gt; You're not on that channel". <br />
	 * Returned by the server whenever a client tries to
	 * perform a channel effecting command for which the
	 * client isn't a member.
	 */
    int ERR_NOTONCHANNEL = 442;
	
	/**
	 * Format: "&lt;user&gt; &lt;channel&gt; is already on channel". <br />
	 * Returned when a client tries to invite a user to a
	 * channel they are already on.
	 */
    int ERR_USERONCHANNEL = 443;
	
	/**
	 * Format: "&lt;user&gt; User not logged in". <br />
	 * Returned by the summon after a SUMMON command for a
	 * user was unable to be performed since they were not
	 * logged in.
	 */
    int ERR_NOLOGIN = 444;
	
	/**
	 * Format: "SUMMON has been disabled". <br />
	 * Returned as a response to the SUMMON command.  Must be
	 * returned by any server which does not implement it.
	 */
    int ERR_SUMMONDISABLED = 445;
	
	/**
	 * Format: "USERS has been disabled". <br />
	 * Returned as a response to the USERS command.  Must be
	 * returned by any server which does not implement it.
	 */
    int ERR_USERSDISABLED = 446;
	
	/**
	 * Format: "You have not registered". <br /> 
	 * Returned by the server to indicate that the client
	 * must be registered before the server will allow it
	 * to be parsed in detail.
	 */
    int ERR_NOTREGISTERED = 451;
	
	/**
	 * Format: "&lt;command&gt; Not enough parameters". <br />
	 * Returned by the server by numerous commands to
	 * indicate to the client that it didn't supply enough
	 * parameters.
	 */
    int ERR_NEEDMOREPARAMS = 461;
	
	/**
	 * Format: "You may not reregister". <br />
	 * Returned by the server to any link which tries to
	 * change part of the registered details (such as
	 * password or user details from second USER message).
	 */
    int ERR_ALREADYREGISTRED = 462;
	
	/**
	 * Format: "Your host isn't among the privileged". <br />
	 * Returned to a client which attempts to register with
	 * a server which does not been setup to allow
	 * connections from the host the attempted connection
	 * is tried.
	 */
    int ERR_NOPERMFORHOST = 463;
	
	/**
	 * Format: "Password incorrect". <br />
	 * Returned to indicate a failed attempt at registering
	 * a connection for which a password was required and
	 * was either not given or incorrect.
	 */
    int ERR_PASSWDMISMATCH = 464;
	
	/**
	 * Format: "You are banned from this server". <br />
	 * Returned after an attempt to connect and register
	 * yourself with a server which has been setup to
	 * explicitly deny connections to you.
	 */
    int ERR_YOUREBANNEDCREEP = 465;
	
	/**
	 * Format: "&lt;channel&gt; Channel key already set".
	 */
    int ERR_KEYSET = 467;
	
	/**
	 * Format:  "&lt;channel&gt; Cannot join channel (+l)".
	 */
    int ERR_CHANNELISFULL = 471;
	
	/**
	 * Format:  "&lt;char&gt; is unknown mode char to me".
	 */
    int ERR_UNKNOWNMODE = 472;
	
	/**
	 * Format: "&lt;channel&gt; Cannot join channel (+i)".
	 */
    int ERR_INVITEONLYCHAN = 473;
	
	/**
	 * Format:  "&lt;channel&gt; Cannot join channel (+b)".
	 */
    int ERR_BANNEDFROMCHAN = 474;
	
	/**
	 * Format:  "&lt;channel&gt; Cannot join channel (+k)".
	 */
    int ERR_BADCHANNELKEY = 475;
	
	/**
	 * Format:  "Permission Denied- You're not an IRC operator".
	 * Any command requiring operator privileges to operate
	 * must return this error to indicate the attempt was
	 * unsuccessful.
	 */
    int ERR_NOPRIVILEGES = 481;
	
	/**
	 * Format: "&lt;channel&gt; You're not channel operator". <br />
	 * Any command requiring 'chanop' privileges (such as
	 * MODE messages) must return this error if the client
	 * making the attempt is not a chanop on the specified
	 * channel.
	 */
    int ERR_CHANOPRIVSNEEDED = 482;
	
	/**
	 * Format: "You cant kill a server!". <br />
	 * Any attempts to use the KILL command on a server
	 * are to be refused and this error returned directly
	 * to the client.
	 */
    int ERR_CANTKILLSERVER = 483;
	
	/**
	 * Format: "No O-lines for your host". <br />
	 * If a client sends an OPER message and the server has
	 * not been configured to allow connections from the
	 * client's host as an operator, this error must be
	 * returned.
	 */
    int ERR_NOOPERHOST = 491;
	
	/**
	 * Format: "Unknown MODE flag". <br />
	 * Returned by the server to indicate that a MODE
	 * message was sent with a nickname parameter and that
	 * the a mode flag sent was not recognized.
	 */
    int ERR_UMODEUNKNOWNFLAG = 501;
	
	/**
	 * Format: "Cant change mode for other users". <br />
	 * Error sent to any user trying to view or change the
	 * user mode for a user other than themselves.
	 */
    int ERR_USERSDONTMATCH = 502;
	
	/**
	 * Dummy reply number. Not used.
	 */
    int RPL_NONE = 300;
	
	/**
	 * Format: "[&lt;reply&gt;{&lt;space&gt;&lt;reply&gt;}]". <br />
	 * Reply format used by USERHOST to list replies to
	 * the query list.  The reply string is composed as
	 * follows:<br />
	 * &lt;reply&gt; ::= &lt;nick&gt;['*'] '=' &lt;'+'|'-'&gt;&lt;hostname&gt;
	 * <br />
	 * The '*' indicates whether the client has registered
	 * as an Operator.  The '-' or '+' characters represent
	 * whether the client has set an AWAY message or not
	 * respectively.
	 */
    int RPL_USERHOST = 302;
	
	/**
	 * Format: "[&lt;nick&gt; {&lt;space&gt;&lt;nick&gt;}]". <br />
	 * Reply format used by ISON to list replies to the
	 * query list.
	 */
    int RPL_ISON = 303;
	
	/**
	 * Format: "&lt;nick&gt; &lt;away message&gt;".
	 */
    int RPL_AWAY = 301;
	
	/**
	 * Format: "You are no longer marked as being away".
	 */
    int RPL_UNAWAY = 305;
	
	/**
	 * Format: "You have been marked as being away". <br />
	 * These replies are used with the AWAY command (if
	 * allowed).  RPL_AWAY is sent to any client sending a
	 * PRIVMSG to a client which is away.  RPL_AWAY is only
	 * sent by the server to which the client is connected.
	 * Replies RPL_UNAWAY and RPL_NOWAWAY are sent when the
	 * client removes and sets an AWAY message.
	 */
    int RPL_NOWAWAY = 306;
	
	/**
	 * Format: "&lt;nick&gt; &lt;user&gt; &lt;host&gt; * :&lt;real name&gt;".
	 * <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_WHOISUSER = 311;
	
	/**
	 * Format: "&lt;nick&gt; &lt;server&gt; &lt;server info&gt;". <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_WHOISSERVER = 312;
	
	/**
	 * Format: "&lt;nick&gt; is an IRC operator". <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_WHOISOPERATOR = 313;
	
	/**
	 * Format: "&lt;nick&gt; &lt;integer&gt; seconds idle". <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_WHOISIDLE = 317;
	
	/**
	 * Format: "&lt;nick&gt; End of /WHOIS list". <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_ENDOFWHOIS = 318;
	
	/**
	 * Format: "&lt;nick&gt; {[@|+]&lt;channel&gt;&lt;space&gt;}". <br />
	 * Replies 311 - 313, 317 - 319 are all replies
	 * generated in response to a WHOIS message.  Given that
	 * there are enough parameters present, the answering
	 * server must either formulate a reply out of the above
	 * numerics (if the query nick is found) or return an
	 * error reply.  The '*' in RPL_WHOISUSER is there as
	 * the literal character and not as a wild card.  For
	 * each reply set, only RPL_WHOISCHANNELS may appear
	 * more than once (for long lists of channel names).
	 * The '@' and '+' characters next to the channel name
	 * indicate whether a client is a channel operator or
	 * has been granted permission to speak on a moderated
	 * channel.  The RPL_ENDOFWHOIS reply is used to mark
	 * the end of processing a WHOIS message.
	 */
    int RPL_WHOISCHANNELS = 319;
	
	/**
	 * Format: "&lt;nick&gt; &lt;authname&gt; is authed as"<br />
	 * This is part of /WHOIS on many servers which provide bots to authenticate.
	 * This is not part of the RFC.
	 */
    int RPL_WHOISAUTHNAME = 330;
	
	/**
	 * Format: "&lt;nick&gt; &lt;user&gt; &lt;host&gt; * &lt;real name&gt;".
	 */
    int RPL_WHOWASUSER = 314;
	
	/**
	 * Format: "&lt;nick&gt; End of WHOWAS". <br />
	 * When replying to a WHOWAS message, a server must use
	 * the replies RPL_WHOWASUSER, RPL_WHOISSERVER or
	 * ERR_WASNOSUCHNICK for each nickname in the presented
	 * list.  At the end of all reply batches, there must
	 * be RPL_ENDOFWHOWAS (even if there was only one reply
	 * and it was an error).
	 */
    int RPL_ENDOFWHOWAS = 369;
	
	/**
	 * Format: "Channel Users  Name". <br />
	 * Replies RPL_LISTSTART, RPL_LIST, RPL_LISTEND mark
	 * the start, actual replies with data and end of the
	 * server's response to a LIST command.  If there are
	 * no channels available to return, only the start
	 * and end reply must be sent.
	 */
    int RPL_LISTSTART = 321;
	
	/**
	 * Format: "&lt;channel&gt; &lt;# visible&gt; &lt;topic&gt;". <br />
	 * Replies RPL_LISTSTART, RPL_LIST, RPL_LISTEND mark
	 * the start, actual replies with data and end of the
	 * server's response to a LIST command.  If there are
	 * no channels available to return, only the start
	 * and end reply must be sent.
	 */
    int RPL_LIST = 322;
	
	/**
	 * Format: "End of /LIST". <br />
	 * Replies RPL_LISTSTART, RPL_LIST, RPL_LISTEND mark
	 * the start, actual replies with data and end of the
	 * server's response to a LIST command.  If there are
	 * no channels available to return, only the start
	 * and end reply must be sent.
	 */
    int RPL_LISTEND = 323;
	
	/**
	 * Format: "&lt;channel&gt; &lt;mode&gt; &lt;mode params&gt;". <br />
	 */
    int RPL_CHANNELMODEIS = 324;
	
	/**
	 * Format: "&lt;nick&gt; &lt;authname&gt; is authed as"<br />
	 * This is part of /WHOIS on many servers which provide bots to authenticate.
	 * This is not part of the RFC.
	 * @deprecated (1) The value should be 330 instead of 333; (2) moved to
	 * <code>RPL_WHOISAUTHNAME</code>.
	 */
    int RPL_AUTHNAME = 333;
	
	/**
	 * Format: "&lt;channel&gt; No topic is set". <br />
	 * When sending a TOPIC message to determine the
	 * channel topic, one of two replies is sent.  If
	 * the topic is set, RPL_TOPIC is sent back else
	 * public static final int RPL_NOTOPIC.
	 */
    int RPL_NOTOPIC = 331;
	
	/**
	 * Format: "&lt;channel&gt; &lt;topic&gt;". <br />
	 * When sending a TOPIC message to determine the
	 * channel topic, one of two replies is sent.  If
	 * the topic is set, RPL_TOPIC is sent back else
	 * public static final int RPL_NOTOPIC.
	 */
    int RPL_TOPIC = 332;
	
	/**
	 * Format: "&lt;channel&gt; &lt;nick-who-set-topic&gt; &lt;seconds&gt;".
	 * The seconds are not milliseconds; just multiply it with 1000 and then 
	 * format a date with it.
	 */
    int RPL_TOPICINFO = 333;
	
	/**
	 * Format: "&lt;channel&gt; &lt;nick&gt;". <br />
	 * Returned by the server to indicate that the
	 * attempted INVITE message was successful and is
	 * being passed onto the end client.
	 */
    int RPL_INVITING = 341;
	
	/**
	 * Format: "&lt;user&gt; Summoning user to IRC". <br />
	 * Returned by a server answering a SUMMON message to
	 * indicate that it is summoning that user.
	 */
    int RPL_SUMMONING = 342;
	
	/**
	 * Format: "&lt;version&gt;.&lt;debuglevel&gt; &lt;server&gt; 
	 * &lt;comments&gt;". <br />
	 * Reply by the server showing its version details.
	 * The &lt;version&gt; is the version of the software being
	 * used (including any patchlevel revisions) and the
	 * &lt;debuglevel&gt; is used to indicate if the server is
	 * running in "debug mode". <br />
	 * The "comments" field may contain any comments about
	 * the version or further version details.
	 */
    int RPL_VERSION = 351;
	
	/**
	 * Format: "&lt;channel&gt; &lt;user&gt; &lt;host&gt; &lt;server&gt; 
	 * &lt;nick&gt; &lt;H|G&gt;[*][@|+] &lt;hopcount&gt; &lt;real name&gt;". <br />
	 * The RPL_WHOREPLY and RPL_ENDOFWHO pair are used
	 * to answer a WHO message.  The RPL_WHOREPLY is only
	 * sent if there is an appropriate match to the WHO
	 * query.  If there is a list of parameters supplied
	 * with a WHO message, a RPL_ENDOFWHO must be sent
	 * after processing each list item with &lt;name&gt; being
	 * the item.
	 */
    int RPL_WHOREPLY = 352;
	
	/**
	 * Format: "&lt;name&gt; End of /WHO list". <br />
	 * The RPL_WHOREPLY and RPL_ENDOFWHO pair are used
	 * to answer a WHO message.  The RPL_WHOREPLY is only
	 * sent if there is an appropriate match to the WHO
	 * query.  If there is a list of parameters supplied
	 * with a WHO message, a RPL_ENDOFWHO must be sent
	 * after processing each list item with &lt;name&gt; being
	 * the item.
	 */
    int RPL_ENDOFWHO = 315;
	
	/**
	 * Format: "&lt;channel&gt; [[@|+]&lt;nick&gt; [[@|+]&lt;nick&gt; 
	 * [...]]]" (RFC 1459) or 
	 * "( "=" / "*" / "@" ) &lt;channel&gt; :[ "@" / "+" ] &lt;nick&gt;
	 * *( " " [ "@" / "+" ] &lt;nick&gt; )" (RFC2812). <br />
	 * To reply to a NAMES message, a reply pair consisting
	 * of RPL_NAMREPLY and RPL_ENDOFNAMES is sent by the
	 * server back to the client.  If there is no channel
	 * found as in the query, then only RPL_ENDOFNAMES is
	 * returned.  The exception to this is when a NAMES
	 * message is sent with no parameters and all visible
	 * channels and contents are sent back in a series of
	 * RPL_NAMEREPLY messages with a RPL_ENDOFNAMES to mark
	 * the end.
	 */
    int RPL_NAMREPLY = 353;
	
	/**
	 * Format: "&lt;channel&gt; End of /NAMES list". <br />
	 * To reply to a NAMES message, a reply pair consisting
	 * of RPL_NAMREPLY and RPL_ENDOFNAMES is sent by the
	 * server back to the client.  If there is no channel
	 * found as in the query, then only RPL_ENDOFNAMES is
	 * returned.  The exception to this is when a NAMES
	 * message is sent with no parameters and all visible
	 * channels and contents are sent back in a series of
	 * RPL_NAMEREPLY messages with a RPL_ENDOFNAMES to mark
	 * the end.
	 */
    int RPL_ENDOFNAMES = 366;
	
	/**
	 * Format: "&lt;mask&gt; &lt;server&gt; &lt;hopcount&gt; &lt;server info&gt;".
	 * <br />
	 * In replying to the LINKS message, a server must send
	 * replies back using the RPL_LINKS numeric and mark the
	 * end of the list using an RPL_ENDOFLINKS reply.
	 */
    int RPL_LINKS = 364;
	
	/**
	 * Format: "&lt;mask&gt; End of /LINKS list". <br />
	 * In replying to the LINKS message, a server must send
	 * replies back using the RPL_LINKS numeric and mark the
	 * end of the list using an RPL_ENDOFLINKS reply.
	 */
    int RPL_ENDOFLINKS = 365;
	
	/**
	 * Format: "&lt;channel&gt; &lt;banid&gt;" (RFC 1459), or
	 * "&lt;channel&gt; &lt;banid&gt; &lt;user or *&gt; &lt;seconds&gt;"
	 * where the seconds are the date when the ban was set (this format is
	 * not part of any RFC but seems to be used in several networks).<br />
	 * When listing the active 'bans' for a given channel,
	 * a server is required to send the list back using the
	 * RPL_BANLIST and RPL_ENDOFBANLIST messages.  A separate
	 * RPL_BANLIST is sent for each active banid.  After the
	 * banids have been listed (or if none present) a
	 * RPL_ENDOFBANLIST must be sent.
	 */
    int RPL_BANLIST = 367;
	
	/**
	 * Format: "&lt;channel&gt; End of channel ban list". <br />
	 * When listing the active 'bans' for a given channel,
	 * a server is required to send the list back using the
	 * RPL_BANLIST and RPL_ENDOFBANLIST messages.  A separate
	 * RPL_BANLIST is sent for each active banid.  After the
	 * banids have been listed (or if none present) a
	 * RPL_ENDOFBANLIST must be sent.
	 */
    int RPL_ENDOFBANLIST = 368;
	
	/**
	 * Format: "&lt;string&gt;". <br />
	 * A server responding to an INFO message is required to
	 * send all its 'info' in a series of RPL_INFO messages
	 * with a RPL_ENDOFINFO reply to indicate the end of the
	 * replies.
	 */
    int RPL_INFO = 371;
	
	
	/**
	 * Format: "End of /INFO list". <br />
	 * A server responding to an INFO message is required to
	 * send all its 'info' in a series of RPL_INFO messages
	 * with a RPL_ENDOFINFO reply to indicate the end of the
	 * replies.
	 */
    int RPL_ENDOFINFO = 374;
	
	/**
	 * Format: "- &lt;server&gt; Message of the day - ". <br />
	 * When responding to the MOTD message and the MOTD file
	 * is found, the file is displayed line by line, with
	 * each line no longer than 80 characters, using
	 * RPL_MOTD format replies.  These should be surrounded
	 * by a RPL_MOTDSTART (before the RPL_MOTDs) and an
	 * RPL_ENDOFMOTD (after).
	 */
    int RPL_MOTDSTART = 375;
	
	/**
	 * Format: "- &lt;text&gt;". <br />
	 * When responding to the MOTD message and the MOTD file
	 * is found, the file is displayed line by line, with
	 * each line no longer than 80 characters, using
	 * RPL_MOTD format replies.  These should be surrounded
	 * by a RPL_MOTDSTART (before the RPL_MOTDs) and an
	 * RPL_ENDOFMOTD (after).
	 */
    int RPL_MOTD = 372;
	
	/**
	 * Format: "End of /MOTD command". <br />
	 * When responding to the MOTD message and the MOTD file
	 * is found, the file is displayed line by line, with
	 * each line no longer than 80 characters, using
	 * RPL_MOTD format replies.  These should be surrounded
	 * by a RPL_MOTDSTART (before the RPL_MOTDs) and an
	 * RPL_ENDOFMOTD (after).
	 */
    int RPL_ENDOFMOTD = 376;
	
	/**
	 * Format: "You are now an IRC operator". <br />
	 * RPL_YOUREOPER is sent back to a client which has
	 * just successfully issued an OPER message and gained
	 * operator status.
	 */
    int RPL_YOUREOPER = 381;
	
	/**
	 * Format: "&lt;config file&gt; Rehashing". <br />
	 * If the REHASH option is used and an operator sends
	 * a REHASH message, an RPL_REHASHING is sent back to
	 * the operator.
	 */
    int RPL_REHASHING = 382;
	
	/**
	 * Format: "&lt;server&gt; &lt;string showing server's local time&gt;". <br />
	 * When replying to the TIME message, a server must send
	 * the reply using the RPL_TIME format above.  The string
	 * showing the time need only contain the correct day and
	 * time there.  There is no further requirement for the
	 * time string.
	 */
    int RPL_TIME = 391;
	
	/**
	 * Format: "UserID   Terminal  Host". <br />
	 * If the USERS message is handled by a server, the
	 * replies RPL_USERSTART, RPL_USERS, RPL_ENDOFUSERS and
	 * RPL_NOUSERS are used.  RPL_USERSSTART must be sent
	 * first, following by either a sequence of RPL_USERS
	 * or a single RPL_NOUSER.  Following this is
	 * RPL_ENDOFUSERS.
	 */
    int RPL_USERSSTART = 392;
	
	/**
	 * Format: "%-8s %-9s %-8s". <br />
	 * If the USERS message is handled by a server, the
	 * replies RPL_USERSTART, RPL_USERS, RPL_ENDOFUSERS and
	 * RPL_NOUSERS are used.  RPL_USERSSTART must be sent
	 * first, following by either a sequence of RPL_USERS
	 * or a single RPL_NOUSER.  Following this is
	 * RPL_ENDOFUSERS.
	 */
    int RPL_USERS = 393;
	
	/**
	 * Format: "End of users". <br />
	 * If the USERS message is handled by a server, the
	 * replies RPL_USERSTART, RPL_USERS, RPL_ENDOFUSERS and
	 * RPL_NOUSERS are used.  RPL_USERSSTART must be sent
	 * first, following by either a sequence of RPL_USERS
	 * or a single RPL_NOUSER.  Following this is
	 * RPL_ENDOFUSERS.
	 */
    int RPL_ENDOFUSERS = 394;
	
	/**
	 * Format: "Nobody logged in". <br />
	 * If the USERS message is handled by a server, the
	 * replies RPL_USERSTART, RPL_USERS, RPL_ENDOFUSERS and
	 * RPL_NOUSERS are used.  RPL_USERSSTART must be sent
	 * first, following by either a sequence of RPL_USERS
	 * or a single RPL_NOUSER.  Following this is
	 * RPL_ENDOFUSERS.
	 */
    int RPL_NOUSERS = 395;
	
	/**
	 * Format: "Link &lt;version & debug level&gt; &lt;destination&gt; 
	 * &lt;next server&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACELINK = 200;
	
	/**
	 * Format: "Try. &lt;class&gt; &lt;server&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACECONNECTING = 201;
	
	/**
	 * Format: "H.S. &lt;class&gt; &lt;server&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACEHANDSHAKE = 202;
	
	/**
	 * Format: "???? &lt;class&gt; [&lt;client IP address in dot form&gt;]". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACEUNKNOWN = 203;
	
	/**
	 * Format: "Oper &lt;class&gt; &lt;nick&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACEOPERATOR = 204;
	
	/**
	 * Format: "User &lt;class&gt; &lt;nick&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACEUSER = 205;
	
	/**
	 * Format: "Serv &lt;class&gt; &lt;int&gt;S &lt;int&gt;C &lt;server&gt;
	 * &lt;nick!user|*!*&gt;@&lt;host|server&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACESERVER = 206;
	
	/**
	 * Format: "&lt;newtype&gt; 0 &lt;client name&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACENEWTYPE = 208;
	
	/**
	 * Format: "File &lt;logfile&gt; &lt;debug level&gt;". <br />
	 * The RPL_TRACE* are all returned by the server in
	 * response to the TRACE message.  How many are
	 * returned is dependent on the the TRACE message and
	 * whether it was sent by an operator or not.  There
	 * is no predefined order for which occurs first.
	 * Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
	 * RPL_TRACEHANDSHAKE are all used for connections
	 * which have not been fully established and are either
	 * unknown, still attempting to connect or in the
	 * process of completing the 'server handshake'.
	 * RPL_TRACELINK is sent by any server which handles
	 * a TRACE message and has to pass it on to another
	 * server.  The list of RPL_TRACELINKs sent in
	 * response to a TRACE command traversing the IRC
	 * network should reflect the actual connectivity of
	 * the servers themselves along that path.
	 * RPL_TRACENEWTYPE is to be used for any connection
	 * which does not fit in the other categories but is
	 * being displayed anyway.
	 */
    int RPL_TRACELOG = 261;
	
	/**
	 * Format: "&lt;linkname&gt; &lt;sendq&gt; &lt;sent messages&gt; 
	 * &lt;sent bytes&gt; &lt;received messages&gt; 
	 * &lt;received bytes&gt; &lt;time open&gt;".
	 */
    int RPL_STATSLINKINFO = 211;
	
	/**
	 * Format: "&lt;command&gt; &lt;count&gt;".
	 */
    int RPL_STATSCOMMANDS = 212;
	
	/**
	 * Format: "C &lt;host&gt; * &lt;name&gt; &lt;port&gt; &lt;class&gt;".
	 */
    int RPL_STATSCLINE = 213;
	
	/**
	 * Format: "N &lt;host&gt; * &lt;name&gt; &lt;port&gt; &lt;class&gt;".
	 */
    int RPL_STATSNLINE = 214;
	
	/**
	 * Format: "I &lt;host&gt; * &lt;host&gt; &lt;port&gt; &lt;class&gt;".
	 */
    int RPL_STATSILINE = 215;
	
	/**
	 * Format: "K &lt;host&gt; * &lt;username&gt; &lt;port&gt; &lt;class&gt;".
	 */
    int RPL_STATSKLINE = 216;
	
	/**
	 * Format: "Y &lt;class&gt; &lt;ping frequency&gt; &lt;connect 
	 * frequency&gt; &lt;max sendq&gt;".
	 */
    int RPL_STATSYLINE = 218;
	
	/**
	 * Format: "&lt;stats letter&gt; End of /STATS report".
	 */
    int RPL_ENDOFSTATS = 219;
	
	/**
	 * Format: "L &lt;hostmask&gt; * &lt;servername&gt; &lt;maxdepth&gt;".
	 */
    int RPL_STATSLLINE = 241;
	
	/**
	 * Format: "Server Up %d days %d:%02d:%02d".
	 */
    int RPL_STATSUPTIME = 242;
	
	/**
	 * Format: "O &lt;hostmask&gt; * &lt;name&gt;"
	 */
    int RPL_STATSOLINE = 243;
	
	/**
	 * Format: "H &lt;hostmask&gt; * &lt;servername&gt;".
	 */
    int RPL_STATSHLINE = 244;
	
	/**
	 * Format: "&lt;user mode string&gt;". <br />
	 * To answer a query about a client's own mode,
	 * RPL_UMODEIS is sent back.
	 */
    int RPL_UMODEIS = 221;
	
	/**
	 * Format:   "There are &lt;integer&gt; users and &lt;integer&gt; 
	 * invisible on &lt;integer&gt; servers". <br />
	 * In processing an LUSERS message, the server
	 * sends a set of replies from RPL_LUSERCLIENT,
	 * RPL_LUSEROP, RPL_USERUNKNOWN,
	 * RPL_LUSERCHANNELS and RPL_LUSERME.  When
	 * replying, a server must send back
	 * RPL_LUSERCLIENT and RPL_LUSERME.  The other
	 * replies are only sent back if a non-zero count
	 * is found for them.
	 */
    int RPL_LUSERCLIENT = 251;
	
	/**
	 * Format: "&lt;integer&gt; operator(s) online". <br />
	 * In processing an LUSERS message, the server
	 * sends a set of replies from RPL_LUSERCLIENT,
	 * RPL_LUSEROP, RPL_USERUNKNOWN,
	 * RPL_LUSERCHANNELS and RPL_LUSERME.  When
	 * replying, a server must send back
	 * RPL_LUSERCLIENT and RPL_LUSERME.  The other
	 * replies are only sent back if a non-zero count
	 * is found for them.
	 */
    int RPL_LUSEROP = 252;
	
	/**
	 * Format: "&lt;integer&gt; unknown connection(s)". <br />
	 * In processing an LUSERS message, the server
	 * sends a set of replies from RPL_LUSERCLIENT,
	 * RPL_LUSEROP, RPL_USERUNKNOWN,
	 * RPL_LUSERCHANNELS and RPL_LUSERME.  When
	 * replying, a server must send back
	 * RPL_LUSERCLIENT and RPL_LUSERME.  The other
	 * replies are only sent back if a non-zero count
	 * is found for them.
	 */
    int RPL_LUSERUNKNOWN = 253;
	
	/**
	 * Format: "&lt;integer&gt; channels formed". <br />
	 * In processing an LUSERS message, the server
	 * sends a set of replies from RPL_LUSERCLIENT,
	 * RPL_LUSEROP, RPL_USERUNKNOWN,
	 * RPL_LUSERCHANNELS and RPL_LUSERME.  When
	 * replying, a server must send back
	 * RPL_LUSERCLIENT and RPL_LUSERME.  The other
	 * replies are only sent back if a non-zero count
	 * is found for them.
	 */
    int RPL_LUSERCHANNELS = 254;
	
	/**
	 * Format: "I have &lt;integer&gt; clients and &lt;integer&gt; 
	 * servers". <br />
	 * In processing an LUSERS message, the server
	 * sends a set of replies from RPL_LUSERCLIENT,
	 * RPL_LUSEROP, RPL_USERUNKNOWN,
	 * RPL_LUSERCHANNELS and RPL_LUSERME.  When
	 * replying, a server must send back
	 * RPL_LUSERCLIENT and RPL_LUSERME.  The other
	 * replies are only sent back if a non-zero count
	 * is found for them.
	 */
    int RPL_LUSERME = 255;
	
	/**
	 * Format: "&lt;server&gt; Administrative info". <br />
	 * When replying to an ADMIN message, a server
	 * is expected to use replies RLP_ADMINME
	 * through to RPL_ADMINEMAIL and provide a text
	 * message with each.  For RPL_ADMINLOC1 a
	 * description of what city, state and country
	 * the server is in is expected, followed by
	 * details of the university and department
	 * (RPL_ADMINLOC2) and finally the administrative
	 * contact for the server (an email address here
	 * is required) in RPL_ADMINEMAIL.
	 */
    int RPL_ADMINME = 256;
	
	/**
	 * Format: "&lt;admin info&gt;". <br />
	 * When replying to an ADMIN message, a server
	 * is expected to use replies RLP_ADMINME
	 * through to RPL_ADMINEMAIL and provide a text
	 * message with each.  For RPL_ADMINLOC1 a
	 * description of what city, state and country
	 * the server is in is expected, followed by
	 * details of the university and department
	 * (RPL_ADMINLOC2) and finally the administrative
	 * contact for the server (an email address here
	 * is required) in RPL_ADMINEMAIL.
	 */
    int RPL_ADMINLOC1 = 257;
	
	/**
	 * Format: "&lt;admin info&gt;". <br />
	 * When replying to an ADMIN message, a server
	 * is expected to use replies RLP_ADMINME
	 * through to RPL_ADMINEMAIL and provide a text
	 * message with each.  For RPL_ADMINLOC1 a
	 * description of what city, state and country
	 * the server is in is expected, followed by
	 * details of the university and department
	 * (RPL_ADMINLOC2) and finally the administrative
	 * contact for the server (an email address here
	 * is required) in RPL_ADMINEMAIL.
	 */
    int RPL_ADMINLOC2 = 258;
	
	/**
	 * Format: "&lt;admin info&gt;". <br />
	 * When replying to an ADMIN message, a server
	 * is expected to use replies RLP_ADMINME
	 * through to RPL_ADMINEMAIL and provide a text
	 * message with each.  For RPL_ADMINLOC1 a
	 * description of what city, state and country
	 * the server is in is expected, followed by
	 * details of the university and department
	 * (RPL_ADMINLOC2) and finally the administrative
	 * contact for the server (an email address here
	 * is required) in RPL_ADMINEMAIL.
	 */
    int RPL_ADMINEMAIL = 259;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_TRACECLASS = 209;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_STATSQLINE = 217;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_SERVICEINFO = 231;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_ENDOFSERVICES = 232;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_SERVICE = 233;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_SERVLIST = 234;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_SERVLISTEND = 235;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_WHOISCHANOP = 316;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_KILLDONE = 361;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_CLOSING = 362;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_CLOSEEND = 363;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_INFOSTART = 373;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int RPL_MYPORTIS = 384;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int ERR_YOUWILLBEBANNED = 466;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int ERR_BADCHANMASK = 476;
	
	/**
	 * This numeric is no longer in use, reserved for future planned use or
	 * anything else.
	 */
    int ERR_NOSERVICEHOST = 492;
	
	/** 
	 * This is part of the mIRC code and shows that a color-code starts / ends. 
	 * Here it is as the ASCII decimal int 3. 
	 */
    char COLOR_INDICATOR = 3; // ASCII code
	
	/** 
	 * This is part of the mIRC code and shows that bold starts / ends.
	 * Here it is as the ASCII decimal int 31. 
	 */
    char BOLD_INDICATOR = 31; // ASCII code
	
	/**
	 * This is part of the mIRC code and shows that bold starts / ends.
	 * Here it is as the ASCII decimal int 2. 
	 */
    char UNDERLINE_INDICATOR = 2; // ASCII code
	
	/**
	 * This is part of the mIRC code and shows that bold, underline and colors 
	 * end. 
	 * Here it is as the ASCII decimal int 15. 
	 */
    char COLOR_END_INDICATOR = 15; // ASCII code
	
	/**
	 * This is part of the mIRC code and indicates that the client's colors are 
	 * reversed (background -&gt; foreground and foreground -&gt; background).
	 * Here it is as the ASCII decimal int 22. 
	 */
    char COLOR_REVERSE_INDICATOR = 22; // ASCII code
	
	/**
	 * This is part of the mIRC code and shows that a PRIVMSG is an ACTION 
	 * (<code>/me</code>).
	 * Here it is as the ASCII decimal int 1. 
	 * @deprecated Replaced by {@link #CTCP_DELIMITER}, because my understanding
	 * of this is wrong. The /me command is no invention of mIRC but part of 
	 * CTCP.
	 */
    char ACTION_INDICATOR = 1; // ASCII code
	
	/**
	 * The delimiter of CTCP messages. CTCP messages start and end with this
	 * character.
	 * The value is the ASCII decimal int 1.
	 */
    char CTCP_DELIMITER = 1; // ASCII code
}
