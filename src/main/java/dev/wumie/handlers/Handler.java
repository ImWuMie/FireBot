package dev.wumie.handlers;

import dev.wumie.messages.Message;
import dev.wumie.websocket.BotMain;

public abstract class Handler {
    public final String name;

    public Handler(String name) {
        this.name = name;
    }

    public boolean canceled;
    public BotMain main;

    public abstract void message(Message message);

    public void init() {}

    protected void cancel() {
        this.canceled = true;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
