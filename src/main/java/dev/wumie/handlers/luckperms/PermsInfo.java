package dev.wumie.handlers.luckperms;

import com.google.gson.annotations.SerializedName;
import dev.wumie.system.modules.Module;

import java.util.ArrayList;
import java.util.List;

public class PermsInfo {
    @SerializedName("name")
    public String groupName;
    @SerializedName("qq_ids")
    public List<String> member;
    @SerializedName("perms")
    public List<String> perms;

    public static PermsInfo newInfo() {
        PermsInfo info = new PermsInfo();
        info.groupName = "default";
        info.member = new ArrayList<>();
        info.perms = new ArrayList<>();
        info.perms.add("fire.*");
        return info;
    }

    public void rename(String n) {
        this.groupName = n;
    }

    public void addUser(String qq) {
        this.member.add(qq);
    }

    public void addPerm(String perms) {
        this.perms.add(perms);
    }

    public void addPerm(Module m) {
        this.perms.add(m.permName);
    }

    public PermsInfo check() {
        return this;
    }
}
