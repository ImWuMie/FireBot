package dev.wumie.system.modules;

import dev.wumie.messages.NoticeMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.modules.impl.CheckModule;
import dev.wumie.system.modules.impl.JKModule;
import dev.wumie.system.modules.impl.TTSModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public final Logger LOG = LogManager.getLogger("Module");
    public final List<Module> modules = new CopyOnWriteArrayList<>();
    public static String PREFIX = "";

    public static ModuleManager INSTANCE;

    public ModuleManager() {
        INSTANCE = this;
    }

    public void init() {
        add(TTSModule.class);
        add(CheckModule.class);
        add(JKModule.class);
    }

    public boolean handleMessage(QMessage groupMessage) {
        String msg = groupMessage.message;
        if (msg.length() == 0) return false;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return false;

            String commandName = args[0];
            for (Module command : modules) {
                boolean isCommand = false;

                if (commandName.equalsIgnoreCase(command.name)) {
                    isCommand = true;
                } else {
                    for (String a : command.aliases) {
                        if (commandName.equalsIgnoreCase(a)) {
                            isCommand = true;
                            break;
                        }
                    }
                }

                if (isCommand) {
                    command.message = groupMessage;
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    command.run(newArgs,groupMessage);

                    return true;
                }
            }

            return true;
        }
        return true;
    }

    public void add(Module module) {
        modules.add(module);
    }

    public void add(Class<? extends Module> klass) {
        try {
            modules.add(klass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Module> T get(Class<T> klass) {
        return (T) modules.stream().filter((c) -> c.getClass().equals(klass)).findFirst().orElse(null);
    }
}
