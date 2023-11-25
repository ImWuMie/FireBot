package dev.wumie.system.event.dick;

import com.google.gson.Gson;
import dev.wumie.FireQQ;
import dev.wumie.utils.GsonUtils;
import dev.wumie.utils.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NiuZiManager {
    public final NiuZiList list = new NiuZiList();
    public static NiuZiManager INSTANCE;

    public final File FOLDER = new File(FireQQ.INSTANCE.FOLDER, "dicks");
    public final File DICKS_JSON = new File(FOLDER, "dicks.json");

    public final Gson gson = GsonUtils.newBuilder().create();
    public final Logger LOG = LogManager.getLogger("DickSystem");

    public NiuZiManager() {
        INSTANCE = this;
    }

    public NiuZiInfo get(String group,String qq) {
        return list.get(group,qq);
    }

    public boolean hasNiuZi(String group,String qq) {
        return get(group,qq) != null;
    }

    public void createNiuZi(String group,String qq) {
        if (hasNiuZi(group,qq)) return;

        NiuZiInfo info = new NiuZiInfo();
        info.name = "牛子";
        info.group_id = group;
        info.niuZiCM = RandomUtils.nextDouble(15, 30);
        info.qq_id = qq;
        info.sex = NiuZiInfo.SexType.MALE.name;
        info.fenshou_data = info.qq_id;
        info.love_request = info.qq_id;
        info.lover = info.qq_id;
        this.list.add(info);
        saveDicks();
    }

    public void load() {
        if (!FOLDER.exists()) FOLDER.mkdirs();

        loadDicks();
    }

    public void loadDicks() {
        if (!DICKS_JSON.exists()) {
            saveDicks();
        }

        String text = null;
        try {
            text = Files.readString(DICKS_JSON.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NiuZiList l = gson.fromJson(text, NiuZiList.class);
        if (l == null) {
            saveDicks();
        } else {
            this.list.clear();
            this.list.addAll(l);
        }
        LOG.info("Loaded {} dicks.", list.size());
    }

    public void saveDicks() {
        try {
            String json = gson.toJson(list);
            if (!DICKS_JSON.exists()) DICKS_JSON.createNewFile();

            Files.writeString(DICKS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
