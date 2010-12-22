package org.schwering.irc.lib;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpAdapter;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ActionsTest {
    public static final String CHANNEL = "##actionsTest";
    private CountDownLatch actionLatch = new CountDownLatch(1);

    public void actions() throws IOException {
        final Connection connection = connect("actionsTest", "actionsTest", "Test Case");
        connection.setColors(true);
        connection.addCtcpListener(new CtcpAdapter() {
            @Override
            public void actionReceived(CtcpActionEvent event) {
                actionLatch.countDown();
            }
        });
        connection.connect();
        connection.joinChannel(CHANNEL);
        try {
            sendOtherAction();
            Assert.assertTrue(actionLatch.await(10, TimeUnit.SECONDS), "action received");
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage(), e);
        } finally {
            connection.quit("test done");
        }
    }

    private Connection connect(String nick, String userName, String realName) {
        return new Connection("irc.freenode.net", new int[]{6667},
            false, null, nick, userName, realName);
    }

    private void sendOtherAction() throws IOException {
        final Connection conn = connect("irclibdummy", "irclibdummy", "irclib test user");
        conn.addConnectionListener(new ConnectionAdapter() {
            @Override
            public void userJoined(UserParticipationEvent event) {
                super.userJoined(event);
                conn.sendCtcpAction(CHANNEL, "bows back");
                conn.quit("my work here is done.");
            }
        });
        conn.connect();
        conn.joinChannel(CHANNEL);
    }
}