package org.schwering.irc.lib;

/**
 * Contains constants: reply codes, error codes and mIRC color codes.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 1.10
 * @version 1.01
 * @see IRCEventListener#onError(int, String)
 * @see IRCEventListener#onReply(int, String, String)
 */
public interface IRCConstants {

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
     * The delimiter of CTCP messages. CTCP messages start and end with this
     * character.
     * The value is the ASCII decimal int 1.
     */
    char CTCP_DELIMITER = 1; // ASCII code
}
