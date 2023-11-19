package dev.wumie.system.event;

import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.impl.AntiRecallEvent;
import dev.wumie.system.event.impl.ImageSaverEvent;
import dev.wumie.system.event.impl.PlusOneEvent;
import dev.wumie.system.event.impl.RuaEvent;
import dev.wumie.system.modules.Module;
import dev.wumie.system.modules.impl.TTSModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MsgEventManager {
    public final Logger LOG = LogManager.getLogger("MsgEvent");
    public final List<MsgEvent> events = new CopyOnWriteArrayList<>();
    public static String PREFIX = "";

    public static MsgEventManager INSTANCE;

    public MsgEventManager() {
        INSTANCE = this;
    }

    public void init() {
        add(ImageSaverEvent.class);
        add(PlusOneEvent.class);
        add(RuaEvent.class);
        //add(AntiRecallEvent.class);
        add(DickSystem.class);
    }

    public void handleNotice(NoticeMessage message) {
        for (MsgEvent event : events) {
            event.onNotice(message);
        }
    }

    public boolean handleMessage(QMessage groupMessage) {
        String msg = groupMessage.message;
        if (msg.length() == 0) return false;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return false;
            for (MsgEvent event : events) {
                event.run(cmd,groupMessage);
            }

            return true;
        }
        return true;
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
