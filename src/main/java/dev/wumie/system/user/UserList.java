package dev.wumie.system.user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    @SerializedName("users")
    public final List<UserInfo> users = new ArrayList<>();

    public void add(UserInfo info) {
        users.add(info);
    }

    public void addAll(UserInfo... info) {
        users.addAll(List.of(info));
    }

    public void addAll(UserList list) {
        users.addAll(list.users);
    }

    public UserInfo get(String qq) {
        return users.stream().filter(d -> d.qq_id.equals(qq)).findFirst().orElse(null);
    }

    public void remove(UserInfo info) {
        users.remove(info);
    }

    public void remove(int id) {
        users.remove(id);
    }

    public UserInfo get(int i) {
        return users.get(i);
    }

    public int size() {
        return users.size();
    }

    public void clear() {
        this.users.clear();
    }
}
