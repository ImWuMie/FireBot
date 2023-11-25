package dev.wumie.handlers.luckperms;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PermList {
    @SerializedName("dicks")
    public final List<PermsInfo> perms = new ArrayList<>();

    public void add(PermsInfo info) {
        perms.add(info);
    }

    public void addAll(PermsInfo... info) {
        perms.addAll(List.of(info));
    }

    public void addAll(PermList i) {
        i.perms.forEach((info) -> this.perms.add(info.check()));
    }

    public PermsInfo get(String qq) {
        return perms.stream().filter(d -> d.member.contains(qq)).findFirst().orElse(null);
    }

    public PermsInfo getByGroup(String group) {
        return perms.stream().filter(d -> d.groupName.equals(group)).findFirst().orElse(null);
    }

    public void remove(PermsInfo info) {
        perms.remove(info);
    }

    public void remove(int id) {
        perms.remove(id);
    }

    public PermsInfo get(int i) {
        return perms.get(i);
    }

    public int size() {
        return perms.size();
    }

    public void clear() {
        this.perms.clear();
    }
}
