package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.utils.RandomUtils;
import dev.wumie.utils.Times;

public class SignCommand extends DickCommand {
    public SignCommand() {
        super("锻炼牛子", "无", "增加你的牛子长度");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system) {
        boolean canSign = (info.last_sign + Times.DAY) <= System.currentTimeMillis();

        if (canSign) {
            double plus = RandomUtils.nextDouble(2, 7);
            info.niuZiCM += plus;
            system.send(exec, "锻炼成功 你的牛子增加了{}厘米", plus);
            info.last_sign = System.currentTimeMillis();
        } else system.send(exec, "你今天已经锻炼过了");
    }
}
