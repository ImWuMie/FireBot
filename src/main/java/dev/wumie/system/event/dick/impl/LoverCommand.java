package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.actions.AtAction;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.StringUtils;

public class LoverCommand extends DickCommand {
    public LoverCommand() {
        super("我的对象", "查看你的对象的牛子信息");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (info.hasLover()) {
            NiuZiInfo lover = NiuZiManager.INSTANCE.get(info.lover);
            MessageBuilder builder = new MessageBuilder();
            builder.append("你的对象：{}({})",new AtAction(lover.qq_id).toAction(),lover.qq_id).append("\n");
            builder.append("Ta的牛子：{}",lover.name).append("\n");
            builder.append("牛子性别：{}",lover.sex).append("\n");
            builder.append("牛子长度：{}厘米",lover.niuZiCM);
            send(exec,builder.getString());
        } else {
            send(exec,"你没有对象你在这叭叭什么？");
        }
    }

    public void send(QMessage exec, String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        MessageBuilder builder = new MessageBuilder();
        builder.append("--------对象系统--------").append("\n");
        builder.append(msg);
        exec.send(builder.getString());
    }
}
