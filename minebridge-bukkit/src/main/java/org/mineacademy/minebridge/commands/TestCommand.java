package org.mineacademy.minebridge.commands;

import org.mineacademy.fo.ProxyUtil;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.platform.Platform;
import org.mineacademy.minebridge.model.MineBridgeProxyMessage;

import lombok.Getter;

@AutoRegister
public final class TestCommand extends SimpleCommand {

    @Getter
    private final static TestCommand instance = new TestCommand();

    public TestCommand() {
        super("test");
    }

    @Override
    protected void onCommand() {
        ProxyUtil.sendPluginMessage(MineBridgeProxyMessage.TEST, "test", Platform.getCustomServerName(),
                "Hello from Bukkit!");
    }

}
