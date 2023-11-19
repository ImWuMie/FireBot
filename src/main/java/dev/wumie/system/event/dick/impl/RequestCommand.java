package dev.wumie.system.event.dick.impl;

import dev.wumie.messages.Message;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;

public class RequestCommand extends DickCommand {
    public RequestCommand() {
        super("处理请求", "[搞对象/分手] [同意/不同意]","管理你的请求");
    }

    private final String PREFIX = "--------对象系统--------";

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system) {
        if (args.length == 2) {
            switch (args[0]) {
                case "搞对象" -> {
                    if (!info.hasLoveReq()) {
                        system.send(exec, "没有待处理的请求");
                        return;
                    }

                    switch (args[1]) {
                        case "同意" -> {
                            MessageBuilder builder = new MessageBuilder();
                            builder.append(PREFIX).append("\n");
                            builder.at(info.love_request).append(" ").append("恭喜！！！！对方同意了你的请求");
                            exec.send(builder.getNo());
                            info.love(info.love_request);
                        }
                        case "不同意" -> {
                            MessageBuilder builder = new MessageBuilder();
                            builder.append(PREFIX).append("\n");
                            builder.at(info.love_request).append(" ").append("真遗憾……对方没有同意你的请求");
                            exec.send(builder.getNo());
                            info.love(info.qq_id);
                        }
                    }

                    NiuZiManager.INSTANCE.saveDicks();
                }
                case "分手" -> {
                    switch (args[1]) {
                        case "同意" -> {
                            MessageBuilder builder = new MessageBuilder();
                            builder.append(PREFIX).append("\n");
                            builder.at(info.fenshou_data).append(" ").append("对方同意了你的分手请求……");
                            exec.send(builder.getNo());
                            info.leave();
                        }
                        case "不同意" -> {
                            MessageBuilder builder = new MessageBuilder();
                            builder.append(PREFIX).append("\n");
                            builder.at(info.fenshou_data).append(" ").append("对方没有同意你的请求");
                            exec.send(builder.getNo());
                            info.fenshou_data = info.qq_id;
                        }
                    }

                    NiuZiManager.INSTANCE.saveDicks();
                }
            }
        }
    }
}
