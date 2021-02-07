package tech.dttp.serversimplified.mixin;

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

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity entity, CallbackInfo info) {
        connection.send(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, VanishCommand.getVanished()));
    }

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("TAIL"))
    protected void checkMaintenance (ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        if (MaintenanceModeCommand.isMaintenance() && !player.hasPermissionLevel(3)) {
            player.networkHandler.disconnect(new LiteralText(MaintenanceModeCommand.getReason()));
            LogManager.getLogger().info("Kicked "+player.getName().asString()+" for maintenance mode");
            return;
        }
    }

}
