package ceptea.crashprotection.mixin;

import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleS2CPacket.class)
public interface ParticleS2CPacketAccessor {
	@Accessor("count")
	@Mutable
	@Final

	void setCount(int count);
}