package dev.wumie.system.event.impl;

import com.google.gson.annotations.SerializedName;
import dev.wumie.messages.QMessage;
import dev.wumie.system.event.MsgEvent;
import dev.wumie.utils.GsonUtils;
import dev.wumie.utils.Http;

public class RuaEvent extends MsgEvent {
    public RuaEvent() {
        super("rua");
    }

    @Override
    public void run(String message, QMessage exec) {
        try {
            if (message.startsWith("rua") || message.startsWith("摸摸")) {
                String cq = message.substring(message.indexOf("["));
                if (isCQCode(cq)) {
                    if (getCQType(cq).equals("at")) {
                        String qq = cq.substring("[CQ:at,qq=".length(), cq.indexOf("]", "[CQ:at,qq=".length()));
                        Rua object = GsonUtils.jsonToBean(Http.get("https://api.52vmy.cn/api/avath/rua?qq=" + qq).sendString(), Rua.class);
                        exec.picture(object.url);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static class Rua {
        @SerializedName("code")
        public String code;
        @SerializedName("msg")
        public String msg;
        @SerializedName("url")
        public String url;
    }
}
