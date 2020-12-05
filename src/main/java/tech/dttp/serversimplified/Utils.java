package tech.dttp.serversimplified;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class Utils {
    public static final Logger LOG = LogManager.getLogger();
    public static boolean isHuman(ServerCommandSource source) {
        return source.getEntity() instanceof ServerPlayerEntity;
    }

    public static boolean hasPermission(ServerPlayerEntity player, String perm) {
        if (player.hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions()
                .hasPermission(player.getUuidAsString(), "mute")) {
            return true;
        }
        return false;
    }

    public static boolean hasPermissionFromSource(ServerCommandSource scs, String string) {
        try {
            if (scs.getPlayer().hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions()
                    .hasPermission(scs.getPlayer().getUuidAsString(), "mute")) {
                return true;
            }
            return false;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
		return false;
    }
    public static void log(String content){
        LOG.info(content);
    }
}
