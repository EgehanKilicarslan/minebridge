package org.mineacademy.minebridge.core.schema;

import org.mineacademy.minebridge.core.internal.BaseSchema;

import lombok.Getter;

public class SendGlobalMessage extends BaseSchema {

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

    public SendGlobalMessage(String type, String message) {
        super("send-global-message", false);
        this.type = type;
        this.message = message;
    }

}
