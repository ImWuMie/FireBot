package dev.wumie.system.modules;

import dev.wumie.FireQQ;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageHandler;
import dev.wumie.system.user.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    public final String name;
    public final String permName;
    public final List<String> aliases = new ArrayList<>();
    public QMessage message;
    public PrivateQMessage privateQMessage;
    public File FOLDER;
    public MessageHandler handler;

    public Module(String name, String permName,String... aliases) {
        this.name = name;
        this.permName = permName;
        Collections.addAll(this.aliases, aliases);
    }

    public abstract void run(String[] args, QMessage message, UserInfo userInfo);
    public void onPrivate(String[] args, PrivateQMessage message, UserInfo userInfo) {}

    protected String argsToString(String[] args) {
        StringBuilder s = new StringBuilder();
        for (String arg : args) {
            s.append(arg).append(" ");
        }
        return s.toString();
    }
}
