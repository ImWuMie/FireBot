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

public class LeaveCommand extends DickCommand {
    public LeaveCommand() {
        super("我要分手", "和你的对象分手");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (!info.hasLover()) {
            send(exec,"你没对象你分哪门子手？");
            return;
        }
        NiuZiInfo lover = NiuZiManager.INSTANCE.get(exec.group_id,info.lover);
        info.fenshou_data = lover.qq_id;
        String a = "{0} 你好，{} 想跟你分手\n";
        String b = "输入命令「处理请求 分手 同意/不同意」";
        send(exec,a+b,new AtAction(lover.qq_id).toAction(),new AtAction(info.qq_id).toAction());
    }

    public void send(QMessage exec, String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        MessageBuilder builder = new MessageBuilder();
        builder.append("--------对象系统--------").append("\n");
        builder.append(msg);
        exec.send(builder.getString());
    }
}
