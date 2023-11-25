package dev.wumie.handlers.luckperms;

import dev.wumie.handlers.Handler;
import dev.wumie.messages.Message;

import java.util.HashMap;
import java.util.List;

public class LuckPermsHandler extends Handler {
    public HashMap<String, List<String>> permsMap = new HashMap<>();

    public LuckPermsHandler() {
        super("luck_perms");
    }

    public boolean hasPerms(String perm) {
        return true;
    }

    @Override
    public void message(Message message) {

    }
}
