package dev.wumie.messages.impls;

import com.google.gson.annotations.SerializedName;

public class Stat {
    @SerializedName("packet_received") public int packet_received;
    @SerializedName("packet_sent") public int packet_sent;
    @SerializedName("packet_lost") public int packet_lost;
    @SerializedName("message_received") public int message_received;
    @SerializedName("message_sent") public int message_sent;
    @SerializedName("disconnect_times") public int disconnect_times;
    @SerializedName("lost_times") public int lost_times;
    @SerializedName("last_message_time") public long last_message_time;
}
