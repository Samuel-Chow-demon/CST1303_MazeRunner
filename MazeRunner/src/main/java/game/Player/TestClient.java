package game.Player;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.net.URI;

public class TestClient {

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8080/websockets/mazegame";
        System.out.println("Connecting to " + uri);
        try {
            container.connectToServer(GameClient.class, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
