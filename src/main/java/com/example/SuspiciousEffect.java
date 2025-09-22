package com.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class SuspiciousEffect extends StatusEffect {

    // Track mobs per affected entity
    private static final Map<LivingEntity, Map<MobEntity, LivingEntity>> allAffected =
            new WeakHashMap<>();

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

        Map<MobEntity, LivingEntity> affectedMobs =
                allAffected.computeIfAbsent(entity, e -> new IdentityHashMap<>());

        double radius = 16.0;
        for (LivingEntity mobEntity : world.getEntitiesByClass(
                LivingEntity.class, entity.getBoundingBox().expand(radius), e -> e != entity)) {

            if (!(mobEntity instanceof PathAwareEntity pathAwareMob)) continue;

            // Skip mobs already affected by this entity
            if (affectedMobs.containsKey(pathAwareMob)) continue;

            // Save original target (null is fine)
            affectedMobs.put(pathAwareMob, pathAwareMob.getTarget());

            // Add flee goal if mob supports it
            if (pathAwareMob instanceof MobEntityAccessor accessor) {
                FleeSpecificEntityGoal fleeGoal =
                        new FleeSpecificEntityGoal(pathAwareMob, entity, (float) radius, 1.0, 1.5);
                accessor.addFleeGoal(fleeGoal, 1);
            }
        }

        return super.applyUpdateEffect(world, entity, amplifier);
    }

    public static void cleanup(LivingEntity entity) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return;

        Map<MobEntity, LivingEntity> affectedMobs = allAffected.remove(entity);
        if (affectedMobs == null) return;

        for (Map.Entry<MobEntity, LivingEntity> entry : affectedMobs.entrySet()) {
            MobEntity mob = entry.getKey();
            LivingEntity originalTarget = entry.getValue();

            if (mob == null || !mob.isAlive()) continue;

            if (mob instanceof MobEntityAccessor accessor) {
                // Remove flee goals safely
                accessor.removeFleeGoalsFor(entity);
            }

            if (mob instanceof PathAwareEntity pathAware) {
                // Stop navigation safely
                pathAware.getNavigation().stop();
            }

            // Restore original target if still alive in same world
            if (originalTarget != null && originalTarget.isAlive()
                    && originalTarget.getWorld() == mob.getWorld()) {
                mob.setTarget(originalTarget);
            } else {
                mob.setTarget(null);
            }
        }
    }
}
