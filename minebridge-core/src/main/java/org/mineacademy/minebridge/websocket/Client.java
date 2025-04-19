package org.mineacademy.minebridge.websocket;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.JsonObject;

public class Client extends WebSocketClient {

    private final WebSocketActionHandler actionHandler;
    private final String serverType;
    private final String password;
    private final String server_list;

    public Client(URI serverUri, String serverType, String password, String server_list) {
        super(serverUri);
        this.actionHandler = new WebSocketActionHandler();
        this.actionHandler.setClient(this); // Set client in action handler
        this.serverType = serverType;
        this.password = password;
        this.server_list = server_list;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection");
        this.authenticate(serverType, password, server_list);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        if (!actionHandler.handleMessage(message)) {
            System.out.println("No handler found for message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + reason);

        // Create a new thread to handle reconnection attempts
        new Thread(() -> {
            // Attempt to reconnect indefinitely with 30 seconds between attempts
            int attemptCount = 0;
            boolean reconnected = false;

            while (!reconnected) {
                try {
                    attemptCount++;
                    System.out.println("Attempting to reconnect... (Attempt " + attemptCount + ")");

                    // Wait 30 seconds before attempting reconnection
                    Thread.sleep(30000);

                    // Attempt to reconnect
                    reconnected = Client.this.reconnectBlocking();

                    if (reconnected) {
                        System.out.println("Successfully reconnected after " + attemptCount + " attempts.");
                    } else {
                        System.out.println(
                                "Reconnection attempt " + attemptCount + " failed. Trying again in 30 seconds...");
                    }
                } catch (InterruptedException e) {
                    System.err.println("Reconnection attempt interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println(
                            "Error during reconnection attempt: " + e.getMessage() + ". Trying again in 30 seconds...");
                }
            }
        }, "WebSocket-Reconnect-Thread").start();
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    public void authenticate(String serverType, String password, String server_list) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "authenticate");
        jsonObject.addProperty("server_type", "bukkit");
        jsonObject.addProperty("password", "MineAcademy");
        jsonObject.addProperty("server_list", server_list);

        this.send(jsonObject.toString());
    }

    /**
     * Register a class containing methods annotated with
     * {@link org.mineacademy.minebridge.annotation.WebSocketAction}
     * 
     * @param instance The instance of the class containing annotated methods
     */
    public void registerActionHandler(Object instance) {
        actionHandler.registerClass(instance);
    }
}
