package tech.dttp.serversimplified;

import tech.dttp.serversimplified.commands.MuteCommand;
import tech.dttp.serversimplified.commands.PermissionCommand;
import tech.dttp.serversimplified.commands.PlayerActionCommand;
import tech.dttp.serversimplified.commands.StaffChatCommand;
import tech.dttp.serversimplified.commands.VanishCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.minecraft.entity.effect.StatusEffects;

import java.io.IOException;

public class ServerSimplified implements ModInitializer {

    private static Configuration configuration;

    @Override
    public void onInitialize() {
        ServerSimplified.configuration = Configuration.load();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            MuteCommand.register(dispatcher);
            PermissionCommand.register(dispatcher);
            PlayerActionCommand.register(dispatcher, VanishCommand.class);
            StaffChatCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            ServerSimplified.shutdown();
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