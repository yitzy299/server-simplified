package tech.dttp.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import tech.dttp.serversimplified.ServerSimplified;
import tech.dttp.serversimplified.Utils;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.screen.ScreenHandler;

import java.util.Collection;

import static tech.dttp.serversimplified.Utils.isHuman;

public class SeekInventoryCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("inv").requires(scs -> Utils.hasPermissionFromSource(scs, "inv")).then(CommandManager.argument("player",EntityArgumentType.player()).executes(scs -> {
                if (!isHuman(scs.getSource())) {
                    scs.getSource().sendError(new LiteralText("You must be a player to use this command!"));
                    return 1;
                }
                ServerPlayerEntity sender = (ServerPlayerEntity) scs.getSource().getEntity();
                Collection<ServerPlayerEntity> entities = EntityArgumentType.getPlayers(scs, "target");
                for (ServerPlayerEntity entity : entities) {
                    entity.inventory.onOpen(sender);
                    sender.openHandledScreen(new NamedScreenHandlerFactory() {
                        @Override
                        public Text getDisplayName() {
                            return entity.getName();
                    }

                        @Override
                        public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity var3) {
                            return GenericContainerScreenHandler.createGeneric9x6(id, inventory, new DoubleInventory(entity.inventory, entity.getEnderChestInventory()));
                        }
                    });
                return 1;
        }
        return 1;
    }  
        )));
        /*LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager
                .literal("seekinv")
                .requires(
                        source ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(source, "seekinv")
                                        || source.hasPermissionLevel(2)
                )
                .then(
                        CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> {
                                    try {
                                        if (!isHuman(context.getSource())) {
                                            context.getSource().sendError(new LiteralText("You must be a player to use this command!"));
                                            return 1;
                                        }
                                        ServerPlayerEntity sender = (ServerPlayerEntity) context.getSource().getEntity();
                                        Collection<ServerPlayerEntity> entities = EntityArgumentType.getPlayers(context, "target");
                                        for (ServerPlayerEntity entity : entities) {
                                            entity.inventory.onOpen(sender);
                                            sender.openHandledScreen(new NamedScreenHandlerFactory() {
                                                @Override
                                                public Text getDisplayName() {
                                                    return entity.getName();
                                                }

                                                @Override
                                                public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity var3) {
                                                    return GenericContainerScreenHandler.createGeneric9x6(id, inventory, new DoubleInventory(entity.inventory, entity.getEnderChestInventory()));
                                                }
                                            });
                                        }
                                        return 1;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return 1;
                                }))
                .executes(context -> {
                            context.getSource().sendError(new LiteralText("You must specify at least one player."));
                            return 1;
                });*/

    }

}
