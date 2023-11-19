package dev.wumie.system;

import com.google.gson.annotations.SerializedName;
import dev.wumie.utils.Times;

import java.util.ArrayList;
import java.util.List;

public class Configs {
    @SerializedName("listen_groups")
    public List<String> groups;
    @SerializedName("admins_list")
    public List<String> admins_list;
    @SerializedName("jb_delay")
    public long jb_delay;
    @SerializedName("tts_delay")
    public long tts_delay;
    @SerializedName("check_delay")
    public long check_delay;
    @SerializedName("main_delay")
    public long main_delay;

    @SerializedName("dick_main_delay")
    public long dick_main_delay;
    @SerializedName("dick_pk_delay")
    public long dick_pk_delay;
    @SerializedName("dick_doi_delay")
    public long dick_doi_delay;
    @SerializedName("dick_sign_delay")
    public long dick_sign_delay;

    @SerializedName("dick_admin_no_cooldown")
    public boolean dick_admin_no_cooldown;

    public static Configs newConfig() {
        Configs configs = new Configs();
        configs.admins_list = new ArrayList<>();
        configs.jb_delay = 5000L;
        configs.tts_delay = 1000L;
        configs.check_delay = 2000L;
        configs.main_delay = 500L;

        configs.dick_main_delay = 100L;
        configs.dick_pk_delay = Times.HOUR;
        configs.dick_doi_delay = Times.HOUR;
        configs.dick_sign_delay = Times.DAY;
        configs.dick_admin_no_cooldown = false;
        return configs;
    }
}
