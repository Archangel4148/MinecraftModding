package com.example.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FleeEntityGoal.class)
public interface FleeEntityGoalAccessor {
    @Accessor("classToFleeFrom")
    Class<? extends LivingEntity> getClassToFleeFrom();
}
