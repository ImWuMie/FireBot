package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;

public class ConnectedMessage extends Message {
    @SerializedName("meta_event_type") public String meta_event_type;
    @SerializedName("post_type") public String post_type;
    @SerializedName("self_id") public String myQid;
    @SerializedName("sub_type") public String sub_type;
    @SerializedName("time") public String time;
}
