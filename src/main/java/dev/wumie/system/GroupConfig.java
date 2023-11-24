package dev.wumie.system;

import com.google.gson.annotations.SerializedName;

public class GroupConfig {
    @SerializedName("enable")
    public boolean enable;

    public static GroupConfig newConfig() {
        return null;
    }
}
