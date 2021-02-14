package tech.dttp.serversimplified.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.dttp.serversimplified.ServerSimplified;
import tech.dttp.serversimplified.commands.MaintenanceModeCommand;
import tech.dttp.serversimplified.commands.VanishCommand;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {

    @Shadow public abstract boolean isOperator(GameProfile profile);

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity entity, CallbackInfo info) {
        connection.send(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, VanishCommand.getVanished()));
    }

    @Inject(method = "checkCanJoin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void checkMaintenance (SocketAddress sa, GameProfile profile, CallbackInfoReturnable cir) {
        if(MaintenanceModeCommand.isMaintenance() && !((PlayerManager)(Object)this).isOperator(profile) && !ServerSimplified.getConfiguration().getPermissions().hasPermission(profile.getId().toString(), "maintenance")) {
            cir.setReturnValue(new TranslatableText("Server is closed for maintenance"));
        }
    }
}
