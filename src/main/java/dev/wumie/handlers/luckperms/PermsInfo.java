package dev.wumie.handlers.luckperms;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PermsInfo {
    @SerializedName("qq_id")
    public String qq;
    @SerializedName("perms")
    public List<String> perms;
}
