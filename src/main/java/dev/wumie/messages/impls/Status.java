package dev.wumie.messages.impls;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("app_enabled") public boolean app_enabled;
    @SerializedName("app_good") public boolean app_good;
    @SerializedName("app_initialized") public boolean app_initialized;
    @SerializedName("good") public boolean good;
    @SerializedName("online") public boolean online;
    @SerializedName("plugins_good") public Object plugins_good;
    @SerializedName("stat") public Stat stat;
}
