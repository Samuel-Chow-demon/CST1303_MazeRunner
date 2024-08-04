package game.Server;

import org.glassfish.tyrus.server.Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestServer {
    public static void main(String[] args) {
        String HOST_NAME = "localhost";
        int SERVER_PORT = 8080;
        String ROOT_URL = "/websockets";

        Server server = new Server(HOST_NAME, SERVER_PORT, ROOT_URL, null, GameServer.class);

        try {
            server.start();
            System.out.println("Press any key to stop the server...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}