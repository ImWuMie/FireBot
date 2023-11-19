package dev.wumie.system.user;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class UserInfo {
    @SerializedName("qq_id")
    public String qq_id;
    @SerializedName("rank")
    public String rankName;
    @SerializedName("rank_level")
    public int rank_level;

    public Rank getRank() {
        return Arrays.stream(Rank.values()).filter((r) -> r.name.equals(this.rankName)).findFirst().orElse(Rank.User);
    }
}
