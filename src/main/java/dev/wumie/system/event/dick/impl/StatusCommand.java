package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.actions.AtAction;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;

public class StatusCommand extends DickCommand {
    public StatusCommand() {
        super("我的牛子", "无", "查看你的牛子");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("主人：{}({})", new AtAction(info.qq_id).toAction(), info.qq_id).append("\n");
        builder.append("名称：{}", info.name).append("\n");
        builder.append("性别：{}", info.sex).append("\n");
        builder.append("长度：{}厘米", info.niuZiCM);
        system.send(exec, builder.getNo());
    }
}
