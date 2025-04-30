package org.mineacademy.minebridge.core.model;

import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.proxy.ProxyMessage;

import lombok.Getter;

public enum MineBridgeProxyMessage implements ProxyMessage {

    SEND_PLAYER_MESSAGE(
            String.class, // Player name
            MessageType.class, // Message type
            SimpleComponent.class // Message
    ),

    SEND_GLOBAL_MESSAGE(
            MessageType.class, // Message type
            SimpleComponent.class // Message
    );

    @Getter
    private final Class<?>[] content;

    MineBridgeProxyMessage(Class<?>... validValues) {
        this.content = validValues;
    }

}
