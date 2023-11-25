package dev.wumie.system.modules.impl;

import dev.wumie.FireQQ;
import dev.wumie.messages.QMessage;
import dev.wumie.system.modules.Module;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.GsonUtils;
import dev.wumie.utils.Infos.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CheckModule extends Module {
    public CheckModule() {
        super("check","fire.check", "ch","cha");
    }

    @Override
    public void run(String[] args, QMessage message, UserInfo userInfo) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("qq")) {
                String qq = args[1];
                Runnable run = () -> {
                    StringBuilder stringBuilder;
                    String inputLine;
                    URL url;
                    BufferedReader in;
                    QQInfo info;

                    stringBuilder = new StringBuilder();
                    try {
                        url = new URL("https://zy.xywlapi.cc/qqapi?qq=" + qq);
                        in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

                        while ((inputLine = in.readLine()) != null) {
                            stringBuilder.append(inputLine);
                        }
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }

                    //message.send(stringBuilder.toString());
                    info = GsonUtils.jsonToBean(stringBuilder.toString(), QQInfo.class);
                    if (info != null) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(stringBuilder.toString()).append("\n");
                        builder.append("\n");
                        builder.append("返回状态: " + info.getStatus()).append("\n");
                        builder.append("返回消息: " + info.getMessage()).append("\n");
                        builder.append("QQ: " + qq).append("\n");
                        builder.append("手机号: " + info.getPhone()).append("\n");
                        builder.append("地区: " + info.getPhonediqu());
                        message.send(builder.toString());
                    }
                };

                FireQQ.INSTANCE.async.submit(run);
            }
        }
    }
}
