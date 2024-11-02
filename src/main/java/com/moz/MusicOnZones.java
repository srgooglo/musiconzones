package com.moz;

import java.util.HashMap;
import java.util.Map;

// FABRIC IMPORTS
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

// MINECRAFT IMPORTS
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

// SLF4J IMPORTS
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// INTERNAL IMPORTS
import com.moz.commands.*;

public class MusicOnZones implements ModInitializer {
    public static final String MOD_ID = "musiconzones";
    public static final String cmd_prefix = "moz";

    public static MinecraftServer server_instance;

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Map<String, CommandFunction> commandsMap = new HashMap<>();

    @FunctionalInterface
    interface CommandFunction {
        int execute(MinecraftServer server, ServerCommandSource source, String[] args);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> server_instance = server);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MusicOnZones.registerBaseCommand(dispatcher));

        registerCommandsMap();
    }

    private static void registerCommandsMap() {
        commandsMap.put("spawnFox", Commands::spawnFox);
        commandsMap.put("create_zone", Commands::createZone);
        commandsMap.put("modify_zone", Commands::modifyZone);
        commandsMap.put("delete_zone", Commands::deleteZone);
    }

    public static void registerBaseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal(MusicOnZones.cmd_prefix)
                        .requires((s) -> s.hasPermissionLevel(1))
                        .then(argument("message", greedyString()).executes((ctx) -> MusicOnZones.handleCommand(getString(ctx, "message").split(" "), ctx.getSource()))));
    }

    public static int handleCommand(String[] args, ServerCommandSource source) {
        String cmd = args[0];

        CommandFunction command = commandsMap.get(cmd);

        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        if (command != null) {
            return command.execute(server_instance, source, commandArgs);
        } else {
            LOGGER.error("Command not found: {}", cmd);
            return 0;
        }
    }
}