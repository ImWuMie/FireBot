package dev.wumie.websocket;

import dev.wumie.FireQQ;
import dev.wumie.system.MessageHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebServer extends WebSocketServer {
    public WebServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message != null && !message.isEmpty()) {
            MessageHandler handler = FireQQ.HANDLER;
            if (handler != null) {
                handler.onMessage(message);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {
        FireQQ.LOG.info("--FireBot Server--");
        FireQQ.LOG.info("Port: "+getPort());
    }
}
