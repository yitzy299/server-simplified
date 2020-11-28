package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import tech.dttp.serversimplified.ServerSimplified;
import tech.dttp.serversimplified.Utils;

import static net.minecraft.server.command.CommandManager.literal;

public class ServerMuteCommand {
    private static Boolean isServerMuted = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("servermute")
                .requires(scs -> Utils.hasPermissionFromSource(scs, "servermute"))
                .executes(scs -> setServerMute(scs)));

    }

    private static int setServerMute(CommandContext<ServerCommandSource> scs) {
        ServerMuteCommand.isServerMuted = !ServerMuteCommand.isServerMuted;
        String message = String.format("%s %s","§3","Server mute is now set to: "+ServerMuteCommand.isServerMuted);
        Text messageText = new LiteralText(message);
        try {
            scs.getSource().getPlayer().sendSystemMessage(messageText, Util.NIL_UUID);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 1;
    }
    public static Boolean isMuted() {
        return ServerMuteCommand.isServerMuted;
    }
}
