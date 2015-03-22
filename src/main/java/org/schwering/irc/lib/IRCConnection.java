package org.schwering.irc.lib;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCConnection {
    int INVALID_TIMEOUT = -1;

    void addIRCEventListener(IRCEventListener l);

    void close();

    void connect() throws IOException;

    void doAway();

    void doAway(String msg);

    void doInvite(String nick, String chan);

    void doIson(String nick);

    void doJoin(String chan);

    void doJoin(String chan, String key);

    void doKick(String chan, String nick);

    void doKick(String chan, String nick, String msg);

    void doList();

    void doList(String chan);

    void doMode(String chan);

    void doMode(String target, String mode);

    void doNames();

    void doNames(String chan);

    void doNick(String nick);

    void doNotice(String target, String msg);

    void doPart(String chan);

    void doPart(String chan, String msg);

    void doPong(String ping);

    void doPrivmsg(String target, String msg);

    void doQuit();

    void doQuit(String msg);

    void doTopic(String chan);

    void doTopic(String chan, String topic);

    void doUserhost(String nick);

    void doWho(String criteric);

    void doWhois(String nick);

    void doWhowas(String nick);

    boolean removeIRCEventListener(IRCEventListener l);

    void send(String line);

    boolean isConnected();

    InetAddress getLocalAddress();

    int getTimeout();

    int getPort();

    String getNick();

}
