package tech.dttp.serversimplified.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import tech.dttp.serversimplified.Permissions;
import tech.dttp.serversimplified.ServerSimplified;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.BaseText;

public class FreezeCommand {
    private static String frozen = "";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> muteBuilder = CommandManager.literal("freeze")
        .requires(scs -> scs.hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions().checkPermissions(scs, "mute"))
        .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(scs -> freezePlayer(EntityArgumentType.getPlayer(scs, "player")))
        );
    }
    public static boolean isFrozen(ServerPlayerEntity player){
        if(frozen.contains(player.getUuidAsString())){
            return true;
        }
        return false;
    }
    private static int freezePlayer(ServerPlayerEntity player){
        if(frozen.contains(player.getUuidAsString())){
            frozen.replace(" "+player.getUuidAsString(), "");
            return 2;
        }
        else{
            frozen+= " "+player.getUuidAsString();
            return 1;
        }
    }
}
