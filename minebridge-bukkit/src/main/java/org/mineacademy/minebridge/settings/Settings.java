package org.mineacademy.minebridge.settings;

import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.settings.SimpleSettings;

@SuppressWarnings("unused")
public class Settings extends SimpleSettings {

    public static String SERVER_TYPE;

    private static void init() {
        SERVER_TYPE = getString("Server_Type");

        // Validate that SERVER_TYPE is either "bukkit" or "proxy"
        if (!SERVER_TYPE.equalsIgnoreCase("bukkit") && !SERVER_TYPE.equalsIgnoreCase("proxy"))
            throw new FoException("Invalid Server_Type '" + SERVER_TYPE + "'. Must be either 'bukkit' or 'proxy'");
    }
}