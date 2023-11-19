package dev.wumie.system.event.dick;

import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.event.MsgEvent;
import dev.wumie.system.event.dick.impl.*;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DickSystem extends MsgEvent {
    private final List<DickCommand> commands = new ArrayList<>();
    private final String PREFIX = "";

    public DickSystem() {
        super("dick_system");
        commands.add(new RequestCommand());
        commands.add(new PKCommand());
        commands.add(new StatusCommand());
        commands.add(new GetCommand());
        commands.add(new SignCommand());
        commands.add(new DoiCommand());
        commands.add(new ChangeSexCommand());
        commands.add(new LoveRequestCommand());
        commands.add(new NameCommand());
        commands.add(new LoverCommand());
        commands.add(new LeaveCommand());
    }

    @Override
    public void run(String message, QMessage exec, UserInfo userInfo) {
        String user = exec.user_id;
        String msg = exec.message;
        if (msg.length() == 0) return;
        if (msg.startsWith("牛子系统")) {
            String[] args = msg.split(" ");
            MessageBuilder builder = new MessageBuilder();
            if (args.length == 2) {
                String commandName = args[1];
                DickCommand command = null;
                for (DickCommand c : commands) {
                    if (c.name.equalsIgnoreCase(commandName)) {
                        command = c;
                        break;
                    }
                }

                if (command == null) {
                    send(exec, "没这个命令发什么发");
                    return;
                }

                send(exec, "{}  {}\n{}", command.name, command.usage, command.desc);
            } else if (args.length == 1) {
                for (DickCommand command : commands) {
                    builder.append(command.name).append("  ").append(command.usage).append("  ").append(command.desc).append("\n");
                }

                send(exec, builder.getString());
            }
            return;
        }

        if (msg.startsWith(PREFIX)) {
            String cmd = msg.substring(PREFIX.length());
            String[] args = cmd.split(" ");
            if (args.length == 0) return;

            String commandName = args[0];
            for (DickCommand command : commands) {
                boolean isCommand = commandName.equalsIgnoreCase(command.name);
                if (isCommand) {
                    NiuZiInfo info = NiuZiManager.INSTANCE.get(user);
                    if (info == null && !commandName.equals("领养牛子")) {
                        send(exec, "没有牛子你用什么用滚");
                        return;
                    }

                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    command.system = this;
                    command.message = exec;
                    command.run(newArgs, exec, info, this, userInfo);
                }
            }
        }
    }

    public void send(QMessage exec, String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        MessageBuilder builder = new MessageBuilder();
        builder.append("--------牛子系统--------").append("\n");
        builder.append(msg);
        exec.send(builder.getString());
    }

    public void success(QMessage exec) {
        this.send(exec, "行了行了行了");
    }
}
