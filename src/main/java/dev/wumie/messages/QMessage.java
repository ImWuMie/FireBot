package dev.wumie.messages;

import com.google.gson.annotations.SerializedName;
import dev.wumie.messages.impls.Sender;
import dev.wumie.system.MessageBuilder;
import dev.wumie.system.MessageHandler;
import dev.wumie.utils.StringUtils;
import dev.wumie.websocket.BotMain;

public class QMessage extends Message {
    @SerializedName("post_type")
    public String post_type;
    @SerializedName("message_type")
    public String message_type;

    @SerializedName("time")
    public String time;

    @SerializedName("sub_type")
    public String sub_type;
    @SerializedName("message")
    public String message;
    @SerializedName("raw_message")
    public String fullMessage;
    @SerializedName("font")
    public String font;
    @SerializedName("group_id")
    public String group_id;

    @SerializedName("anonymous")
    public Object anonymous;

    @SerializedName("self_id")
    public String myQid;
    @SerializedName("sender")
    public Sender sender;
    @SerializedName("user_id")
    public String user_id;

    @SerializedName("message_id")
    public String message_id;
    @SerializedName("message_seq")
    public String message_seq;

    public MessageHandler getHandler() {
        return BotMain.INSTANCE.handlers.getOrDefault(this.group_id,null);
    }

    @Override
    public String toString() {
        return "QMessage{" +
                "post_type='" + this.post_type + '\'' +
                ", message_type='" + message_type + '\'' +
                ", time='" + time + '\'' +
                ", self_id='" + myQid + '\'' +
                ", sub_type='" + sub_type + '\'' +
                ", message='" + message + '\'' +
                ", raw_message='" + fullMessage + '\'' +
                ", font='" + font + '\'' +
                ", group_id='" + group_id + '\'' +
                ", sender=" + sender +
                ", user_id='" + user_id + '\'' +
                ", message_id='" + message_id + '\'' +
                ", anonymous='" + anonymous + '\'' +
                ", message_seq='" + message_seq + '\'' +
                '}';
    }

    public void send(String message, Object... args) {
        message = StringUtils.getReplaced(message, args);
        MessageBuilder builder = new MessageBuilder();
        builder.append(message);
        getHandler().send(builder.get(this.group_id));
    }

    public void face(int faceId) {
        this.send("[CQ:face,id={}]", faceId);
    }

    public void record(String url) {
        this.send("[CQ:record,file={}]", url);
    }

    public void video(String url) {
        this.send("[CQ:video,file={}]", url);
    }

    public void tts(String msg, Object... args) {
        msg = StringUtils.getReplaced(msg, args);
        this.send("[CQ:tts,text={}]", msg);
    }

    public void reply(String message, Object... args) {
        message = StringUtils.getReplaced(message, args);
        this.send("[CQ:reply,id={}][CQ:at,qq={}] {}", this.message_id, this.user_id, message);
    }

    public void at(String qq) {
        this.send("[CQ:at,qq={}]", qq);
    }

    public void urlShare(String title, String url, Object... titleArgs) {
        title = StringUtils.getReplaced(title, titleArgs);
        this.send("[CQ:share,url={},title={}]", url, title);
    }

    public void customMusic(String title, String url, String audioUrl, Object... titleArgs) {
        title = StringUtils.getReplaced(title, titleArgs);
        this.send("[CQ:music,type=custom,url={},audio={},title{}]", url, audioUrl, title);
    }

    public void picture(String url, int id, boolean isFlash) {
        this.send("[CQ:image,file={},type={},id={}]", url, isFlash ? "flash" : "show", id);
    }

    public void picture(String url) {
        this.send("[CQ:image,file={}]",  url);
    }

    public void gift(int giftId) {
        //this.send("[CQ:gift,qq={},id={}]", user_id, giftId);
    }

    public void json(String json, Object... args) {
        json = StringUtils.getReplaced(json, args);
        json = StringUtils.replaceAll(json, ",", "&#44;");
        json = StringUtils.replaceAll(json, "&", "&amp;");
        json = StringUtils.replaceAll(json, "[", "&#91;");
        json = StringUtils.replaceAll(json, "]", "&#93;");
        this.send("[CQ:json,data={}]", json);
    }

    public void retell() {
        this.send(this.fullMessage);
    }
}
