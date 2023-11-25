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

    public boolean hasHandler(String handlerName) {
        return handlers.stream().anyMatch((f) -> f.name.equals(handlerName));
    }

    public boolean hasHandler(Class<? extends Handler> hClass) {
        return handlers.stream().anyMatch((f) -> f.getClass().equals(hClass));
    }

    public <T extends Handler> T getHandler(String handlerName) {
        return (T) handlers.stream().filter((f) -> f.name.equals(handlerName)).findFirst().orElse(null);
    }

    public <T extends Handler> T getHandler(Class<T> hClass) {
        return (T) handlers.stream().filter((f) -> f.getClass().equals(hClass)).findFirst().orElse(null);
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

    public void reload() {
        handlers.forEach(Handler::reload);
    }
    public void stop() {
        handlers.forEach(Handler::stop);
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
