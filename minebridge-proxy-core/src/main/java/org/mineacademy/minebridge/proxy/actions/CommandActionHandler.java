package org.mineacademy.minebridge.proxy.actions;

import org.mineacademy.fo.platform.FoundationServer;
import org.mineacademy.fo.platform.Platform;
import org.mineacademy.fo.proxy.message.OutgoingMessage;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.model.MineBridgeProxyMessage;
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
        final FoundationServer server = Platform.getServer(schema.getServer());
        final String[] commands = schema.getCommands();

        for (String command : commands) {
            OutgoingMessage message = new OutgoingMessage(MineBridgeProxyMessage.DISPATCH_COMMAND);
            message.writeString(command);
            message.sendToServer("bungeecord", server);
        }
    }
}
