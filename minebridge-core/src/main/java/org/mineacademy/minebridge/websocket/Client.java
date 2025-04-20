package org.mineacademy.minebridge.websocket;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.mineacademy.fo.CommonCore;
import org.mineacademy.fo.debug.Debugger;

import com.google.gson.JsonObject;

public class Client extends WebSocketClient {

    private final WebSocketActionHandler actionHandler;
    private final String password;
    private final String[] server_list;

    public Client(URI serverUri, String password, String[] server_list) {
        super(serverUri);
        this.actionHandler = new WebSocketActionHandler();
        this.actionHandler.setClient(this); // Set client in action handler
        this.password = password;
        this.server_list = server_list;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Debugger.debug("websocket", "Opened connection to server: " + getURI());
        this.authenticate(password, server_list);
    }

    @Override
    public void onMessage(String message) {
        Debugger.debug("websocket", "Received message: " + message);

        if (!actionHandler.handleMessage(message)) {
            Debugger.debug("websocket", "No handler found for message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Debugger.debug("websocket", "Closed connection: " + reason);

        // Create a new thread to handle reconnection attempts
        new Thread(() -> {
            // Attempt to reconnect indefinitely with 30 seconds between attempts
            int attemptCount = 0;
            boolean reconnected = false;

            while (!reconnected) {
                try {
                    attemptCount++;
                    Debugger.debug("websocket", "Attempting to reconnect... (Attempt " + attemptCount + ")");

                    // Wait 30 seconds before attempting reconnection
                    Thread.sleep(30000);

                    // Attempt to reconnect
                    reconnected = Client.this.reconnectBlocking();

                    if (reconnected) {
                        Debugger.debug("websocket", "Reconnected successfully after " + attemptCount + " attempts.");
                    } else {
                        Debugger.debug("websocket",
                                "Reconnection attempt " + attemptCount + " failed. Trying again in 30 seconds...");
                    }
                } catch (InterruptedException e) {
                    CommonCore.error(e, "Reconnection attempt interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    Debugger.debug("websocket",
                            "Error during reconnection attempt: " + e.getMessage() + ". Trying again in 30 seconds...");
                }
            }
        }, "WebSocket-Reconnect-Thread").start();
    }

    @Override
    public void onError(Exception ex) {
        CommonCore.error(ex, "WebSocket error: " + ex.getMessage());
    }

    public void authenticate(String password, String[] server_list) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "authenticate");
        jsonObject.addProperty("password", password);

        // Convert String array to JSON array
        com.google.gson.JsonArray serverArray = new com.google.gson.JsonArray();
        for (String server : server_list) {
            serverArray.add(new com.google.gson.JsonPrimitive(server));
        }
        jsonObject.add("server_list", serverArray);

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
