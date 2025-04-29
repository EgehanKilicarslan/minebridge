package org.mineacademy.minebridge.bungee.actions;

import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.schema.PlayerStatusCheck;
import org.mineacademy.minebridge.core.websocket.Client;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Handles WebSocket actions related to player status on the BungeeCord server.
 * This class implements WebSocketAware to handle communication with external
 * services.
 */
public class PlayerActionHandler implements WebSocketAware {

    /**
     * The WebSocket client used for sending responses.
     */
    private Client client;

    /**
     * Sets the WebSocket client for this handler.
     * 
     * @param client The WebSocket client instance to use for communication
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Handles player status check requests from WebSocket.
     * This method checks if a player is online based on username or UUID,
     * then sends back the status information.
     * 
     * @param schema The schema containing request data (username and/or UUID)
     */
    @WebSocketAction(value = "player-status-check", schema = PlayerStatusCheck.class)
    public void playerStatusCheck(PlayerStatusCheck schema) {
        // Extract username and UUID from the schema
        final String username = schema.getUsername();
        final String uuid = schema.getUuid();

        // Find player by username or UUID (if available)
        final ProxiedPlayer player = username != null ? Remain.getPlayer(username, false)
                : uuid != null ? Remain.getPlayer(uuid, false) : null;

        // Create response schema
        String response = new PlayerStatusCheck(
                player != null ? player.getName() : username,
                player != null ? player.getUniqueId().toString() : uuid,
                player != null && player.isConnected()).toJson();

        // Log the response for debugging
        Debugger.debug("websocket", "Sending player status response: " + response);

        // Send the response
        client.send(response);
    }
}
