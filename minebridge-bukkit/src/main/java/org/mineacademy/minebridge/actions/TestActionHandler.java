package org.mineacademy.minebridge.actions;

import org.mineacademy.minebridge.annotation.WebSocketAction;
import org.mineacademy.minebridge.implementations.WebSocketAware;
import org.mineacademy.minebridge.schemas.TestSchema;
import org.mineacademy.minebridge.websocket.Client;

public class TestActionHandler implements WebSocketAware {

    private Client client;

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @WebSocketAction(value = "test", schema = TestSchema.class)
    public void handleMessage(TestSchema schema) {
        System.out.println("Got: " + schema.getText());

        // Now you can use the client to send responses
        if (client != null) {
            // Example of sending a response
            TestSchema response = new TestSchema("Response to: " + schema.getText());
            client.send(response.toJson());
        }
    }
}