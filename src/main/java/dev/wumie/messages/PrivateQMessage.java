package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;
import dev.wumie.messages.impls.Sender;

public class PrivateQMessage extends Message {
    @SerializedName("post_type") public String post_type;
    @SerializedName("message_type") public String message_type;

    @SerializedName("time") public String time;

    @SerializedName("sub_type") public String sub_type;
    @SerializedName("message") public String message;
    @SerializedName("raw_message") public String fullMessage;
    @SerializedName("font") public String font;

    @SerializedName("self_id") public String myQid;
    @SerializedName("sender") public Sender sender;
    @SerializedName("user_id") public String user_id;

    @SerializedName("message_id") public String message_id;
    @SerializedName("message_seq") public String message_seq;
    @SerializedName("target_id") public String target_id;
}
