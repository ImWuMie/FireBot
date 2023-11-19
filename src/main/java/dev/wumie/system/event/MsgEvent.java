package dev.wumie.system.event;

import dev.wumie.FireQQ;
import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.QMessage;

import java.io.File;

public abstract class MsgEvent {
    public final String eventName;
    public final File FOLDER;

    public MsgEvent(String eventName) {
        this.eventName = eventName;
        FOLDER = new File(FireQQ.FOLDER,eventName);
        if (!FOLDER.exists()) {
            FOLDER.mkdirs();
        }
    }

    public abstract void run(String message,QMessage exec);
    public void onNotice(NoticeMessage exec) {}

    protected boolean isCQCode(String msg) {
        return (msg.startsWith("[CQ") || msg.startsWith("[cq")) && msg.endsWith("]");
    }

    protected String getCQType(String msg) {
        return msg.substring("[CQ:".length(),msg.indexOf(","));
    }
}
