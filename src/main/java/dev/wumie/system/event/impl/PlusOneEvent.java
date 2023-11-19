package dev.wumie.system.event.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.event.MsgEvent;

public class PlusOneEvent extends MsgEvent {
    public PlusOneEvent() {
        super("plus_one");
    }

    private String[] msgs = new String[4];
    private int index = 0;

    @Override
    public void run(String message, QMessage exec) {
        msgs[index] = message;
        index++;
        if (index >= msgs.length) {
            index = 0;
        }

        boolean isNull = msgs[0] == null || msgs[1] == null || msgs[2] == null || msgs[3] != null;

        if (!isNull) {
            String last = msgs[0];
            boolean should = true;
            for (String msg : msgs) {
                if (last.equals(msg)) {
                    last = msg;
                } else {
                    should = false;
                    break;
                }
            }

            if (should) {
                exec.send(last);
                msgs = new String[4];
            }
        }
    }
}
