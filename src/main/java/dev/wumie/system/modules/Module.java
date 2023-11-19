package dev.wumie.system.modules;

import dev.wumie.FireQQ;
import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    public final String name;
    public final List<String> aliases = new ArrayList<>();
    public QMessage message;
    public PrivateQMessage privateQMessage;
    protected final File MODULES_FOLDER = new File(FireQQ.FOLDER,"modules");
    protected final File FOLDER;

    public Module(String name, String... aliases) {
        this.name = name;
        Collections.addAll(this.aliases, aliases);
        FOLDER = new File(MODULES_FOLDER,name);
        if (!MODULES_FOLDER.exists()) MODULES_FOLDER.mkdirs();

        if (!FOLDER.exists()) FOLDER.mkdirs();
    }

    public abstract void run(String[] args,QMessage message);

    protected String argsToString(String[] args) {
        StringBuilder s = new StringBuilder();
        for (String arg : args) {
            s.append(arg).append(" ");
        }
        return s.toString();
    }
}
