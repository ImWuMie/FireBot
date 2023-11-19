package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.system.user.UserInfo;

public class ChangeSexCommand extends DickCommand {
    public ChangeSexCommand() {
        super("变女性", "转变为女性，扣除50厘米");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (info.sex.equals(NiuZiInfo.SexType.FEMALE.name)) {
            system.send(exec, "你已经是女的了，怎么？");
            return;
        }
        info.niuZiCM -= 50.0;
        info.sex = NiuZiInfo.SexType.FEMALE.name;
        system.success(exec);
        NiuZiManager.INSTANCE.saveDicks();
    }
}
