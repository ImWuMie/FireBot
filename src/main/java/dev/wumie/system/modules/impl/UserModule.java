package dev.wumie.system.modules.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.modules.Module;
import dev.wumie.system.user.UserInfo;

public class UserModule extends Module {
    public UserModule() {
        super("user", "admin.user");
    }

    @Override
    public void run(String[] args, QMessage message, UserInfo userInfo) {
    }
}
