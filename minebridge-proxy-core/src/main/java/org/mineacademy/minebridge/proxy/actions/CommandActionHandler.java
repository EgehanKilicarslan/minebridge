package org.mineacademy.minebridge.proxy.actions;

import java.util.Arrays;
import java.util.stream.Stream;

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
        final String server = schema.getServer();
        final Stream<String> commands = Arrays.stream(schema.getCommands())
                .filter(cmd -> cmd != null && !cmd.isEmpty());

        if ("all".equals(server)) {
            Platform.getServers().forEach(srv -> dispatchCommandsToServer(commands, srv));
        } else {
            dispatchCommandsToServer(commands, Platform.getServer(server));
        }
    }

    /**
     * Sends multiple commands to a specific server
     * 
     * @param commands Array of commands to dispatch
     * @param server   The target server
     */
    private void dispatchCommandsToServer(final Stream<String> commands, final FoundationServer server) {
        commands.forEach(cmd -> {
            final OutgoingMessage msg = new OutgoingMessage(MineBridgeProxyMessage.DISPATCH_COMMAND);
            msg.writeString(cmd);
            msg.sendToServer("proxy", server);
        });
    }
}