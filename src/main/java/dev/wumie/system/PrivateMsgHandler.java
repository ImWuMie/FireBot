package dev.wumie.system;

import dev.wumie.TickingTask;
import dev.wumie.messages.Message;
import dev.wumie.websocket.BotMain;

import java.util.HashSet;

public class PrivateMsgHandler {
    public final BotMain main;
    public static PrivateMsgHandler INSTANCE;

    public final TickingTask loopThread;
    public final HashSet<Message> msgTask = new HashSet<>();

    public PrivateMsgHandler(BotMain main) {
        INSTANCE = this;
        this.main = main;

        loopThread = new TickingTask("PrivateHandler", () -> {
            if (!msgTask.isEmpty()) {
                Message msg = msgTask.stream().findAny().get();
                msgTask.remove(msg);
                postMessage(msg);
            }
        });
    }

    public void submitMsg(Message message) {

    }

    private void postMessage(Message msg) {

    }

    public void load() {

    }
}
