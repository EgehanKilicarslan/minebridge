package org.mineacademy.minebridge.bungee.listener;

import org.mineacademy.minebridge.core.internal.CommandHandler;
import org.mineacademy.minebridge.core.websocket.Client;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class CommandListenerBungeeCord extends CommandHandler implements Listener {

    private final Client webSocketClient;

    /**
     * Initialize the command listener with the given WebSocket client
     * 
     * @param webSocketClient The WebSocket client to use for communication
     * @throws IllegalArgumentException if webSocketClient is null
     */
    public CommandListenerBungeeCord(Client webSocketClient) {
        if (webSocketClient == null) {
            throw new IllegalArgumentException("WebSocket client cannot be null");
        }
        this.webSocketClient = webSocketClient;
    }

    @EventHandler
    public void onChatCommand(final ChatEvent event) {
        if (!event.isCommand())
            return;

        final Connection sender = event.getSender();
        final String executor = sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getName() : "CONSOLE";

        processCommand(event.getMessage(), executor, webSocketClient);
    }

}
