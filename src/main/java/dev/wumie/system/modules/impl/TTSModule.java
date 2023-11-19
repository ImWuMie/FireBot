package dev.wumie.system.modules.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.modules.Module;
import dev.wumie.system.user.UserInfo;

public class TTSModule extends Module {
    public TTSModule() {
        super("tts");
    }

    private long lastSend = 0;

    @Override
    public void run(String[] args, QMessage message, UserInfo userInfo) {
        if (System.currentTimeMillis() - lastSend >= 5000L) {
            String to = argsToString(args);
            if (to.isEmpty()) {
                message.reply("空的TTS消息");
                return;
            }
            message.tts(to);
            lastSend = System.currentTimeMillis();
        }
    }
}
