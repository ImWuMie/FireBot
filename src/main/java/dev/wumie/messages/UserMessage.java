package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;

public class UserMessage {
    @SerializedName("post_type") public String post_type;
    @SerializedName("message_type") public String message_type;
}
