package org.mineacademy.minebridge.implementations;

import org.mineacademy.minebridge.websocket.Client;

/**
 * Interface for classes that need access to the WebSocket client.
 */
public interface WebSocketAware {
    /**
     * Sets the WebSocket client for this class to use.
     * 
     * @param client The WebSocket client
     */
    void setClient(Client client);
}