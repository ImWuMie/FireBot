package dev.wumie.handlers.luckperms;

import dev.wumie.FireQQ;
import dev.wumie.handlers.Handler;
import dev.wumie.messages.Message;
import dev.wumie.messages.PrivateQMessage;
import dev.wumie.messages.QMessage;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.modules.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class LuckPermsHandler extends Handler {
    public PermList permsMap = new PermList();
    public static LuckPermsHandler INSTANCE;
    public final File PERMS_JSON = new File(FOLDER, "perms.json");

    public LuckPermsHandler() {
        super("luck_perms");
        INSTANCE = this;
    }

    public boolean hasPerms(String qq, Module m) {
        return hasPerms(qq, m.permName);
    }

    public boolean hasPerms(String qq, String perm) {
        PermsInfo info = permsMap.get(qq);
        if (info == null) {
            info = permsMap.getByGroup("default");
            info.addUser(qq);
            savePerms();
        }

        if (info.perms.contains(perm)) {
            return true;
        }

        if (info.perms.contains("*")) {
            return true;
        }
        for (String p : info.perms) {
            if (p.endsWith("*")) {
                String a = p.substring(0, p.length() - ".*".length());
                if (perm.startsWith(a)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void init() {
        loadPerms();
    }

    @Override
    public void reload() {
        loadPerms();
    }

    @Override
    public void stop() {
        savePerms();
    }

    @Override
    public void message(Message message) {
        String msg = "";
        String qq = "null";
        if (message instanceof QMessage m) {
            msg = m.message;
            qq = m.user_id;
        } else if (message instanceof PrivateQMessage m) {
            msg = m.message;
            qq = m.user_id;
        }
        if (msg.startsWith("lp")) {
            cancel();
            if (!qq.equalsIgnoreCase("null") && hasPerms(qq,"fire.lp")) {
                String cmd = msg.substring("lp".length());
                String[] args = cmd.split(" ");
                if (args.length == 0) return;

                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                onMsg(message, newArgs);
            } else {
                log(true,message,"[LP] 你没有权限执行 {}","fire.lp");
            }
        }
    }

    private void onMsg(Message message, String[] args) {
        if (args.length == 0) {
            showUsage(message);
            return;
        }
        switch (args[0]) {
            case "move" -> {
                if (args.length == 3) {
                    String qq = args[1];
                    String group = args[2];
                    moveUser(message, qq, group, true);
                } else {
                    showUsage(message);
                }
            }
            case "set" -> {
                if (args.length == 4) {
                    String group = args[1];
                    String perm = args[2];
                    String action = args[3];
                    setPerm(message, group, perm, action.equalsIgnoreCase("add"), true);
                } else {
                    showUsage(message);
                }
            }
            case "create" -> {
                if (args.length == 2) {
                    String name = args[1];
                    createGroup(message, name, true);
                } else {
                    showUsage(message);
                }
            }
            case "remove" -> {
                if (args.length == 2) {
                    String name = args[1];
                    removeGroup(message, name);
                } else {
                    showUsage(message);
                }
            }
            case "rename" -> {
                if (args.length == 3) {
                    String name = args[1];
                    String next = args[2];
                    renameGroup(message, name, next);
                } else {
                    showUsage(message);
                }
            }
        }

        savePerms();
    }

    private void setPerm(Message m, String groupName, String perms, boolean addPerms, boolean log) {
        if (permsMap.getByGroup(groupName) == null) {
            log(log, m, "[LP] 权限组'{}'不存在", groupName);
            return;
        }

        PermsInfo info = permsMap.getByGroup(groupName);
        if (addPerms) {
            if (info.perms.contains(perms)) {
                log(log, m, "[LP] 权限组'{}'已存在权限'{}'", groupName, perms);
            } else {
                info.addPerm(perms);
                log(log, m, "[LP] 已添加权限'{}'至'{}'中", perms, groupName);
            }
        } else {
            if (info.perms.contains(perms)) {
                info.perms.remove(perms);
                log(log, m, "[LP] 已删除'{}'中的权限'{}'", groupName, perms);
            } else {
                log(log, m, "[LP] 权限组'{}'中不存在权限'{}'", groupName, perms);
            }
        }
    }

    private void moveUser(Message m, String qq, String groupName, boolean log) {
        if (permsMap.getByGroup(groupName) == null) {
            log(log, m, "[LP] 权限组'{}'不存在", groupName);
            return;
        }

        PermsInfo info = permsMap.get(qq);
        if (info != null) {
            info.member.remove(qq);
        }

        PermsInfo next = permsMap.getByGroup(groupName);
        if (next != null) {
            next.addUser(qq);
        }
        log(log, m, "[LP] 已移动'{}'到权限组'{}'中", qq, groupName);
    }

    private void createGroup(Message m, String groupName, boolean log) {
        if (permsMap.getByGroup(groupName) != null) {
            log(log, m, "[LP] 权限组'{}'已存在", groupName);
            return;
        }
        PermsInfo info = PermsInfo.newInfo();
        info.groupName = groupName;
        permsMap.add(info);
        log(log, m, "[LP] 权限组'{}'已创建", groupName);
    }

    private void removeGroup(Message m, String groupName) {
        if (permsMap.getByGroup(groupName) == null) {
            log(true, m, "[LP] 权限组'{}'不存在", groupName);
            return;
        }
        PermsInfo info = permsMap.getByGroup(groupName);
        info.member.forEach((qq) -> {
            moveUser(m, qq, "default", false);
        });
        permsMap.remove(info);
        log(true, m, "[LP] 权限组'{}'已移除", groupName);
    }

    private void renameGroup(Message m, String l, String n) {
        if (permsMap.getByGroup(l) == null) {
            log(true, m, "[LP] 权限组'{}'不存在", l);
            return;
        }
        PermsInfo info = permsMap.getByGroup(l);
        if (n.isEmpty()) {
            log(true, m, "[LP] 重命名名字'{}'不能为空", n);
        }
        info.rename(n);
        log(true, m, "[LP] 权限组'{}'已被重命名为'{}'", l, n);
    }

    private void showUsage(Message message) {
        MessageBuilder builder = new MessageBuilder("", true);
        builder.append("[LP] lp move [qq] [group]");
        builder.append("[LP] lp set [group] [perms] [add/del]");
        builder.append("[LP] lp create [name]");
        builder.append("[LP] lp remove [name]");
        builder.append("[LP] lp rename [last] [next]");
        log(true, message, builder.getString());
    }

    private void log(boolean log, Message message, String msg, Object... args) {
        if (!log) return;
        if (message instanceof QMessage m) {
            m.send(msg, args);
        } else if (message instanceof PrivateQMessage m) {

        }
    }

    public void loadPerms() {
        if (!PERMS_JSON.exists()) {
            savePerms();
        }

        String text = null;
        try {
            text = Files.readString(PERMS_JSON.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PermList l = FireQQ.INSTANCE.gson.fromJson(text, PermList.class);
        if (l == null) {
            savePerms();
        } else {
            this.permsMap.clear();
            this.permsMap.addAll(l);
        }
        LOG.info("Loaded {} perms.", permsMap.size());
    }

    public void savePerms() {
        try {
            if (!PERMS_JSON.exists()) {
                PERMS_JSON.createNewFile();
                permsMap.add(PermsInfo.newInfo());
            }
            String json = FireQQ.INSTANCE.gson.toJson(permsMap);
            Files.writeString(PERMS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
