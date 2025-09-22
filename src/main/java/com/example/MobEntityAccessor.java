package com.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;

public interface MobEntityAccessor {
    // Add a flee goal targeting a specific entity
    void addFleeGoal(FleeEntityGoal<?> goal, int priority);

    // Remove all flee goals that target a specific entity
    void removeFleeGoalsFor(LivingEntity entity);

    // Tick all the current goals
    void tickAllGoals();
}
