package dev.wumie.handlers;

import dev.wumie.FireQQ;
import dev.wumie.messages.Message;
import dev.wumie.websocket.BotMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class Handler {
    public final String name;
    protected final File FOLDER;

    public static final Logger LOG = LogManager.getLogger("Handler");

    public Handler(String name) {
        this.name = name;
         FOLDER = FireQQ.INSTANCE.FOLDER.toPath().resolve("handlers").resolve(name).toFile();
         if (!FOLDER.exists()) FOLDER.mkdirs();
    }

    public boolean canceled;
    public BotMain main;

    public abstract void message(Message message);

    public void init() {}
    public void reload() {}
    public void stop() {}

    protected void cancel() {
        this.canceled = true;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
