package tech.dttp.serversimplified.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import tech.dttp.serversimplified.ServerSimplified;
import net.minecraft.server.command.KickCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.BanCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BanCommand.class)
public class MixinBanCommand {

    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"))
    private static LiteralCommandNode register(CommandDispatcher dispatcher, final LiteralArgumentBuilder command) {
        command.requires(o -> {
            try {
                return ((ServerCommandSource) o).getPlayer().hasPermissionLevel(3)
                        || ServerSimplified.getConfiguration().getPermissions()
                                .hasPermission(((ServerCommandSource) o).getEntity().getUuidAsString(), "ban") == true;
            } catch (CommandSyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        });
        final LiteralCommandNode build = command.build();
        dispatcher.getRoot().addChild(build);
        return build;
    }

}
