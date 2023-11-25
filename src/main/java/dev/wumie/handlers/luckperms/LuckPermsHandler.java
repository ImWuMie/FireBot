package dev.wumie.handlers.luckperms;

import dev.wumie.FireQQ;
import dev.wumie.handlers.Handler;
import dev.wumie.messages.Message;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.system.modules.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class LuckPermsHandler extends Handler {
    public PermList permsMap = new PermList();
    public static LuckPermsHandler INSTANCE;
    public final File PERMS_JSON = new File(FOLDER,"perms.json");

    public LuckPermsHandler() {
        super("luck_perms");
        INSTANCE = this;
    }

    public boolean hasPerms(String qq,Module m) {
        return hasPerms(qq,m.permName);
    }

    public boolean hasPerms(String qq,String perm) {
        PermsInfo info = permsMap.get(qq);
        if (info == null) {
            info = permsMap.getByGroup("default");
            info.addUser(qq);
            savePerms();
        }

        if (info.perms.contains(perm)) {
            return true;
        }

        if (info.perms.contains("*")) {
            return true;
        }
        for (String p : info.perms) {
            // fire.* fire.tts
            if (p.endsWith("*")) {
                String a = p.substring(0,p.length() - ".*".length());
                if (perm.startsWith(a)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void init() {
        loadPerms();
    }

    @Override
    public void reload() {
        loadPerms();
    }

    @Override
    public void stop() {
        savePerms();
    }

    @Override
    public void message(Message message) {
        String msg = "";
        if (message instanceof QMessage m) {
            msg = m.message;
        } else if (message instanceof PrivateQMessage m) {
            msg = m.message;
        }
        if (msg.startsWith("lp")) {
            cancel();
            String cmd = msg.substring("lp".length());
            String[] args = cmd.split(" ");
            if (args.length == 0) return;

            String commandName = args[0];
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            onMsg(message,args);
        }
    }

    private void onMsg(Message message,String[] args) {
        if (args.length == 0) {
            showUsage(message);
            return;
        }
        switch (args[0]) {
            case "move" -> {

            }
        }
    }

    private void showUsage(Message message) {
        if (message instanceof QMessage m) {
            MessageBuilder builder = new MessageBuilder("",true);
            builder.append("lp move [qq] [group]");
            builder.append("lp set [group] [perms] [add/del]");
            builder.append("lp create [name]");
            builder.append("lp remove [name]");
            m.send(builder.getString());
        }
    }

    public void loadPerms() {
        if (!PERMS_JSON.exists()) {
            savePerms();
        }

        String text = null;
        try {
            text = Files.readString(PERMS_JSON.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PermList l = FireQQ.INSTANCE.gson.fromJson(text, PermList.class);
        if (l == null) {
            savePerms();
        } else {
            this.permsMap.clear();
            this.permsMap.addAll(l);
        }
        LOG.info("Loaded {} perms.", permsMap.size());
    }

    public void savePerms() {
        try {
            if (!PERMS_JSON.exists()) {
                PERMS_JSON.createNewFile();
                permsMap.add(PermsInfo.newInfo());
            }
            String json = FireQQ.INSTANCE.gson.toJson(permsMap);
            Files.writeString(PERMS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
