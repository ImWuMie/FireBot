package dev.wumie.system;

import com.google.gson.Gson;
import dev.wumie.FireQQ;
import dev.wumie.TickingTask;
import dev.wumie.messages.*;
import dev.wumie.system.event.MsgEventManager;
import dev.wumie.system.modules.ModuleManager;
import dev.wumie.utils.GsonUtils;
import dev.wumie.websocket.BotMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Date;
import java.util.HashSet;

public class MessageHandler {
    public Gson gson = GsonUtils.newBuilder().create();
    private final Logger LOG = LogManager.getLogger("Handler");
    public final TickingTask loopThread;
    public final HashSet<Message> msgTask = new HashSet<>();
    public String myQid;
    private final BotMain main;

    public final ModuleManager manager;
    public final MsgEventManager eventManager;
    public final File FOLDER;

    public GroupConfig groupConfig = GroupConfig.newConfig();

    public long lastSendDelay;

    public final String group_id;

    public void submitMessage(Message msg) {
        msgTask.add(msg);
    }

    public MessageHandler(String group, BotMain server) {
        this.group_id = group;
        this.main = server;
        this.FOLDER = new File(FireQQ.INSTANCE.GROUP_FOLDER,group);

        manager = new ModuleManager(this);
        eventManager = new MsgEventManager(this);

        loopThread = new TickingTask("MsgHandler-" + group_id, () -> {
            if (!msgTask.isEmpty()) {
                Message msg = msgTask.stream().findAny().get();
                msgTask.remove(msg);
                postMessage(msg);
            }
        });
    }

    public void send(String msg) {
        main.broadcast(msg);
    }

    public void load() {
        try {
            loopThread.start();
            eventManager.init();
            manager.init();
        } catch (Exception e) {
            LOG.error("初始化失败", e);
        }
    }

    public void reload() {
    }

    public void stop() {
    }

    private void postMessage(Message msg) {
        try {
        } catch (Throwable e) {
            MessageBuilder builder = new MessageBuilder();
            builder.append("--------").append("Stacktrace").append("--------").append("\n");
            builder.append("发生错误!").append("\n");
            builder.append("原因: ").append(e.toString()).append("\n");
            builder.append("位于: ").append(((msg instanceof QMessage) ? ((QMessage) msg).user_id : FireQQ.OWNER_QQ)).append("\n");
            builder.append("信息: ").append("\n");
            builder.append(" -QID: {}", myQid).append("\n");
            builder.append(" -Time: {}({})", System.currentTimeMillis(), new Date().toString());
            if (msg instanceof QMessage m) {
                send(builder.get(m.group_id));
            } else {
                LOG.error(builder.get(""));
            }

            e.printStackTrace();
        }
    }

    private void processHeartbeat(HeartMessage message) {
        myQid = message.myQid;
    }

    private void processPrivateMessage(PrivateQMessage message) {
        LOG.info("{}: {}", message.user_id, message.fullMessage);
        String msg = message.message;
        if (msg.contains("test")) {
            throw new RuntimeException("Test Exception");
        }
    }

    private void processGroupMessage(QMessage message) {
        if (message.group_id.startsWith("750288446")) return;

        LOG.info("({}) {}: {}", message.message_id, message.user_id, message.fullMessage);
        String msg = message.message;
        if (msg.startsWith("exception")) {
            throw new RuntimeException("Test Exception: " + message);
        }

        if (msg.contains("原神")) {
            throw new RuntimeException("玩原神玩烂了");
        }

        eventManager.handleMessage(message);
        manager.handleMessage(message);
    }
}
