package dev.wumie.system.modules.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.modules.Module;

public class EWMModule extends Module {
    public EWMModule() {
        super("ewm","2wm","qr");
    }

    @Override
    public void run(String[] args, QMessage message) {
        String to = argsToString(args);
        if (to.isEmpty()) {
            message.reply("空Message.");
            return;
        }
        message.picture("");
    }
}
