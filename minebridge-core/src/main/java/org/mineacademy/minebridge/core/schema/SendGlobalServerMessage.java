package org.mineacademy.minebridge.core.schema;

import org.mineacademy.minebridge.core.internal.BaseSchema;

import lombok.Getter;

public class SendGlobalServerMessage extends BaseSchema {

    /**
     * The message type to send to all players.
     */
    @Getter
    private final String type;

    /**
     * The message to send to all players.
     */
    @Getter
    private final String message;

    public SendGlobalServerMessage(String type, String message, String server) {
        super("send-global-server-message", server);
        this.type = type;
        this.message = message;
    }
}
