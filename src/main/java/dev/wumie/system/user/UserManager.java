package dev.wumie.system.user;

import com.google.gson.Gson;
import dev.wumie.FireQQ;
import dev.wumie.system.event.dick.NiuZiList;
import dev.wumie.utils.GsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UserManager {
    public final UserList list = new UserList();
    public static UserManager INSTANCE;

    public final File FOLDER = new File(FireQQ.INSTANCE.FOLDER, "user");
    public final File USERS_JSON = new File(FOLDER, "users.json");

    public final Gson gson = GsonUtils.newBuilder().create();
    public final Logger LOG = LogManager.getLogger("UserSystem");

    public UserManager() {
        INSTANCE = this;
    }

    public UserInfo get(String qq) {
        return list.get(qq);
    }

    public boolean has(String qq) {
        return list.get(qq) != null;
    }

    public UserInfo createUser(String qq) {
        return createUser(qq,Rank.User);
    }

    public UserInfo createUser(String qq,Rank rank) {
        UserInfo info = new UserInfo();
        info.qq_id = qq;
        info.rank_level = rank.level;
        info.rankName = rank.name;
        this.list.add(info);
        return info;
    }

    public void load() {
        if (!FOLDER.exists()) FOLDER.mkdirs();

        loadUsers();
    }

    public void loadUsers() {
        if (!USERS_JSON.exists()) {
            saveUsers();
        }

        String text = null;
        try {
            text = Files.readString(USERS_JSON.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserList l = gson.fromJson(text, UserList.class);
        if (l == null) {
            saveUsers();
        } else {
            this.list.clear();
            this.list.addAll(l);
        }
        LOG.info("Loaded {} users.", list.size());
    }

    public void saveUsers() {
        try {
            String json = gson.toJson(list);
            if (!USERS_JSON.exists()) USERS_JSON.createNewFile();

            Files.writeString(USERS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
