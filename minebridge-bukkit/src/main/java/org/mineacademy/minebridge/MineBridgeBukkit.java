package org.mineacademy.minebridge;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Properties;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.platform.BukkitPlugin;
import org.mineacademy.minebridge.actions.TestActionHandler;
import org.mineacademy.minebridge.websocket.Client;

import lombok.Getter;

public final class MineBridgeBukkit extends BukkitPlugin {

	private Client webSocketClient;

	@Getter
	private static String serverName;

	@Override
	public String[] getStartupLogo() {
		return new String[] {
				"&c  __  __ _____ _   _ ______ ____  _____  _____ _____   _____ ______ ",
				"&4 |  \\/  |_   _| \\ | |  ____|  _ \\|  __ \\|_   _|  __ \\ / ____|  ____|",
				"&4 | \\  / | | | |  \\| | |__  | |_) | |__) | | | | |  | | |  __| |__   ",
				"&4 | |\\/| | | | | . ` |  __| |  _ <|  _  /  | | | |  | | | |_ |  __|  ",
				"&4 | |  | |_| |_| |\\  | |____| |_) | | \\ \\ _| |_| |__| | |__| | |____ ",
				"&4 |_|  |_|_____|_| \\_|______|____/|_|  \\_\\_____|_____/ \\_____|______|",
				"&0                                                                    ",
		};
	}

	@Override
	protected void onPluginPreStart() {
		parseServerName();
	}

	@Override
	protected void onPluginStart() {
		try {
			// Create WebSocket client
			webSocketClient = new Client(new URI("ws://localhost:8080"), "MineAcademy",
					new String[] { serverName });

			// Register handler classes with WebSocketAction annotations
			webSocketClient.registerActionHandler(new TestActionHandler());

			// Connect to the WebSocket server
			webSocketClient.connect();

			Common.log("Client started and connected successfully");
		} catch (URISyntaxException e) {
			Common.error(e, "Failed to create client: " + e.getMessage());
		}
	}

	@Override
	protected void onPluginStop() {
		// Close the WebSocket connection when the plugin is disabled
		if (webSocketClient != null && webSocketClient.isOpen()) {
			webSocketClient.close();
			Common.log("Client connection closed");
		}
	}

	private void parseServerName() {
		final File serverProperties = new File("server.properties");
		final Properties properties = new Properties();

		try {
			properties.load(Files.newInputStream(serverProperties.toPath()));
		} catch (final IOException ex) {
			Common.error(ex, "Failed to load server.properties file: " + ex.getMessage());
		}

		final String name = properties.getProperty("server-name");

		if (name == null || name.isEmpty()) {
			Common.throwError(new FoException("Server name not found in server.properties"));
		}

		serverName = name;
	}
}
