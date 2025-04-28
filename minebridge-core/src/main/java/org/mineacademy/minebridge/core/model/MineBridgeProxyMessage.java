package org.mineacademy.minebridge.core.model;

import org.mineacademy.fo.proxy.ProxyMessage;

import lombok.Getter;

public enum MineBridgeProxyMessage implements ProxyMessage {

    PLAYER_STATUS_CHECK(
            String.class, // Action name
            String.class, // Player name
            String.class, // Player UUID
            Boolean.class // Online status
    );

    @Getter
    private final Class<?>[] content;

    MineBridgeProxyMessage(Class<?>... validValues) {
        this.content = validValues;
    }

}
