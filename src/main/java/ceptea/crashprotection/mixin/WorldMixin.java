package ceptea.crashprotection.mixin;

import ceptea.crashprotection.Protection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public class WorldMixin {
    @Inject(at = @At("HEAD"), method = "tickEntity", cancellable = true)
    private void onTick(Consumer<Entity> tickConsumer, Entity entity, CallbackInfo ci) {
        try {
            tickConsumer.accept(entity);
            ci.cancel();
        } catch (Exception e) {

            Protection.mc.world.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
            Protection.send("Server sent a invaild entity that would have crashed you.");

            ci.cancel();


        }
    }
}