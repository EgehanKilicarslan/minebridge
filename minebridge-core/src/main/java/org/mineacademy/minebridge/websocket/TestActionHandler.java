package org.mineacademy.minebridge.websocket;

import org.mineacademy.minebridge.annotation.WebSocketAction;
import org.mineacademy.minebridge.schema.TestSchema;

public class TestActionHandler {

    @WebSocketAction(value = "test", schema = TestSchema.class)
    public void handleMessage(TestSchema schema) {
        System.out.println("Got: " + schema.getText());
    }

}