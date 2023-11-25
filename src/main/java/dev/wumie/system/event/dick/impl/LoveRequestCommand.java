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

public class LoveRequestCommand extends DickCommand {
    public LoveRequestCommand() {
        super("搞对象", "[@对方]", "和别人搞对象");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (info.hasLover()) {
            send(exec, "你有对象了你还找对象？");
            return;
        }

        if (args.length != 0) {
            String target = getAt(args[0]);
            if (target != null) {

                if (target.equals(exec.user_id)) {
                    send(exec, "你跟自己你搞什么对象？");
                    return;
                }
                if (info.hasLover()) {
                    send(exec, "你有对象了你还找对象？");
                    return;
                }
                NiuZiInfo targetInfo = NiuZiManager.INSTANCE.get(exec.group_id,target);
                if (targetInfo == null) {
                    send(exec, "真可惜！Ta没有牛子");
                    return;
                }
                if (targetInfo.hasLover()) {
                    send(exec, "真可惜！人家有对象了");
                    return;
                }
                targetInfo.love_request = target;
                MessageBuilder builder = new MessageBuilder();
                builder.append("{} 你好，{}想跟你搞对象", new AtAction(target).toAction(), new AtAction(exec.user_id).toAction()).append("\n");
                builder.append("输入命令「处理请求 搞对象 同意/不同意」");
                send(exec, builder.getString());
            }
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
