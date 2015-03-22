package org.schwering.irc.lib;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public interface IRCConnection {
    static int INVALID_TIMEOUT = -1;

    public abstract void addIRCEventListener(IRCEventListener l);

    public abstract void close();

    public abstract void connect() throws IOException;

    public abstract void doAway();

    public abstract void doAway(String msg);

    public abstract void doInvite(String nick, String chan);

    public abstract void doIson(String nick);

    public abstract void doJoin(String chan);

    public abstract void doJoin(String chan, String key);

    public abstract void doKick(String chan, String nick);

    public abstract void doKick(String chan, String nick, String msg);

    public abstract void doList();

    public abstract void doList(String chan);

    public abstract void doMode(String chan);

    public abstract void doMode(String target, String mode);

    public abstract void doNames();

    public abstract void doNames(String chan);

    public abstract void doNick(String nick);

    public abstract void doNotice(String target, String msg);

    public abstract void doPart(String chan);

    public abstract void doPart(String chan, String msg);

    public abstract void doPong(String ping);

    public abstract void doPrivmsg(String target, String msg);

    public abstract void doQuit();

    public abstract void doQuit(String msg);

    public abstract void doTopic(String chan);

    public abstract void doTopic(String chan, String topic);

    public abstract void doUserhost(String nick);

    public abstract void doWho(String criteric);

    public abstract void doWhois(String nick);

    public abstract void doWhowas(String nick);

    public abstract boolean removeIRCEventListener(IRCEventListener l);

    public abstract void send(String line);

    public abstract boolean isConnected();

    public abstract InetAddress getLocalAddress();

    public abstract int getTimeout();

    public abstract int getPort();

    public abstract String getNick();

}
