package com.example;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.IdentityHashMap;
import java.util.Map;

public class SuspiciousEffect extends StatusEffect {

    // Track all mobs modified by this effect and their original targets
    private final Map<MobEntity, LivingEntity> affectedMobs = new IdentityHashMap<>();

    protected SuspiciousEffect() {
        super(StatusEffectCategory.HARMFUL, 0xa020f0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Apply every tick
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (entity.age % 10 != 0) return super.applyUpdateEffect(world, entity, amplifier);

        double radius = 16.0;
        for (LivingEntity mobEntity : world.getEntitiesByClass(
                LivingEntity.class, entity.getBoundingBox().expand(radius), e -> e != entity)) {

            if (!(mobEntity instanceof PathAwareEntity pathAwareMob)) continue;

            // Save original target if not already saved
            affectedMobs.putIfAbsent(pathAwareMob, pathAwareMob.getTarget());

            // Clear target if attacking the player
            if (pathAwareMob.getTarget() == entity) {
                pathAwareMob.setTarget(null);
            }

            // Only add flee goal if mixin accessor is available
            if (pathAwareMob instanceof MobEntityAccessor accessor) {
                accessor.addFleeGoalFor(pathAwareMob, entity, (float) radius, 1.0, 1.5);
            }
        }

        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public void onEntityRemoval(ServerWorld world, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        SimonMod.LOGGER.info("ENTITY REMOVAL TRIGGER");
        super.onEntityRemoval(world, entity, amplifier, reason);
        // Reset all affected mobs
        for (Map.Entry<MobEntity, LivingEntity> entry : affectedMobs.entrySet()) {
            MobEntity mob = entry.getKey();
            LivingEntity originalTarget = entry.getValue();

            if (mob instanceof MobEntityAccessor accessor) {
                accessor.removeFleeGoalsFor(entity);
            }

            mob.setTarget(originalTarget);
        }
        affectedMobs.clear();
    }
}
