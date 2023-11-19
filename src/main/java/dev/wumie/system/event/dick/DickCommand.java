package dev.wumie.system.event.dick;

import dev.wumie.messages.QMessage;

public abstract class DickCommand {
    public String name;
    public String usage;
    public String desc;

    public DickCommand(String name) {
        this(name, "无","无");
    }

    public DickCommand(String name, String usage,String desc) {
        this.name = name;
        this.usage = usage;
        this.desc = desc;
    }

    public abstract void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system);

    public String getAt(String cq) {
        return cq.substring("[CQ:at,qq=".length(), cq.indexOf("]", "[CQ:at,qq=".length()));
    }

    protected boolean isCQCode(String msg) {
        return (msg.startsWith("[CQ") || msg.startsWith("[cq")) && msg.endsWith("]");
    }

    protected String getCQType(String msg) {
        return msg.substring("[CQ:".length(),msg.indexOf(","));
    }
}
