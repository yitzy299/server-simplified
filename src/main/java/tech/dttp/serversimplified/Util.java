package tech.dttp.serversimplified;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class Util {

    public static boolean isHuman(ServerCommandSource source) {
        return source.getEntity() instanceof ServerPlayerEntity;
    }

}
