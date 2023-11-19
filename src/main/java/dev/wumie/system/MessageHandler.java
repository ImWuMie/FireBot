package dev.wumie.system;

import com.google.gson.Gson;
import dev.wumie.FireQQ;
import dev.wumie.LaunchMode;
import dev.wumie.TickingTask;
import dev.wumie.messages.*;
import dev.wumie.system.event.MsgEventManager;
import dev.wumie.system.modules.ModuleManager;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.utils.GsonUtils;
import dev.wumie.websocket.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;

public class MessageHandler {
    public Gson gson = GsonUtils.newBuilder().create();
    private final Logger LOG = LogManager.getLogger("Handler");
    private final LaunchMode launchMode;
    public final TickingTask loopThread;
    public final HashSet<String> msgTask = new HashSet<>();
    public String myQid;
    private final WebServer main;
    public static MessageHandler INSTANCE;

    public final ModuleManager manager;
    public final MsgEventManager eventManager;
    public final NiuZiManager niuZiManager;

    public long lastSendDelay;

    public void submitMessage(String msg) {
        msgTask.add(msg);
    }

    public MessageHandler(WebServer server, LaunchMode launchMode) {
        INSTANCE = this;
        this.main = server;
        this.launchMode = launchMode;
        manager = new ModuleManager();
        eventManager = new MsgEventManager();
        niuZiManager = new NiuZiManager();
        loopThread = new TickingTask("MsgHandler", () -> {
            if (!msgTask.isEmpty()) {
                String msg = msgTask.stream().findAny().get();
                msgTask.remove(msg);
                TempMsg message = gson.fromJson(msg, TempMsg.class);
                if (message != null) {
                    postMessage(message, msg);
                }
            }
        });
    }

    public void chat(String msg) {
        main.broadcast(msg);
    }

    public void load() {
        try {
            loopThread.start();
            eventManager.init();
            manager.init();
            niuZiManager.load();
        } catch (Exception e) {
            LOG.error("初始化失败", e);
        }
    }

    public void reload() {
    }

    public void stop() {
    }

    public void onMessage(String msg) {
        submitMessage(msg);
    }

    private Message postMessage(TempMsg message, String json) {
        Message msg = new Message();
        if (message.msgType == null) return msg;

        try {
            switch (message.msgType) {
                case Message.LIFECYCLE -> {
                    msg = gson.fromJson(json, ConnectedMessage.class);
                    processLifecycle((ConnectedMessage) msg);
                }
                case Message.META_EVENT_HEART -> {
                    msg = gson.fromJson(json, HeartMessage.class);
                    processHeartbeat((HeartMessage) msg);
                }
                case Message.NOTICE_MESSAGE -> {
                    msg = gson.fromJson(json, NoticeMessage.class);
                    eventManager.handleNotice((NoticeMessage) msg);
                }
                case Message.USER_MESSAGE -> {
                    UserMessage um = gson.fromJson(json, UserMessage.class);
                    switch (um.message_type) {
                        case Message.PRIVATE_USER_MESSAGE -> {
                            msg = gson.fromJson(json, PrivateQMessage.class);
                            processPrivateMessage((PrivateQMessage) msg);
                        }
                        case Message.GROUP_USER_MESSAGE -> {
                            msg = gson.fromJson(json, QMessage.class);
                            processGroupMessage((QMessage) msg);
                        }
                        default -> LOG.info(json);
                    }
                }
                default -> {
                    LOG.info(json);
                }
            }
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
                chat(builder.get(m.group_id));
            } else {
                LOG.error(builder.get(""));
            }

            e.printStackTrace();
        }
        return msg;
    }

    private void processLifecycle(ConnectedMessage message) {

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
            throw new RuntimeException("Test Exception: "+ message);
        }

        if (msg.contains("原神")) {
            throw new RuntimeException("玩原神玩烂了");
        }

        eventManager.handleMessage(message);
        manager.handleMessage(message);
    }
}
