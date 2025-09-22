package com.example.mixin;

import com.example.MobEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements MobEntityAccessor {
    @Shadow @Final protected GoalSelector goalSelector;

    @Override
    public boolean hasFleeGoalFor(LivingEntity entity) {
        return this.goalSelector.getGoals().stream().anyMatch(g -> {
            if (g.getGoal() instanceof FleeEntityGoal<?> fleeGoal) {
                FleeEntityGoalAccessor accessor = (FleeEntityGoalAccessor) fleeGoal;
                return accessor.getClassToFleeFrom().isInstance(entity);
            }
            return false;
        });
    }

    @Override
    public void addFleeGoalFor(PathAwareEntity mob, LivingEntity target, float radius, double slowSpeed, double fastSpeed) {
        if (!this.hasFleeGoalFor(target)) {
            this.goalSelector.add(1, new FleeEntityGoal<>(mob, target.getClass(), radius, slowSpeed, fastSpeed));
        }
    }

    @Override
    public void removeFleeGoalsFor(LivingEntity entity) {
        this.goalSelector.getGoals().removeIf(g -> {
            if (g.getGoal() instanceof FleeEntityGoal<?> fleeGoal) {
                FleeEntityGoalAccessor accessor = (FleeEntityGoalAccessor) fleeGoal;
                return accessor.getClassToFleeFrom().isInstance(entity);
            }
            return false;
        });
    }
}