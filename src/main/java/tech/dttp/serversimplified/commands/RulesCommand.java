package tech.dttp.serversimplified.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import tech.dttp.serversimplified.ServerSimplified;

public class RulesCommand {
    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher
            .register(CommandManager.literal("rules").executes(ctx -> readRules(ctx.getSource())));
    }
    private static int readRules(ServerCommandSource scs) throws CommandSyntaxException {
        Text rules;
        try {
            rules = getRules(scs);
        } catch (CommandSyntaxException e) {
            rules = null;
        }
        if(rules == null){
            scs.getPlayer().sendSystemMessage(new LiteralText("Your server admin has not yet added the rules to their server").formatted(Formatting.RED), Util.NIL_UUID);
        }
        scs.getPlayer().sendSystemMessage(rules, Util.NIL_UUID);
        return 1;

    }

    private static Text getRules(ServerCommandSource scs) throws CommandSyntaxException {
        String rulesFileContents;
        Text rulesText;
        try {
            rulesFileContents = readFile("config/rules.json");
            rulesText = Texts.parse(scs, new LiteralText(rulesFileContents), scs.getEntity(), 0);
        } catch (IOException e) {
            return null;
        }
        String output = rulesFileContents;
        scs.getPlayer().sendSystemMessage(rulesText, Util.NIL_UUID);
        return rulesText;
    }
    public static String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }
}
