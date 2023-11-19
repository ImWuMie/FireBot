package dev.wumie.system;

import dev.wumie.system.actions.AtAction;
import dev.wumie.system.actions.MsgAction;
import dev.wumie.system.actions.PicAction;
import dev.wumie.utils.StringUtils;

import java.io.File;

public class MessageBuilder {
    private final StringBuilder currentBuilder;
    private final boolean autoNextLine;

    public MessageBuilder() {
        this("");
    }

    public MessageBuilder(String current) {
        this(current, false);
    }

    public MessageBuilder(String current, boolean autoNext) {
        this.currentBuilder = new StringBuilder(current);
        this.autoNextLine = autoNext;
    }

    public MessageBuilder append(String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        if (autoNextLine) {
            msg += "\n";
        }
        currentBuilder.append(msg);
        return this;
    }

    public MessageBuilder append(Action action, Object... args) {
        return this.append(action.toAction(), args);
    }

    public MessageBuilder append(Number n, Object... args) {
        return this.append(n.toString(), args);
    }

    public MessageBuilder at(String qq, Object... args) {
        return this.append(new AtAction(qq), args);
    }

    public MessageBuilder picture(String path, Object... args) {
        return this.append(new PicAction(new File(path)), args);
    }

    public String get(String group) {
        return new MsgAction(group,this.currentBuilder.toString()).toAction();
    }

    public String get() {
        return get("null");
    }

    public String getString() {
        return currentBuilder.toString();
    }
}
