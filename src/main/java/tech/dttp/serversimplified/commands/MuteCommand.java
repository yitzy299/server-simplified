package tech.dttp.serversimplified.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import tech.dttp.serversimplified.ServerSimplified;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.BaseText;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MuteCommand {

    public static final HashMap<String, Long> mutedPlayers = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> muteBuilder = CommandManager
                .literal("mute")
                .requires(
                        (commandSource) ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, "mute")
                                        || commandSource.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("target", EntityArgumentType.players())
                                .then(CommandManager.argument("time", MessageArgumentType.message())
                                        .executes(context -> mutePlayer(context, EntityArgumentType.getPlayers(context, "target"), true, MessageArgumentType.getMessage(context, "time"))))
                                .executes((context) -> mutePlayer(context, EntityArgumentType.getPlayers(context, "target"), true, null))
                )
                .executes(context -> {
                    context.getSource().sendError(new LiteralText("You must specify at least one player."));
                    return 1;
                });

        LiteralArgumentBuilder<ServerCommandSource> unmuteBuilder = CommandManager
                .literal("unmute")
                .requires(
                        (commandSource) ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, "unmute")
                                        || commandSource.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("target", EntityArgumentType.players())
                                .executes((context) -> mutePlayer(context, EntityArgumentType.getPlayers(context, "target"), false, null))
                )
                .executes(context -> {
                    context.getSource().sendError(new LiteralText("You must specify at least one player."));
                    return 1;
                });

        dispatcher.register(muteBuilder);
        dispatcher.register(unmuteBuilder);
    }

    private static int mutePlayer(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target, boolean muted, Text time) {
        long muteTime = -1L;
        Duration muteDuration = null;
        if (time != null) {
            try {
                muteTime = Instant.now().plus(muteDuration = parseTime(time.asString())).toEpochMilli();
            } catch (NumberFormatException e) {
                context.getSource().sendError(new LiteralText("Failed to parse time. Input must be numerical!"));
                return 1;
            } catch (IllegalStateException e) {
                context.getSource().sendError(new LiteralText("Failed to parse time. " + e.getMessage()));
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        }

        int i = 0;
        for (ServerPlayerEntity entity : target) {
            i++;
            if (muted)
                mutedPlayers.put(entity.getUuidAsString(), muteTime);
            else
                mutedPlayers.remove(entity.getUuidAsString());
        }

        String timeAppend = muteTime == -1 ? "" : " for " + humanReadableDuration(muteDuration);
        if (i == 1) {
            context.getSource().sendFeedback(new LiteralText((muted ? "Muted" : "Unmuted") +  " ").append(target.iterator().next().getName()).append(timeAppend + "."), false);
        } else {
            context.getSource().sendFeedback(new LiteralText((muted ? "Muted" : "Unmuted") +  i + " players" + timeAppend + "."), false);
        }
        return i;
    }

    public static String humanReadableDuration(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private static Pair<Integer, String> removeAndReturn(String s, char c) throws NumberFormatException {
        int index = s.indexOf(c);
        if (index != -1) {
            String s1 = s.substring(0, index);
            s = s.substring(index+1);
            index = Integer.parseInt(s1);
            return new Pair(index, s);
        }

        return new Pair(0, s);
    }

    private static Duration parseTime(String formattedText) throws NumberFormatException {
        formattedText = formattedText.trim().replace(" ", "").toLowerCase();

        Duration duration = Duration.ZERO;

        Pair<Integer, String> pair = removeAndReturn(formattedText, 'y');
        duration = duration.plus(Duration.ofDays(pair.key * 365));
        formattedText = pair.value;

        pair = removeAndReturn(formattedText, 'd');
        duration = duration.plus(Duration.ofDays(pair.key));
        formattedText = pair.value;

        pair = removeAndReturn(formattedText, 'h');
        duration = duration.plus(Duration.ofHours(pair.key));
        formattedText = pair.value;

        pair = removeAndReturn(formattedText, 'm');
        duration = duration.plus(Duration.ofMinutes(pair.key));
        formattedText = pair.value;

        pair = removeAndReturn(formattedText, 's');
        duration = duration.plus(Duration.ofSeconds(pair.key));
        formattedText = pair.value;

        if (!formattedText.isEmpty()) {
            throw new IllegalStateException("Did not expect '" + formattedText + "'");
        }

        return duration;
    }

    public static boolean isMuted(String uuid) {
        if (!mutedPlayers.containsKey(uuid)) return false;
        long epoch = mutedPlayers.get(uuid);
        if (epoch != -1 && System.currentTimeMillis() > epoch) {
            mutedPlayers.remove(uuid);
            return false;
        }
        return true;
    }

    public static JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, Long> entry : mutedPlayers.entrySet()) {
            object.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
        }
        return object;
    }

    public static void fromJson(JsonObject muted) throws IllegalStateException, ClassCastException {
        mutedPlayers.clear();
        for (Map.Entry<String, JsonElement> entry : muted.entrySet()) {
            mutedPlayers.put(entry.getKey(), entry.getValue().getAsLong());
        }
    }

    private static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
