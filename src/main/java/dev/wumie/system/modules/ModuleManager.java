package dev.wumie.system.modules;

import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageHandler;
import dev.wumie.system.modules.impl.CheckModule;
import dev.wumie.system.modules.impl.JKModule;
import dev.wumie.system.modules.impl.TTSModule;
import dev.wumie.system.user.UserInfo;
import dev.wumie.system.user.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {
    public static final Logger LOG = LogManager.getLogger("Module");
    public final List<Module> modules = new CopyOnWriteArrayList<>();
    public static String PREFIX = "";

    private final MessageHandler handler;

    public ModuleManager(MessageHandler handler) {
        this.handler = handler;
    }

    public void init() {
        add(TTSModule.class);
        add(CheckModule.class);
        add(JKModule.class);
    }

    public void handlePrivateMessage(PrivateQMessage privateMessage) {
        String msg = privateMessage.message;
        if (msg.length() == 0) return;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return;

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
                    command.privateQMessage = privateMessage;
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

                    UserInfo info = UserManager.INSTANCE.get(privateMessage.user_id);
                    command.onPrivate(newArgs, privateMessage, info);

                    return;
                }
            }
        }
    }

    public void handleMessage(QMessage groupMessage) {
        String msg = groupMessage.message;
        if (msg.length() == 0) return;
        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");

            if (args.length == 0) return;

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

                    UserInfo info = UserManager.INSTANCE.get(groupMessage.user_id);
                    command.run(newArgs, groupMessage, info);

                    return;
                }
            }
        }
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
