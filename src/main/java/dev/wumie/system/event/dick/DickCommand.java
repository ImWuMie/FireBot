package dev.wumie.system.event.dick;

import dev.wumie.messages.QMessage;
import dev.wumie.system.user.UserInfo;

public abstract class DickCommand {
    public String name;
    public String usage;
    public String desc;
    public DickSystem system;
    public QMessage message;

    public DickCommand(String name) {
        this(name, "无","无");
    }

    public DickCommand(String name,String desc) {
        this(name,"无",desc);
    }

    public DickCommand(String name, String usage,String desc) {
        this.name = name;
        this.usage = usage;
        this.desc = desc;
    }

    public abstract void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo);

    public String getAt(String cq) {
        try {
            return cq.substring("[CQ:at,qq=".length(), cq.indexOf("]", "[CQ:at,qq=".length()));
        } catch (Exception e) {
            system.send(message,"你@的谁?我找不到!");
        }
        return null;
    }

    protected boolean isCQCode(String msg) {
        return (msg.startsWith("[CQ") || msg.startsWith("[cq")) && msg.endsWith("]");
    }

    protected String getCQType(String msg) {
        return msg.substring("[CQ:".length(),msg.indexOf(","));
    }
}
