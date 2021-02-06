package tech.dttp.serversimplified;

import tech.dttp.serversimplified.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;

public class ServerSimplified implements ModInitializer {

    public static Settings settings;
    private static Configuration configuration;
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerSimplified.configuration = Configuration.load();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            MuteCommand.register(dispatcher);
            PermissionCommand.register(dispatcher);
            PlayerActionCommand.register(dispatcher, VanishCommand.class);
            ServerMuteCommand.register(dispatcher);
            StaffChatCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            ServerSimplified.shutdown();
        });
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            ServerSimplified.server = server;
        });
    }

    public static void shutdown() {
        try {
            configuration.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server Simplified: couldn't save configuration!");
        }

        VanishCommand.getVanished().forEach(entity -> entity.removeStatusEffect(StatusEffects.INVISIBILITY));
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}