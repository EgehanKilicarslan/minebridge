package org.mineacademy.minebridge.core.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mineacademy.minebridge.core.schema.CommandExecuted;
import org.mineacademy.minebridge.core.utils.CommandParser;
import org.mineacademy.minebridge.core.utils.CommandParser.ParsedCommand;
import org.mineacademy.minebridge.core.websocket.Client;

/**
 * Abstract command handler that provides platform-agnostic command processing
 */
public abstract class CommandHandler {

    // Command constants to avoid string duplication
    private static final String CMD_KICK = "kick";
    private static final String CMD_BAN = "ban";
    private static final String CMD_TEMPBAN = "tempban";
    private static final String CMD_UNBAN = "unban";
    private static final String CMD_MUTE = "mute";
    private static final String CMD_UNMUTE = "unmute";

    // Parameter constants
    private static final String PARAM_PLAYER = "player";
    private static final String PARAM_DURATION = "duration";
    private static final String PARAM_REASON = "reason";

    // Expected command count for initial HashMap capacity
    private static final int EXPECTED_COMMAND_COUNT = 6;

    // Command registry map for O(1) command lookup, initialized with capacity
    private final Map<String, CommandSpec> commandSpecs = new HashMap<>(EXPECTED_COMMAND_COUNT * 4 / 3 + 1);

    /**
     * Command specification that includes parameter requirements
     */
    private static class CommandSpec {
        final Set<String> requiredParams;

        CommandSpec(String... requiredParams) {
            // Initialize with expected size for better performance
            this.requiredParams = requiredParams.length > 0 ? new HashSet<>(Arrays.asList(requiredParams))
                    : Collections.emptySet();
        }
    }

    /**
     * Constructor initializes all command handlers
     */
    public CommandHandler() {
        registerCommands();
    }

    /**
     * Register all available commands
     */
    protected void registerCommands() {
        // Register commands with their required parameters
        registerCommand(CMD_KICK, PARAM_PLAYER);
        registerCommand(CMD_BAN, PARAM_PLAYER);
        registerCommand(CMD_TEMPBAN, PARAM_PLAYER, PARAM_DURATION);
        registerCommand(CMD_UNBAN, PARAM_PLAYER);
        registerCommand(CMD_MUTE, PARAM_PLAYER, PARAM_DURATION);
        registerCommand(CMD_UNMUTE, PARAM_PLAYER);
    }

    /**
     * Register a command with its required parameters
     * 
     * @param commandType    The command name
     * @param requiredParams Required parameter names
     */
    private void registerCommand(final String commandType, final String... requiredParams) {
        commandSpecs.put(commandType, new CommandSpec(requiredParams));
    }

    /**
     * Process incoming commands and route to appropriate handlers
     *
     * @param message         The raw command message
     * @param executor        The name of who executed the command
     * @param webSocketClient The WebSocket client
     * @return true if the command was handled, false otherwise
     */
    protected boolean processCommand(final String message, final String executor, final Client webSocketClient) {
        // Parse the command
        final ParsedCommand parsedCommand = CommandParser.parseCommand(message);
        if (parsedCommand == null)
            return false;

        final String commandType = parsedCommand.getCommandType().toLowerCase();

        // Get the command spec
        final CommandSpec spec = commandSpecs.get(commandType);
        if (spec == null)
            return false;

        // Validate required parameters
        for (final String param : spec.requiredParams) {
            if (parsedCommand.getParameter(param) == null || parsedCommand.getParameter(param).isEmpty()) {
                return false;
            }
        }

        // Build parameters map directly from parsed command - more efficient
        final Map<String, Object> params = new HashMap<>(5); // Initial capacity estimation

        // Get all needed parameters in one pass
        params.put(PARAM_PLAYER, parsedCommand.getParameter(PARAM_PLAYER));

        if (parsedCommand.hasParameter(PARAM_DURATION)) {
            params.put(PARAM_DURATION, parsedCommand.getParameter(PARAM_DURATION));
        }

        final String reason = parsedCommand.getParameter(PARAM_REASON);
        if (reason != null) {
            params.put(PARAM_REASON, reason);
        }

        // Create and send command
        final CommandExecuted commandExecuted = new CommandExecuted(commandType, executor, params);
        webSocketClient.send(commandExecuted.toJson());

        return true;
    }
}