package com.example.mixin;

import com.example.FleeSpecificEntityGoal;
import com.example.MobEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements MobEntityAccessor {

    @Shadow
    @Final
    protected GoalSelector goalSelector;

    @Override
    public void addFleeGoal(FleeEntityGoal<?> goal, int priority) {
        this.goalSelector.add(priority, goal);
    }

    @Override
    public void removeFleeGoalsFor(LivingEntity entity) {
        this.goalSelector.clear(goal -> goal instanceof FleeSpecificEntityGoal fleeGoal && fleeGoal.getTarget() == entity);
    }

    @Override
    public void tickAllGoals() {
        this.goalSelector.getGoals().forEach(pg -> pg.getGoal().tick());
    }
}
