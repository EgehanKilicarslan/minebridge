package org.mineacademy.minebridge.core.schema;

import java.util.Map;

import org.mineacademy.minebridge.core.internal.BaseSchema;

import lombok.Getter;

public class CommandExecuted extends BaseSchema {

    @Getter
    private final String command_type;

    @Getter
    private final String executor;

    @Getter
    private final Map<String, Object> args;

    public CommandExecuted(String command_type, String executor, Map<String, Object> args, String server) {
        super("command-executed", server);
        this.command_type = command_type;
        this.executor = executor;
        this.args = args;
    }

    public CommandExecuted(String command_type, String executor, Map<String, Object> args) {
        super("command-executed");
        this.command_type = command_type;
        this.executor = executor;
        this.args = args;
    }
}
