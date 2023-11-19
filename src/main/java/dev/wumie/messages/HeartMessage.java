package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;
import dev.wumie.messages.impls.Status;

public class HeartMessage extends Message {
    @SerializedName("post_type") public String post_type;
    @SerializedName("meta_event_type") public String meta_event_type;
    @SerializedName("time") public String time;
    @SerializedName("self_id") public String myQid;
    @SerializedName("status") public Status status;
    @SerializedName("interval") public String interval;
}
