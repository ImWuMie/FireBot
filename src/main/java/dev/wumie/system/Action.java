package dev.wumie.system;

import com.google.gson.Gson;
import dev.wumie.utils.GsonUtils;

public abstract class Action {
    protected static final Gson gson = GsonUtils.newBuilder().create();

    public abstract String toAction();
    public abstract Action get();

    @Override
    public String toString() {
        return toAction();
    }

    protected String beanToJson(Action action) {
        return gson.toJson(action.get());
    }
}
