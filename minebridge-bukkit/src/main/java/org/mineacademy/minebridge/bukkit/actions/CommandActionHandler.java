package org.mineacademy.minebridge.bukkit.actions;

import org.mineacademy.fo.platform.Platform;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.schema.DispatchCommand;
import org.mineacademy.minebridge.core.websocket.Client;

@SuppressWarnings("unused")
public class CommandActionHandler implements WebSocketAware {

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

    @WebSocketAction(value = "dispatch-command", schema = DispatchCommand.class)
    public void dispatchCommand(DispatchCommand schema) {
        for (String command : schema.getCommands())
            Platform.dispatchConsoleCommand(null, command);
    }
}
