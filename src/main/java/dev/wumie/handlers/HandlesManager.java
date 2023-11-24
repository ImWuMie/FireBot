package dev.wumie.handlers;

import dev.wumie.messages.Message;
import dev.wumie.websocket.BotMain;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class HandlesManager {
    public final LinkedList<Handler> handlers = new LinkedList<>();
    public static HandlesManager INSTANCE;
    private final BotMain server;

    public HandlesManager(BotMain server) {
        this.server = server;
        INSTANCE = this;
    }

    public void addFirst(Handler handler) {
        handlers.addFirst(handler);
    }

    public void addFirst(Class<? extends Handler> klass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.addFirst(klass.getConstructor().newInstance());
    }

    public void addLast(Handler handler) {
        handlers.addLast(handler);
    }

    public void addLast(Class<? extends Handler> klass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.addLast(klass.getConstructor().newInstance());
    }

    public void init() {
        try {
            //add(LuckPermsHandler.class);

            postInit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void postInit() {
        handlers.forEach((h) -> {
            h.main = server;
            h.init();
        });
    }

    public boolean checkMessage(Message message) {
        boolean cancel = false;
        for (Handler handler : handlers) {
            handler.message(message);
            if (handler.canceled) {
                handler.canceled = false;
                cancel = true;
            }
        }

        return cancel;
    }
}
