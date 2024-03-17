package ceptea.crashprotection.mixin;

import ceptea.crashprotection.Protection;
import ceptea.crashprotection.setting.Settings;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.network.ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
    private static void onPacketGet(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (packet instanceof GameStateChangeS2CPacket p) {
            GameStateChangeS2CPacket.Reason reason = p.getReason();
            if (reason == GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN) {
                Protection.mc.player.sendMessage(Text.of("Prevented. illegal demo screen packet."));

                ci.cancel();
            }
            if (reason == GameStateChangeS2CPacket.ELDER_GUARDIAN_EFFECT) {
                if (Protection.pps > 3) {
                    ci.cancel();
                }
            }
            if (!ci.isCancelled()) {
                Protection.pps += 1;
            }
        }
        if (packet instanceof ExplosionS2CPacket p) {
            boolean danger = (p.getPlayerVelocityX() > Settings.reasonableVelocity || p.getPlayerVelocityX() < -Settings.reasonableVelocity || p.getPlayerVelocityY() > Settings.reasonableVelocity || p.getPlayerVelocityY() < -Settings.reasonableVelocity || p.getPlayerVelocityZ() > Settings.reasonableVelocity || p.getPlayerVelocityZ() < -Settings.reasonableVelocity);
            if (danger) {
                Protection.send("Server sent Illegal Explosion Velocity");
                ci.cancel();
            }
        } else if (packet instanceof ParticleS2CPacket p) {
            if (Protection.panicMode) {
                ci.cancel();
                return;
            }
            if (Protection.pps > 3) {
                ci.cancel();
            }
            ParticleS2CPacketAccessor accessor = (ParticleS2CPacketAccessor) p;
            if (p.getCount() > Settings
                    .reasonableParticleLimit) {
                accessor.setCount(Settings.reasonableParticleLimit);
            }
            if (!ci.isCancelled()) {
                Protection.pps += 1;
            }

        } else if (packet instanceof CloseScreenS2CPacket) {
            if (!(Protection.mc.currentScreen instanceof HandledScreen<?>)) {
                ci.cancel();
            }
        } else if (packet instanceof EntityVelocityUpdateS2CPacket p) {

            boolean danger = (p.getVelocityX() > Settings.reasonableVelocity || p.getVelocityX() < -Settings.reasonableVelocity || p.getVelocityY() > Settings.reasonableVelocity || p.getVelocityY() < -Settings.reasonableVelocity || p.getVelocityZ() > Settings.reasonableVelocity || p.getVelocityZ() < -Settings.reasonableVelocity);
            if (danger) {
                Protection.send("Server sent Illegal Player Velocity");
                ci.cancel();
            }
        } else if (packet instanceof EntityPositionS2CPacket p) {
            float pitch = p.getPitch();

            if (pitch > 90 || pitch < -90) {
//                Protection.send("Server sent a illegal look position.");
                ci.cancel();
            }

        }
    }
}