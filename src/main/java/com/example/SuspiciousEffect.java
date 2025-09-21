package com.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;

public class SuspiciousEffect extends StatusEffect {

    protected SuspiciousEffect() {
        super(StatusEffectCategory.HARMFUL, 0xa020f0);
    }

    // Called every tick to check if the effect can be applied
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    // Called when the effect is applied
    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        // Do the potion thing!

        entity.damage(world, ModDamageTypes.of(world, ModDamageTypes.SUSPICIOUS_DAMAGE_TYPE), 5.0f);

        return super.applyUpdateEffect(world, entity, amplifier);
    }
}
