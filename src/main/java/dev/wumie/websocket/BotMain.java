package dev.wumie.websocket;

import com.google.gson.Gson;
import dev.wumie.FireQQ;
import dev.wumie.handlers.Handler;
import dev.wumie.handlers.HandlesManager;
import dev.wumie.messages.*;
import dev.wumie.system.MessageHandler;
import dev.wumie.system.PrivateMsgHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BotMain extends WebSocketServer {
    public static BotMain INSTANCE;

    public final Map<String, MessageHandler> handlers = new HashMap<>();
    public PrivateMsgHandler privateHandler;
    public final Gson gson = FireQQ.INSTANCE.gson;
    private final Logger LOG = LogManager.getLogger("MsgServer");
    public final HandlesManager handlesManager = new HandlesManager(this);

    public BotMain(int port) {
        super(new InetSocketAddress(port));
        INSTANCE = this;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message != null && !message.isEmpty()) {
            try {
                decodeMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void decodeMessage(String json) {
        Message msg = null;
        TempMsg message = gson.fromJson(json, TempMsg.class);
        if (message.msgType == null) return;

        switch (message.msgType) {
            case Message.LIFECYCLE -> msg = gson.fromJson(json, ConnectedMessage.class);
            case Message.META_EVENT_HEART -> msg = gson.fromJson(json, HeartMessage.class);
            case Message.NOTICE_MESSAGE -> msg = gson.fromJson(json, NoticeMessage.class);
            case Message.USER_MESSAGE -> {
                UserMessage um = gson.fromJson(json, UserMessage.class);
                switch (um.message_type) {
                    case Message.GROUP_USER_MESSAGE -> {
                        msg = gson.fromJson(json, QMessage.class);
                        QMessage mess = (QMessage) msg;
                        String group = mess.group_id;
                        if (!handlers.containsKey(group)) {
                            MessageHandler handler = new MessageHandler(group, this);
                            handler.load();
                            handlers.put(group, handler);
                        }
                    }
                    case Message.PRIVATE_USER_MESSAGE -> {
                        msg = gson.fromJson(json, PrivateQMessage.class);
                        if (privateHandler == null) {
                            privateHandler = new PrivateMsgHandler(this);
                            privateHandler.load();
                        }
                    }
                    default -> LOG.info(json);
                }
            }
            default -> LOG.info(json);
        }

        if (msg != null && !handlesManager.checkMessage(msg)) {
            if (msg instanceof QMessage m) {
                handlers.get(m.group_id).submitMessage(msg);
            }
            if (msg instanceof PrivateQMessage m) {
                privateHandler.submitMsg(m);
            }
        }
    }

    public BotMain addFirst(Handler handler) {
        handlesManager.addFirst(handler);
        return this;
    }

    public BotMain addFirst(Class<? extends Handler> klass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        handlesManager.addFirst(klass);
        return this;
    }

    public BotMain addLast(Handler handler) {
        handlesManager.addLast(handler);
        return this;
    }

    public BotMain addLast(Class<? extends Handler> klass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        handlesManager.addLast(klass);
        return this;
    }

    public void load() {
        handlesManager.init();
    }

    public void stopMain() {
        handlers.forEach((g,h) -> h.stop());
        handlesManager.stop();
    }

    public void reload() {
        handlers.forEach((g,h) -> h.reload());
        handlesManager.reload();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    }

    @Override
    public void onStart() {
        FireQQ.INSTANCE.LOG.info("--FireBot Server--");
        FireQQ.INSTANCE.LOG.info("Port: " + getPort());
    }
}
