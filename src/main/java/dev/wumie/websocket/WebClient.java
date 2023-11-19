package dev.wumie.websocket;

import dev.wumie.FireQQ;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebClient extends WebSocketClient {
    private final String url;

    public WebClient(String url) throws URISyntaxException {
        super(new URI(url));
        this.url = url;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        FireQQ.LOG.info("[MCBB] Connect!. "+serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String s) {
        FireQQ.LOG.info("[MCBB] Receive: "+s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        FireQQ.LOG.info("[MCBB] Disconnect.");
    }

    @Override
    public void onError(Exception e) {

    }
}
