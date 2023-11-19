package dev.wumie.system.event.impl;

import dev.wumie.messages.QMessage;
import dev.wumie.system.event.MsgEvent;

public class GPTEvent extends MsgEvent {
    public GPTEvent() {
        super("chat_gpt");
    }

    private static final String apiKey = "";
    private static String chatEndpoint = "https://api.openai.com/v1/chat/completions";

    @Override
    public void run(String message, QMessage exec) {

    }
}
