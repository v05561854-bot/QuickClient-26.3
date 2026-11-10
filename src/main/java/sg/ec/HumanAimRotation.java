package sg.ec;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class HumanAimRotation implements AuraRotationStrategy {
    private static final String NAME = "Human Aim";

    private static final float MIN_DURATION = 1.5F;
    private static final float MAX_DURATION = 8.0F;
    private static final float BASE_DURATION = 1.2F;
    private static final float FITTS_SCALE = 1.6F;

    private static final float OVERSHOOT_MIN = 0.02F;
    private static final float OVERSHOOT_MAX = 0.09F;
    private static final float ARC_MAX = 0.18F;
    private static final float MIN_CORRECTION = 1.0F;
    private static final float MAX_CORRECTION = 2.0F;

    private static final float TREMOR_YAW = 0.16F;
    private static final float TREMOR_PITCH = 0.11F;
    private static final float SETTLED_TREMOR = 0.35F;

    private static final float RETARGET_YAW = 6.0F;
    private static final float RETARGET_PITCH = 6.0F;
    private static final float MIN_DISTANCE = 0.01F;
    private static final float MIN_WIDTH = 0.6F;

    private boolean active;
    private int targetId = Integer.MIN_VALUE;
    private int flickTick;
    private float startYaw;
    private float startPitch;
    private float goalYaw;
    private float goalPitch;
    private float overshootYaw;
    private float overshootPitch;
    private float arcYaw;
    private float arcPitch;
    private float duration;
    private float correction;
    private float tremorYaw;
    private float tremorPitch;

    private int tremorTick = Integer.MIN_VALUE;
    private float tremorOffsetYaw;
    private float tremorOffsetPitch;

    @Override
    public String method02258() {
        return NAME;
    }

    @Override
    public void method02268() {
        this.active = false;
        this.targetId = Integer.MIN_VALUE;
        this.tremorTick = Integer.MIN_VALUE;
    }

    @Override
    public void method02266(AuraRotationController.Nested160 in, AuraRotationController.Nested161 out) {
        if (mc.player == null || in.target() == null) {
            return;
        }

        int tick = mc.player.tickCount;
        float currentYaw = in.currentYaw();
        float currentPitch = in.currentPitch();
        float desiredYaw = this.desiredYaw(in);
        float desiredPitch = this.desiredPitch(in);

        if (this.needsFlick(in, desiredYaw, desiredPitch)) {
            this.beginFlick(tick, currentYaw, currentPitch, desiredYaw, desiredPitch, in);
        }

        float elapsed = tick - this.flickTick;
        float settledAt = this.duration + this.correction;
        float targetYaw;
        float targetPitch;

        if (elapsed < this.duration) {
            float t = Mth.clamp(elapsed / this.duration, 0.0F, 1.0F);
            float eased = easeOutCubic(t);
            float arc = Mth.sin((float) Math.PI * t);
            targetYaw = Mth.lerp(eased, this.startYaw, this.goalYaw + this.overshootYaw) + this.arcYaw * arc;
            targetPitch = Mth.lerp(eased, this.startPitch, this.goalPitch + this.overshootPitch) + this.arcPitch * arc;
        } else if (elapsed < settledAt) {
            float t = Mth.clamp((elapsed - this.duration) / this.correction, 0.0F, 1.0F);
            float eased = easeOutQuad(t);
            targetYaw = Mth.lerp(eased, this.goalYaw + this.overshootYaw, this.goalYaw);
            targetPitch = Mth.lerp(eased, this.goalPitch + this.overshootPitch, this.goalPitch);
        } else {
            targetYaw = this.goalYaw;
            targetPitch = this.goalPitch;
        }

        this.updateTremor(tick, elapsed >= settledAt ? SETTLED_TREMOR : 1.0F);

        float outYaw = targetYaw + this.tremorOffsetYaw;
        float outPitch = Mth.clamp(targetPitch + this.tremorOffsetPitch, -89.0F, 89.0F);

        float maxYaw = in.isGliding() ? in.aura().method00449() : in.aura().method00448();
        float maxPitch = in.isGliding() ? in.aura().method00416() : in.aura().method00417();
        float stepYaw = Math.abs(Mth.wrapDegrees(outYaw - currentYaw));
        float stepPitch = Math.abs(outPitch - currentPitch);

        out.method05332(outYaw);
        out.method05339(outPitch);
        out.method05325(Math.min(stepYaw + 0.01F, maxYaw), Math.min(stepPitch + 0.01F, maxPitch));
        out.method05335(true);
    }

    private float desiredYaw(AuraRotationController.Nested160 in) {
        Vec3 direction = in.rayTrace() ? this.centerDirection(in) : null;
        return direction == null
            ? in.rawYaw()
            : Mth.wrapDegrees((float) (Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0));
    }

    private float desiredPitch(AuraRotationController.Nested160 in) {
        Vec3 direction = in.rayTrace() ? this.centerDirection(in) : null;
        return direction == null
            ? in.rawPitch()
            : Mth.wrapDegrees((float) Math.toDegrees(-Math.atan2(direction.y, Math.hypot(direction.x, direction.z))));
    }

    private Vec3 centerDirection(AuraRotationController.Nested160 in) {
        Vec3 direction = in.targetBox().getCenter().subtract(mc.player.getEyePosition());
        return direction.lengthSqr() < 1.0E-4 ? null : direction;
    }

    private boolean needsFlick(AuraRotationController.Nested160 in, float desiredYaw, float desiredPitch) {
        return !this.active
            || in.target().getId() != this.targetId
            || Math.abs(Mth.wrapDegrees(desiredYaw - this.goalYaw)) > RETARGET_YAW
            || Math.abs(desiredPitch - this.goalPitch) > RETARGET_PITCH;
    }

    private void beginFlick(int tick, float currentYaw, float currentPitch, float desiredYaw, float desiredPitch, AuraRotationController.Nested160 in) {
        this.active = true;
        this.targetId = in.target().getId();
        this.flickTick = tick;
        this.startYaw = currentYaw;
        this.startPitch = currentPitch;
        this.goalYaw = desiredYaw;
        this.goalPitch = desiredPitch;

        float yawDelta = Mth.wrapDegrees(desiredYaw - currentYaw);
        float pitchDelta = desiredPitch - currentPitch;
        float distance = (float) Math.hypot(yawDelta, pitchDelta);
        float width = Math.max(MIN_WIDTH, (float) Math.min(in.targetBox().getXsize(), in.targetBox().getZsize()));
        float difficulty = (float) (Math.log(distance / width + 1.0) / Math.log(2.0));
        float base = Mth.clamp(BASE_DURATION + difficulty * FITTS_SCALE, MIN_DURATION, MAX_DURATION);
        this.duration = Math.max(MIN_DURATION, base * RandomUtils.method10454(0.85F, 1.15F));
        this.correction = RandomUtils.method10454(MIN_CORRECTION, MAX_CORRECTION);

        if (distance < MIN_DISTANCE) {
            this.overshootYaw = 0.0F;
            this.overshootPitch = 0.0F;
            this.arcYaw = 0.0F;
            this.arcPitch = 0.0F;
        } else {
            float overshoot = distance * RandomUtils.method10454(OVERSHOOT_MIN * 0.7F, OVERSHOOT_MAX * 0.7F);
            float sign = RandomUtils.method10442() < 0.5F ? -1.0F : 1.0F;
            this.overshootYaw = yawDelta / distance * overshoot * sign;
            this.overshootPitch = pitchDelta / distance * overshoot * sign;

            float arc = distance * RandomUtils.method10454(-ARC_MAX * 0.5F, ARC_MAX * 0.5F);
            this.arcYaw = -pitchDelta / distance * arc;
            this.arcPitch = yawDelta / distance * arc;
        }

        this.tremorYaw = TREMOR_YAW * RandomUtils.method10454(0.6F, 1.4F);
        this.tremorPitch = TREMOR_PITCH * RandomUtils.method10454(0.6F, 1.4F);
        this.tremorTick = Integer.MIN_VALUE;
    }

    private void updateTremor(int tick, float scale) {
        if (tick == this.tremorTick) {
            return;
        }

        this.tremorTick = tick;
        this.tremorOffsetYaw = this.tremorYaw * scale * RandomUtils.method10454(-1.0F, 1.0F);
        this.tremorOffsetPitch = this.tremorPitch * scale * RandomUtils.method10454(-1.0F, 1.0F);
    }

    private static float easeOutCubic(float t) {
        float remaining = 1.0F - t;
        return 1.0F - remaining * remaining * remaining;
    }

    private static float easeOutQuad(float t) {
        return 1.0F - (1.0F - t) * (1.0F - t);
    }
}
