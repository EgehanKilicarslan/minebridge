package org.mineacademy.minebridge.proxy.settings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.settings.SimpleSettings;

@SuppressWarnings("unused")
public class ProxySettings extends SimpleSettings {

    @Override
    protected List<String> getUncommentedSections() {
        return Collections.unmodifiableList(Arrays.asList("WebSocket"));
    }

    /*
     * Settings for the WebSocket connection
     */
    public static class WebSocket {
        public static String HOST;
        public static Integer PORT;
        public static String PASSWORD;

        private static void init() {
            setPathPrefix("WebSocket");
            HOST = getString("Host");
            PORT = getInteger("Port");
            PASSWORD = getString("Password");

            // Validate that PORT is a valid port number
            if (PORT < 1 || PORT > 65535)
                throw new FoException("Invalid WebSocket port: " + PORT);
        }
    }

}
