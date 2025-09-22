package com.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;

public class FleeSpecificEntityGoal extends FleeEntityGoal<LivingEntity> {

    private final LivingEntity target;
    private final double slow;
    private final double fast;

    public FleeSpecificEntityGoal(PathAwareEntity mob, LivingEntity target, float fleeDistance, double slowSpeed, double fastSpeed) {
        super(mob, LivingEntity.class, fleeDistance, slowSpeed, fastSpeed);
        this.target = target;
        this.slow = slowSpeed;
        this.fast = fastSpeed;
    }

    @Override
    public boolean canStart() {
        if (this.target == null || !this.target.isAlive() || this.mob.squaredDistanceTo(this.target) > this.fleeDistance * this.fleeDistance) {
            return false;
        }

        Vec3d fleePos = NoPenaltyTargeting.findFrom(this.mob, 16, 7, this.target.getPos());
        if (fleePos == null) return false;

        this.fleePath = this.fleeingEntityNavigation.findPathTo(fleePos.x, fleePos.y, fleePos.z, 0);
        return this.fleePath != null;
    }

    @Override
    public boolean shouldContinue() {
        return !this.fleeingEntityNavigation.isIdle() && this.target.isAlive();
    }

    @Override
    public void stop() {
        this.fleePath = null;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        double distanceSq = this.mob.squaredDistanceTo(this.target);
        this.mob.getNavigation().setSpeed(distanceSq < 49.0 ? this.fast : this.slow);
    }

    public LivingEntity getTarget() {
        return this.target;
    }
}
