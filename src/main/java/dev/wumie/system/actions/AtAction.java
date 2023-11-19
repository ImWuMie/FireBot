package dev.wumie.system.actions;

import dev.wumie.system.Action;

public class AtAction extends Action {
    String qq;

    public AtAction(String qq) {
        this.qq = qq;
    }

    @Override
    public String toAction() {
        return "[CQ:at,qq="+qq+"]";
    }

    @Override
    public AtAction get() {
        return this;
    }
}
