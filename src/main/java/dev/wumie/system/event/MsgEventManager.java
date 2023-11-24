package dev.wumie.system.event;

import dev.wumie.FireQQ;
import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageHandler;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.impl.ImageSaverEvent;
import dev.wumie.system.event.impl.PlusOneEvent;
import dev.wumie.system.event.impl.RuaEvent;
import dev.wumie.system.user.UserInfo;
import dev.wumie.system.user.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MsgEventManager {
    public static final Logger LOG = LogManager.getLogger("MsgEvent");
    public final List<MsgEvent> events = new CopyOnWriteArrayList<>();
    public static String PREFIX = "";
    private File FOLDER;

    public final MessageHandler handler;

    public MsgEventManager(MessageHandler handler) {
        this.handler = handler;
    }

    public void init() {
        FOLDER = new File(handler.FOLDER, "events");
        if (!FOLDER.exists()) FOLDER.mkdirs();

        add(ImageSaverEvent.class);
        add(PlusOneEvent.class);
        add(RuaEvent.class);
        //add(AntiRecallEvent.class);
        add(DickSystem.class);

        events.forEach((e) -> {
            e.FOLDER = new File(FOLDER, e.eventName);
            if (!e.FOLDER.exists()) e.FOLDER.mkdirs();
        });
    }

    public void handleNotice(NoticeMessage message) {
        for (MsgEvent event : events) {
            event.onNotice(message);
        }
    }

    public void handlePrivateMessage(PrivateQMessage privateMessage) {
        String msg = privateMessage.message;
        if (msg.length() == 0) return;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return;
            for (MsgEvent event : events) {
                UserInfo info = UserManager.INSTANCE.get(privateMessage.user_id);
                event.onPrivateMsg(cmd, privateMessage, info);
            }
        }
    }

    public void handleMessage(QMessage groupMessage) {
        String msg = groupMessage.message;
        if (msg.length() == 0) return;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return;
            for (MsgEvent event : events) {
                UserInfo info = UserManager.INSTANCE.get(groupMessage.user_id);
                event.run(cmd, groupMessage, info);
            }
        }
    }

    public void add(MsgEvent event) {
        events.add(event);
    }

    public void add(Class<? extends MsgEvent> klass) {
        try {
            events.add(klass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends MsgEvent> T get(Class<T> klass) {
        return (T) events.stream().filter((c) -> c.getClass().equals(klass)).findFirst().orElse(null);
    }
}
