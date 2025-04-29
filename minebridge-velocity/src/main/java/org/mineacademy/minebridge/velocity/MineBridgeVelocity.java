package org.mineacademy.minebridge.velocity;

import java.nio.file.Path;

import org.mineacademy.fo.platform.VelocityPlugin;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

public class MineBridgeVelocity extends VelocityPlugin {

    @Inject
    public MineBridgeVelocity(final ProxyServer proxyServer, final Logger logger, @DataDirectory Path dataDirectory) {
        super(proxyServer, logger, dataDirectory);
    }

    @Override
    protected void onPluginStart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPluginStart'");
    }

}
