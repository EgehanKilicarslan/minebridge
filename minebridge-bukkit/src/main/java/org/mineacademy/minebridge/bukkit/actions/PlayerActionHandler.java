package org.mineacademy.minebridge.bukkit.actions;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.schema.PlayerStatusCheck;
import org.mineacademy.minebridge.core.websocket.Client;

public class PlayerActionHandler implements WebSocketAware {

    private Client client;

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @WebSocketAction(value = "player-status-check", schema = PlayerStatusCheck.class)
    public void playerStatusCheck(PlayerStatusCheck schema) {
        // Extract username and UUID from the schema
        final String username = schema.getUsername();
        final String uuid = schema.getUuid();

        // Find player by username or UUID (if available)
        final Player player = username != null ? PlayerUtil.getPlayerByNick(username, false)
                : uuid != null ? Remain.getPlayerByUUID(UUID.fromString(uuid)) : null;

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