package org.mineacademy.minebridge.core.model;

import org.mineacademy.fo.proxy.ProxyMessage;

import lombok.Getter;

public enum MineBridgeProxyMessage implements ProxyMessage {

    TEST(
            String.class, // Action name
            String.class, // Server name
            String.class // Message content
    );

    @Getter
    private final Class<?>[] content;

    MineBridgeProxyMessage(Class<?>... validValues) {
        this.content = validValues;
    }

}
