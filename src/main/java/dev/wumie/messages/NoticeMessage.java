package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;

public class NoticeMessage extends Message {
    @SerializedName("post_type") public String post_type;
    @SerializedName("notice_type") public String notice_type;
    @SerializedName("time") public String time;
    @SerializedName("self_id") public String self_id;
    @SerializedName("group_id") public String group_id;
    @SerializedName("user_id") public String user_id;
    @SerializedName("operator_id") public String operator_id;
    @SerializedName("message_id") public String message_id;
}
