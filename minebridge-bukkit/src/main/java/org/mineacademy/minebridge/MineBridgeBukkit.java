package org.mineacademy.minebridge;

import java.net.URI;
import java.net.URISyntaxException;

import org.mineacademy.fo.platform.BukkitPlugin;
import org.mineacademy.minebridge.actions.TestActionHandler;
import org.mineacademy.minebridge.websocket.Client;

public final class MineBridgeBukkit extends BukkitPlugin {

	private Client webSocketClient;

	@Override
	protected void onPluginLoad() {
		try {
			// Create WebSocket client
			webSocketClient = new Client(new URI("ws://localhost:8080"), "bukkit", "MineAcademy", null);

			// Register handler classes with WebSocketAction annotations
			webSocketClient.registerActionHandler(new TestActionHandler());

			// Connect to the WebSocket server
			webSocketClient.connect();

			getLogger().info("MineBridgeBukkit WebSocket client started and connected!");
		} catch (URISyntaxException e) {
			getLogger().severe("Failed to create WebSocket client: " + e.getMessage());
		}
	}

	@Override
	protected void onPluginStart() {
		System.out.println("Plugin started!");
	}

	@Override
	protected void onPluginStop() {
		// Close the WebSocket connection when the plugin is disabled
		if (webSocketClient != null && webSocketClient.isOpen()) {
			webSocketClient.close();
			getLogger().info("MineBridgeBukkit WebSocket client closed!");
		}
	}
}
