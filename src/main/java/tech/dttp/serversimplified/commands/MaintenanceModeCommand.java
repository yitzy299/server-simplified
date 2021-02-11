package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import tech.dttp.serversimplified.ServerSimplified;

import static net.minecraft.server.command.CommandManager.literal;

public class MaintenanceModeCommand {
    private static boolean maintenance = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("maintenance").requires(scs -> scs.hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions().checkPermissions(scs, "maintenance")).executes(context -> toggle(context.getSource())));
    }

    private static int toggle(ServerCommandSource scs) {
        maintenance = !maintenance;
        scs.sendFeedback(new LiteralText("Maintenance mode ").formatted(Formatting.ITALIC).formatted(Formatting.GRAY).append(new LiteralText (maintenance ? "on" : "off").formatted(maintenance ? Formatting.RED : Formatting.GREEN)), true);
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
