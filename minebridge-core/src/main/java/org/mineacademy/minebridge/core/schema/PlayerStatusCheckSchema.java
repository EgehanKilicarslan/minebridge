package org.mineacademy.minebridge.core.schema;

import org.mineacademy.minebridge.core.internal.BaseSchema;

import lombok.Getter;

public class PlayerStatusCheckSchema extends BaseSchema {

    @Getter
    private final String username;

    @Getter
    private final String uuid;

    @Getter
    private final Boolean online;

    public PlayerStatusCheckSchema(String username, String uuid, Boolean online) {
        super("player-status-check");
        this.username = username;
        this.uuid = uuid;
        this.online = online;
    }

}
