package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;

public class NameCommand extends DickCommand {
    public NameCommand() {
        super("改牛子名","[要改的名字]","改你的牛子的名字，支持空格，最长12个字");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system) {
        if (args.length == 0) {
            system.send(exec,"你牛子要改的名字忘给了，我怎么改？");
            return;
        }

        String name = args[0];
        if (name.length() > 12) {
            system.send(exec,"你牛子名字太长了，最多只支持12个字");
            return;
        }

        info.name = name;
        system.success(exec);
        NiuZiManager.INSTANCE.saveDicks();
    }
}
