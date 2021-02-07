package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class MaintenanceModeCommand {
    private static boolean maintenance = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("maintenance").executes(context -> toggle(context.getSource())));
    }

    private static int toggle(ServerCommandSource scs) {
        maintenance = !maintenance;
        return 1;
    }

    public static boolean isMaintenance() {
        return maintenance;
    }

    public static String getReason() {
        return "Server is closed for maintenance";
    }
    public static void setMaintenanceMode(boolean value) {
        maintenance = value;
    }
}
