package org.mineacademy.minebridge.actions;

import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.minebridge.core.annotation.WebSocketAction;
import org.mineacademy.minebridge.core.internal.WebSocketAware;
import org.mineacademy.minebridge.core.schema.TestSchema;
import org.mineacademy.minebridge.core.websocket.Client;

public class TestActionHandler implements WebSocketAware {

    private Client client;

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @WebSocketAction(value = "test", schema = TestSchema.class)
    public void handleMessage(TestSchema schema) {
        Debugger.debug("websocket", "Received message: " + schema.getText());

        // Now you can use the client to send responses
        if (client != null) {
            // Example of sending a response
            TestSchema response = new TestSchema("Response to: " + schema.getText());
            client.send(response.toJson());
        }
    }
}