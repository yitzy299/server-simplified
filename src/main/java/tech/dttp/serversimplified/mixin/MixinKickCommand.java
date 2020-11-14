package tech.dttp.serversimplified.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import tech.dttp.serversimplified.ServerSimplified;
import net.minecraft.server.command.KickCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KickCommand.class)
public class MixinKickCommand {

    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"))
    private static LiteralCommandNode register(CommandDispatcher dispatcher, final LiteralArgumentBuilder command) {
        command.requires(o -> ((ServerCommandSource) o).hasPermissionLevel(3) || ServerSimplified.getConfiguration().getPermissions().hasPermission(((ServerCommandSource) o).getEntity().getUuidAsString(), "kick"));
        final LiteralCommandNode build = command.build();
        dispatcher.getRoot().addChild(build);
        return build;
    }

}
