package dev.wumie.system.event;

import dev.wumie.FireQQ;
import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.user.UserInfo;

import java.io.File;

public abstract class MsgEvent {
    public final String eventName;
    public File FOLDER;

    public MsgEvent(String eventName) {
        this.eventName = eventName;
    }

    public abstract void run(String message, QMessage exec, UserInfo userInfo);
    public void onPrivateMsg(String message,PrivateQMessage exec,UserInfo userInfo) {}
    public void onNotice(NoticeMessage exec) {}

    protected boolean isCQCode(String msg) {
        return (msg.startsWith("[CQ") || msg.startsWith("[cq")) && msg.endsWith("]");
    }

    protected String getCQType(String msg) {
        return msg.substring("[CQ:".length(),msg.indexOf(","));
    }
}
