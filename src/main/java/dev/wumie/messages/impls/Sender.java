package dev.wumie.messages.impls;

import com.google.gson.annotations.SerializedName;

public class Sender {
    @SerializedName("age") String age;
    @SerializedName("nickname") String nickname;
    @SerializedName("user_id") String user_id;
    @SerializedName("sex") String sex;

    @SerializedName("area") String area;
    @SerializedName("card") String card;
    @SerializedName("level") String level;
    @SerializedName("role") String role;
    @SerializedName("title") String title;

    @Override
    public String toString() {
        return "Sender{" +
                "age='" + age + '\'' +
                ", area='" + area + '\'' +
                ", card='" + card + '\'' +
                ", level='" + level + '\'' +
                ", nickname='" + nickname + '\'' +
                ", role='" + role + '\'' +
                ", sex='" + sex + '\'' +
                ", title='" + title + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
