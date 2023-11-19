package dev.wumie.system.actions;

import dev.wumie.system.Action;

public class MsgAction extends Action {
    private final String action = "send_group_msg";
    private final Params params;

    protected MsgAction(Params params) {
        this.params = params;
    }

    public MsgAction(String group, String message) {
        this.params = new Params(group, message);
    }

    @Override
    public String toAction() {
        return beanToJson(get());
    }

    public MsgAction get() {
        return this;
    }

    public static class Params {
        String group_id;
        String message;

        public Params(String group_id, String message) {
            this.group_id = group_id;
            this.message = message;
        }
    }
}
