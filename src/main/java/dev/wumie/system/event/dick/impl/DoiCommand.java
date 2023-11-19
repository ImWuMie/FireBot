package dev.wumie.system.event.dick.impl;

import dev.wumie.FireQQ;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.system.user.Rank;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.RandomUtils;
import dev.wumie.utils.StringUtils;

public class DoiCommand extends DickCommand {
    public DoiCommand() {
        super("贴贴", "和对象贴贴！");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (!info.hasLover()) {
            send(exec, "贴你ma 你都没人跟你贴");
            return;
        }
        boolean noCooldown = userInfo != null && userInfo.rank_level >= Rank.Admin.level && FireQQ.configs.dick_admin_no_cooldown;

        String target = info.getLover();
        NiuZiInfo targetInfo = NiuZiManager.INSTANCE.get(target);
        if (targetInfo == null) return;

        double length = RandomUtils.nextDouble(0,121);
        long now = System.currentTimeMillis();
        if (!noCooldown && (targetInfo.tietie_data - now > 0 || info.tietie_data - now > 0)) {
            send(exec,"你俩能不能消停会儿 都粘掉皮了\uD83D\uDE05 等 {}ms 再贴", targetInfo.tietie_data - now);
            return;
        }
        int h = RandomUtils.nextInt(0,13);
        long data = (12 + h + (long) RandomUtils.nextDouble(0,1)) * FireQQ.configs.dick_doi_delay;
        info.tietie_data = now + data;
        targetInfo.tietie_data = now + data;
        info.niuZiCM += length;
        targetInfo.niuZiCM += length;
        String msg = (h < 6) ? "" : "但你俩已经虚了，所以你们得等";
        send(exec,"行行行 贴贴贴 一会儿粘上了\uD83D\uDE05 加了 {} 厘米，{} {}ms 后才可以再次贴贴", length, msg, data);
    }

    public void send(QMessage exec, String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        MessageBuilder builder = new MessageBuilder();
        builder.append("--------对象系统--------").append("\n");
        builder.append(msg);
        exec.send(builder.getString());
    }
}
