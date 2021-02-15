package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import tech.dttp.serversimplified.ServerSimplified;

import static net.minecraft.server.command.CommandManager.literal;

public class MaintenanceModeCommand {
    private static boolean maintenance = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("maintenance").requires(scs -> {
            try {
                return scs.hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions().hasPermission(scs.getPlayer().getUuidAsString(), "maintenance");
            } catch (CommandSyntaxException e) {
                return false;
            }
        }).then(literal("on").executes(ctx -> setMaintenanceMode(ctx.getSource(), true))).then(literal("off").executes(ctx -> setMaintenanceMode(ctx.getSource(), false))));
        //dispatcher.register(literal("maintenance").requires(scs -> scs.hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions().checkPermissions(scs, "maintenance")).executes(context -> toggle(context.getSource())));
    }

    public static boolean isMaintenance() {
        return maintenance;
    }

    public static String getReason() {
        return "Server is closed for maintenance";
    }

    private static int setMaintenanceMode(ServerCommandSource scs, boolean value) {
        maintenance = value;
        scs.sendFeedback(new LiteralText("Maintenance mode ").formatted(Formatting.ITALIC).formatted(Formatting.GRAY).append(new LiteralText (maintenance ? "on" : "off").formatted(maintenance ? Formatting.RED : Formatting.GREEN)), true);
        if(maintenance) {
            scs.getMinecraftServer().getPlayerManager().getPlayerList().forEach(player -> {
                if(!player.hasPermissionLevel(3) && !ServerSimplified.getConfiguration().getPermissions().hasPermission(player.getUuidAsString(), "maintenance")) {
                    player.networkHandler.disconnect(new LiteralText("Server has closed for maintenance"));
                }
            });
        }
        return 1;
    }
    public static void setMaintenanceMode(boolean value) {
        maintenance = value;
    }
}
