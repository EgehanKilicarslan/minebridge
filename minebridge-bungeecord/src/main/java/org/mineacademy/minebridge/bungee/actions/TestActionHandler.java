package org.mineacademy.minebridge.bungee.actions;

import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.platform.Platform;
import org.mineacademy.fo.proxy.message.OutgoingMessage;
import org.mineacademy.minebridge.annotation.WebSocketAction;
import org.mineacademy.minebridge.bungee.schemas.TestSchema;
import org.mineacademy.minebridge.implementations.WebSocketAware;
import org.mineacademy.minebridge.model.MineBridgeProxyMessage;
import org.mineacademy.minebridge.websocket.Client;

@SuppressWarnings("unused")
public class TestActionHandler implements WebSocketAware {

    private Client client;

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @WebSocketAction(value = "test", schema = TestSchema.class)
    public void handleMessage(TestSchema schema) {
        Debugger.debug("websocket", "Received message: " + schema.getText());

        OutgoingMessage message = new OutgoingMessage(MineBridgeProxyMessage.TEST);
        final String serverName = schema.getServer();

        message.writeString(schema.getAction());
        message.writeString(serverName);
        message.writeString(schema.getText());

        message.sendToServer("bungeecord", Platform.getServer(serverName));
    }
}