package dev.wumie.system.event.dick.impl;

import dev.wumie.FireQQ;
import dev.wumie.messages.QMessage;
import dev.wumie.system.actions.AtAction;
import dev.wumie.system.event.dick.DickCommand;
import dev.wumie.system.event.dick.DickSystem;
import dev.wumie.system.event.dick.NiuZiInfo;
import dev.wumie.system.event.dick.NiuZiManager;
import dev.wumie.system.user.Rank;
import dev.wumie.system.user.UserInfo;
import dev.wumie.utils.RandomUtils;

public class PKCommand extends DickCommand {
    public PKCommand() {
        super("比划比划", "[@对方]", "比划一下，赢加长度输减长度，断掉双方都减长度");
    }

    @Override
    public void run(String[] args, QMessage exec, NiuZiInfo info, DickSystem system, UserInfo userInfo) {
        if (args.length == 0) {
            system.send(exec, "不艾特人家我怎么知道你想跟谁比划？");
            return;
        }

        String targetQQ = getAt(args[0]);
        if (!targetQQ.isEmpty()) {
            if (targetQQ.equals(exec.user_id)) {
                system.send(exec, "你跟自己比划什么？");
                return;
            }

            NiuZiInfo targetInfo = NiuZiManager.INSTANCE.get(targetQQ);
            if (targetInfo == null) {
                system.send(exec, "真可惜！你选的比划对象人家没有牛子");
                return;
            }

            boolean noCooldown = userInfo != null && userInfo.rank_level >= Rank.Admin.level && FireQQ.INSTANCE.configs.dick_admin_no_cooldown;

            double delta = RandomUtils.nextDouble(0, 10);
            String source = exec.user_id;
            if (!noCooldown && info.hongzhong_data - System.currentTimeMillis() >= 0L) {
                system.send(exec, "你牛子红肿了，等 {}ms。", info.hongzhong_data - System.currentTimeMillis());
                return;
            }

            long pkCD = FireQQ.INSTANCE.configs.dick_pk_delay;
            info.hongzhong_data = System.currentTimeMillis() + pkCD;
            targetInfo.hongzhong_data = System.currentTimeMillis() + pkCD;
            int success = RandomUtils.nextInt(0, 101);
            if (success < 40) {
                info.niuZiCM -= delta;
                targetInfo.niuZiCM += delta;
                system.send(exec, "{} 和 {} 开始比划牛子，输了 {} 厘米。", new AtAction(source).toAction(), new AtAction(targetQQ).get(), delta);
            } else if (success > 60) {
                info.niuZiCM += delta;
                targetInfo.niuZiCM -= delta;
                system.send(exec, "{} 和 {} 开始比划牛子，赢到了 {} 厘米。", new AtAction(source).toAction(), new AtAction(targetQQ).get(), delta);
            } else {
                info.niuZiCM -= delta;
                targetInfo.niuZiCM -= delta;
            }

            NiuZiManager.INSTANCE.saveDicks();
        }
    }
}
