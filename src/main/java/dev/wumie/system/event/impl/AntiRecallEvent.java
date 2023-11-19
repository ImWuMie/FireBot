package dev.wumie.system.event.impl;

import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.event.MsgEvent;
import dev.wumie.system.user.UserInfo;

public class AntiRecallEvent extends MsgEvent {
    public AntiRecallEvent() {
        super("anti_recall");
    }

    public QMessage[] messages = new QMessage[256];
    public int currentMsgIndex;
    private int offer = 1;

    @Override
    public void run(String message, QMessage exec, UserInfo userInfo) {
       /* messages[currentMsgIndex] = exec;
        currentMsgIndex += offer;
        if (currentMsgIndex >= messages.length) {
            offer = -1;
        }

        if (currentMsgIndex == 0) {
            offer = 1;
        }*/
    }

    public QMessage get(String mid) {
        for (QMessage message : messages) {
            if (message == null) continue;

            if (message.message_id.equals(mid)) return message;
        }

        return null;
    }

    @Override
    public void onNotice(NoticeMessage exec) {
       /* if (exec.notice_type.equalsIgnoreCase(Message.NOTICE_GROUP_RECALL_MESSAGE)) {
            String qq = exec.user_id;
            QMessage message = get(exec.message_id);
            if (message != null) {
                FireQQ.LOG.info("{}撤回了消息\n{}", qq, message.fullMessage);
                //message.send("{}撤回了消息\n{}", qq, message.fullMessage);
            }
        }*/
    }
}
