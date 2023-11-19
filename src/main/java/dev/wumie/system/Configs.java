package dev.wumie.system;

import com.google.gson.annotations.SerializedName;

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
}
