package com.example.mixin;

import com.example.ModEffects; // replace with your actual effects registry class
import com.example.SimonMod;
import com.example.SuspiciousEffect; // replace with the class where your cleanup logic lives
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "onStatusEffectsRemoved", at = @At("TAIL"))
    private void example$onStatusEffectsRemoved(Collection<StatusEffectInstance> effects, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        SimonMod.LOGGER.info("STATUS EFFECT REMOVED!!!");
        for (StatusEffectInstance inst : effects) {
            // Check if the removed effect is your custom "fear" effect
            if (inst.getEffectType().equals(ModEffects.SUSPICIOUS)) {
                // Call your cleanup logic
                SuspiciousEffect.cleanup(self);
            }
        }
    }
}
