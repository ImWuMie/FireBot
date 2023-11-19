package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.actions.AtAction;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;

public class GetCommand extends DickCommand {
    public GetCommand() {
        super("领养牛子","无","领养一只牛子");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system) {
        if (info == null) {
            NiuZiManager.INSTANCE.createNiuZi(exec.user_id);
            system.send(exec,new AtAction(exec.user_id).toAction() +  " 领养了，输入「我的牛子」查看你的牛子信息");
        } else {
            system.send(exec,"？你有了你还领，有病");
        }
    }
}
