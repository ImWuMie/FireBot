package dev.wumie.system.event.dick;

import com.google.gson.annotations.SerializedName;
import dev.wumie.utils.RandomUtils;

public class NiuZiInfo {
    @SerializedName("qq_id")
    public String qq_id;
    @SerializedName("name")
    public String name;
    @SerializedName("cm")
    public double niuZiCM;
    @SerializedName("sex")
    public String sex;
    @SerializedName("yang_wei")
    public boolean yang_wei;

    @SerializedName("last_sign")
    public long last_sign;
    @SerializedName("last_jijian")
    public long last_jijian;

    @SerializedName("lover")
    public String lover;
    @SerializedName("love_request")
    public String love_request;
    @SerializedName("fenshou_data")
    public String fenshou_data;

    @SerializedName("hongzhong_data")
    public long hongzhong_data;
    @SerializedName("tietie_data")
    public long tietie_data;

    @SerializedName("sign_count")
    public int sign_count;
    @SerializedName("jijian_count")
    public int jijian_count;

    public void signup(double minPlus, double maxPlus) {
        double add = RandomUtils.nextDouble(minPlus, maxPlus);
        this.niuZiCM += add;
    }

    public void jijian(boolean winner,double min,double max) {
        double add = RandomUtils.nextDouble(min, max);
        if (!winner) {
            add = -add;
        }
        this.niuZiCM += add;
    }

    public void cgSex(SexType sexType) {
        this.sex = sexType.name;
    }

    public boolean hasLoveReq() {
        return !love_request.equals(qq_id);
    }

    public boolean hasLover() {
        return !lover.equals(qq_id);
    }

    public void love(String qq) {
        this.lover = qq;
        this.love_request = this.qq_id;
    }

    public String getLover() {
        return this.lover;
    }

    public void leave() {
        this.lover = qq_id;
        this.love_request = qq_id;
        this.fenshou_data = qq_id;
    }

    public enum SexType {
        MALE("男"),
        FEMALE("女");

        public final String name;

        SexType(String name) {
            this.name = name;
        }
    }
}
