package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import tech.dttp.serversimplified.Utils;

import static net.minecraft.server.command.CommandManager.literal;

public class ServerMuteCommand {
    private static Boolean isServerMuted = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("servermute")
                .requires(scs -> {
                    try {
                        if (scs.getPlayer().hasPermissionLevel(3)) return true;
                    } catch (CommandSyntaxException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        return Utils.hasPermission(scs.getPlayer(), "servermute");
                    } catch (CommandSyntaxException e) {
                        return false;
                    }
                })
                .executes(scs -> setServerMute(scs)));

    }

    private static int setServerMute(CommandContext<ServerCommandSource> scs) {
        ServerMuteCommand.isServerMuted = !ServerMuteCommand.isServerMuted;
        Text messageText = new LiteralText(isServerMuted ? "Muted the server":"Unmuted the server");
        scs.getSource().sendFeedback(messageText, true);
        return 1;
    }
    public static Boolean isMuted() {
        return ServerMuteCommand.isServerMuted;
    }
}
