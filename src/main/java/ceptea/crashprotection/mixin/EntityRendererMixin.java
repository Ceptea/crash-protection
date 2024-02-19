package ceptea.crashprotection.mixin;

import ceptea.crashprotection.Protection;
import ceptea.crashprotection.setting.Settings;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void a(Entity en, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (en.getCustomName() != null) {
            if (en.getCustomName().getString().length() > Settings.charLimit) {
                en.setCustomName(Text.of(String.format("Name longer than %s chars.", Settings.charLimit)));
            }
        }

    }
}