package ceptea.crashprotection.mixin;

import ceptea.crashprotection.Protection;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.network.ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
    private static void onPacketGet(Packet<?> packet, PacketListener listener, CallbackInfo ci) {

        if (packet instanceof ExplosionS2CPacket) {
            ExplosionS2CPacket p = (ExplosionS2CPacket) packet;
            boolean danger = (p.getPlayerVelocityX() > Protection.reasonableVelocity || p.getPlayerVelocityX() < -Protection.reasonableVelocity || p.getPlayerVelocityY() > Protection.reasonableVelocity || p.getPlayerVelocityY() < -Protection.reasonableVelocity || p.getPlayerVelocityZ() > Protection.reasonableVelocity || p.getPlayerVelocityZ() < -Protection.reasonableVelocity);
            if (danger) {
                Protection.send(String.format("Server sent Illegal Explosion Velocity"));
                ci.cancel();
            }
        } else if (packet instanceof ParticleS2CPacket) {
            if (Protection.panicMode) {
                ci.cancel();
                return;
            }
            if (Protection.pps > 3) {
                ci.cancel();
            }
            ParticleS2CPacket p = (ParticleS2CPacket) packet;
            ParticleS2CPacketAccessor accessor = (ParticleS2CPacketAccessor) p;
            if (p.getCount() > Protection.reasonableParticleLimit) {
                accessor.setCount(Protection.reasonableParticleLimit);
            }
            if (!ci.isCancelled()) {
                Protection.pps += 1;
            }

        } else if (packet instanceof CloseScreenS2CPacket) {
            CloseScreenS2CPacket p = (CloseScreenS2CPacket) packet;
            if (Protection.mc.currentScreen instanceof ChatScreen) {
                ci.cancel();
            }
        } else if (packet instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacket p = (EntityVelocityUpdateS2CPacket) packet;

            boolean danger = (p.getVelocityX() > Protection.reasonableVelocity || p.getVelocityX() < -Protection.reasonableVelocity || p.getVelocityY() > Protection.reasonableVelocity || p.getVelocityY() < -Protection.reasonableVelocity || p.getVelocityZ() > Protection.reasonableVelocity || p.getVelocityZ() < -Protection.reasonableVelocity);
            if (danger) {
                Protection.send(String.format("Server sent Illegal Player Velocity"));
                ci.cancel();
            }
        } else if (packet instanceof EntityPositionS2CPacket) {
            EntityPositionS2CPacket p = (EntityPositionS2CPacket) packet;
            float yaw = p.getPitch();
            float pitch = p.getPitch();

            if (yaw > 360 || pitch > 90 || pitch < -90 || yaw < -360) {
                Protection.send("Server sent a illegal look position.");
                ci.cancel();
            }

        }
    }
}