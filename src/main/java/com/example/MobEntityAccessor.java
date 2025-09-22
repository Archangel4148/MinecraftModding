package com.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;

public interface MobEntityAccessor {
    boolean hasFleeGoalFor(LivingEntity entity);

    void addFleeGoalFor(PathAwareEntity mob, LivingEntity target, float radius, double slowSpeed, double fastSpeed);

    void removeFleeGoalsFor(LivingEntity entity);
}
