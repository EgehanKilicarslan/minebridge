package org.mineacademy.minebridge.actions;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.schema.PlayerStatusCheckSchema;
import org.mineacademy.minebridge.core.websocket.Client;

public class PlayerActionHandler implements WebSocketAware {

    private Client client;

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @WebSocketAction(value = "player-status-check", schema = PlayerStatusCheckSchema.class)
    public void playerStatusCheck(PlayerStatusCheckSchema schema) {
        // Extract username and UUID from the schema
        final String username = schema.getUsername();
        final UUID uuid = schema.getUuid() != null ? UUID.fromString(schema.getUuid()) : null;

        // Find player by username or UUID (if available)

        final Player player = username != null ? PlayerUtil.getPlayerByNick(username, false)
                : uuid != null ? Remain.getPlayerByUUID(uuid) : null;

        // Create response schema
        String response = new PlayerStatusCheckSchema(
                player != null ? player.getName() : username,
                player != null ? player.getUniqueId().toString() : (uuid != null ? uuid.toString() : null),
                player != null && player.isConnected()).toJson();

        // Log the response for debugging
        Debugger.debug("websocket", "Sending player status response: " + response);

        // Send the response
        client.send(response);
    }
}