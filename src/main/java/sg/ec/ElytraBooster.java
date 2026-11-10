package sg.ec;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ElytraBooster extends ClientModule {
    // Settings
    private final ModeSetting mode = new ModeSetting("Mode")
        .method05597("Normal", "Custom", "Client")
        .method03017("Normal")
        .method03023(this);

    private final ModeSetting antiCheatMode = new ModeSetting("AntiCheat Mode")
        .method05597("ReallyWorld", "Grim")
        .method03017("ReallyWorld")
        .method03025(() -> mode.method05600("Client"))
        .method03023(this);

    private final ModeSetting version = new ModeSetting("Version")
        .method05597("1.17.1", "1.18.1")
        .method03017("1.18.1")
        .method03025(() -> mode.method05600("Client"))
        .method03023(this);

    private final ToggleSetting speedAdaptation = new ToggleSetting("Speed Adaptation")
        .method03017(false)
        .method03023(this);

    private final NumberSetting<Float> vectorDistance = new NumberSetting<Float>("Vector Distance")
        .method10835(1.0F)
        .method10838(8.0F)
        .method10834(0.5F)
        .method03017(6.0F)
        .method03025(speedAdaptation::method05999)
        .method03023(this);

    private final NumberSetting<Float> slowdownStrength = new NumberSetting<Float>("Slowdown Strength")
        .method10835(0.1F)
        .method10838(1.0F)
        .method10834(0.05F)
        .method03017(1.0F)
        .method03025(speedAdaptation::method05999)
        .method03023(this);

    // Custom mode yaw/pitch settings per speed range
    private final NumberSetting<Float>[] yawSettings = new NumberSetting[9];
    private final NumberSetting<Float>[] pitchSettings = new NumberSetting[9];

    // Client mode settings
    private final NumberSetting<Float> clientSpeedX = new NumberSetting<Float>("Speed X")
        .method10835(1.5F)
        .method10838(2.5F)
        .method10834(0.01F)
        .method03017(1.5F)
        .method03025(() -> mode.method05600("Custom"))
        .method03023(this);

    private final NumberSetting<Float> clientSpeedY = new NumberSetting<Float>("Speed Y")
        .method10835(1.5F)
        .method10838(2.5F)
        .method10834(0.01F)
        .method03017(1.5F)
        .method03025(() -> mode.method05600("Custom"))
        .method03023(this);

    // Presets
    private final ActionListSetting presets = new ActionListSetting("Presets")
        .method03025(() -> mode.method05600("Custom"))
        .method03023(this);

    // Runtime fields
    private double boostFactor = 0.0;
    private int lastBoostTick = 0;
    private Vec3 lastTargetVelocity = null;
    private int boostTicks = 0;
    private float lastYaw = Float.NaN;
    private float lastPitch = Float.NaN;
    private float currentYaw = Float.NaN;
    private float currentPitch = Float.NaN;
    private int lastMovePacketTick = -1;
    private Vec3 lastDeltaMovement = null;
    private int lastDeltaMovementTick = -1;

    private static final float[] SPEED_RANGES = {5, 10, 15, 20, 25, 30, 35, 40, 45};
    private static final float DEFAULT_YAW = 1.5F;
    private static final float DEFAULT_PITCH = 1.5F;

    // Preset values for different modes
    private static final float[] DEFAULT_PRESET_YAW = {1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F};
    private static final float[] DEFAULT_PRESET_PITCH = {1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F, 1.5F};
    private static final float[] GRIM_PRESET_YAW = {1.58F, 1.62F, 1.65F, 1.69F, 1.73F, 1.79F, 1.79F, 1.79F, 1.81F};
    private static final float[] GRIM_PRESET_PITCH = {1.54F, 1.57F, 1.58F, 1.6F, 1.63F, 1.7F, 1.89F, 2.01F, 2.24F};
    private static final float[] REALLYWORLD_RAGE_PRESET_YAW = {1.56F, 1.61F, 1.74F, 1.76F, 1.77F, 1.88F, 1.9F, 2.01F, 2.24F};
    private static final float[] REALLYWORLD_RAGE_PRESET_PITCH = {1.58F, 1.62F, 1.65F, 1.69F, 1.73F, 1.79F, 1.79F, 1.79F, 1.81F};

    // Grim/ReallyWorld constants
    private static final double GRIM_SPEED_1_17 = 1.7;
    private static final double GRIM_SPEED_1_18_1 = 1.68;
    private static final double GRIM_SPEED_1_18_2 = 1.93;
    private static final double GRIM_SPEED_1_18_3 = 1.9;
    private static final double MAX_TARGET_DISTANCE = 5.0;
    private static final double MIN_VELOCITY_THRESHOLD = 1.0E-6;
    private static final double VELOCITY_CHANGE_THRESHOLD = 0.05;
    private static final double ANGLE_THRESHOLD = 35.0;
    private static final double DISTANCE_FACTOR_1 = 5.0;
    private static final double DISTANCE_FACTOR_2 = 15.0;
    private static final double DISTANCE_FACTOR_3 = 75.0;
    private static final double SPEED_FACTOR_1 = 0.8;
    private static final double SPEED_FACTOR_2 = 0.3;
    private static final double MAX_BOOST = 25.0;
    private static final double MIN_BOOST = 0.5;
    private static final double BOOST_DECAY = -0.3;
    private static final double BOOST_ACCEL = 0.25;
    private static final double MIN_BOOST_THRESHOLD = 0.001;
    private static final double COSINE_THRESHOLD = 0.6;
    private static final double VERTICAL_OFFSET_1 = -1.7;
    private static final double VERTICAL_OFFSET_2 = -0.05;
    private static final double VERTICAL_OFFSET_3 = 1.7;
    private static final double VERTICAL_OFFSET_4 = 0.9;
    private static final double DRAG_MULTIPLIER = 1.7;
    private static final double DRAG_ADDITION = 0.05;
    private static final double MAX_ANGLE = 16.0;
    private static final double MAX_ITERATIONS = 16;
    private static final double CONVERGENCE_THRESHOLD = 0.5;
    private static final double MAX_CHANGE = 0.04;
    private static final double MIN_CHANGE = 0.02;
    private static final double DRAG_FACTOR = 0.1;
    private static final double DRAG_FACTOR_2 = 0.5;
    private static final double YAW_PITCH_MAX = 0.5;
    private static final float DEG_TO_RAD = (float) (Math.PI / 180.0);
    private static final double VEL_THRESHOLD = 0.003;
    private static final double GRAVITY_FACTOR = 0.75;
    private static final double VERTICAL_DAMPING = -0.1;
    private static final double HORIZONTAL_DAMPING = 0.04;
    private static final double MAX_VERTICAL_SPEED = 3.2;
    private static final double LERP_FACTOR = 0.1;
    private static final double DRAG_1 = 0.99;
    private static final double DRAG_2 = 0.98;
    private static final double DRAG_3 = 0.99;
    private static final double GRAVITY = -1.0;
    private static final double SPEED_MULT_1 = 1.3;
    private static final double MAX_SPEED = 8.0;
    private static final double MAX_DISTANCE = 40.0;
    private static final double ANTI_CHEAT_THRESHOLD = -1.0;
    private static final double VERTICAL_ADJUST_1 = -0.05;
    private static final double VERTICAL_ADJUST_2 = 1.7;
    private static final double VERTICAL_ADJUST_3 = 0.05;
    private static final double VERTICAL_ADJUST_4 = 0.05;
    private static final double MIN_TIME = 0.001;
    private static final double FALLBACK_SPEED = 2.5;
    private static final double MIN_SLOWDOWN = 0.5;
    private static final double MAX_SLOWDOWN = 0.1;
    private static final double SLOWDOWN_FACTOR_1 = 0.5;
    private static final double SLOWDOWN_FACTOR_2 = 0.5;
    private static final double SLOWDOWN_FACTOR_3 = 0.5;
    private static final double MIN_SLOWDOWN_THRESHOLD = 1.0E-9;
    private static final double MAX_SLOWDOWN_SPEED = 2.5;
    private static final double SLOWDOWN_FACTOR_4 = 0.5;
    private static final double SLOWDOWN_FACTOR_5 = 0.1;
    private static final double SLOWDOWN_FACTOR_6 = 0.5;
    private static final double SLOWDOWN_FACTOR_7 = 0.1;
    private static final double SLOWDOWN_FACTOR_8 = 2.0;
    private static final double SLOWDOWN_FACTOR_9 = 4.0;
    private static final double SLOWDOWN_FACTOR_10 = 2.0;
    private static final double SLOWDOWN_FACTOR_11 = 2.0;
    private static final float YAW_RANGE = 5.0F;
    private static final float PITCH_RANGE = 5.0F;
    private static final float MAX_PITCH = 90.0F;
    private static final float MAX_YAW = 180.0F;
    private static final float HALF_PITCH = 45.0F;
    private static final float MAX_YAW_2 = 90.0F;

    public ElytraBooster() {
        super(ModuleCategory.Movement, ModuleSubCategory.Main);
        this.mode.method05597("Normal", "Custom", "Client");
        this.mode.method03017("Normal");
        this.mode.method03023(this);

        this.antiCheatMode.method05597("ReallyWorld", "Grim");
        this.antiCheatMode.method03017("ReallyWorld");
        this.antiCheatMode.method03025(() -> this.mode.method05600("Client"));
        this.antiCheatMode.method03023(this);

        this.version.method05597("1.17.1", "1.18.1");
        this.version.method03017("1.18.1");
        this.version.method03025(() -> this.mode.method05600("Client"));
        this.version.method03023(this);

        this.speedAdaptation.method03017(false);
        this.speedAdaptation.method03023(this);

        this.vectorDistance.method10835(1.0F);
        this.vectorDistance.method10838(8.0F);
        this.vectorDistance.method10834(0.5F);
        this.vectorDistance.method03017(6.0F);
        this.vectorDistance.method03025(this.speedAdaptation::method05999);
        this.vectorDistance.method03023(this);

        this.slowdownStrength.method10835(0.1F);
        this.slowdownStrength.method10838(1.0F);
        this.slowdownStrength.method10834(0.05F);
        this.slowdownStrength.method03017(1.0F);
        this.slowdownStrength.method03025(this.speedAdaptation::method05999);
        this.slowdownStrength.method03023(this);

        this.clientSpeedX.method10835(1.5F);
        this.clientSpeedX.method10838(2.5F);
        this.clientSpeedX.method10834(0.01F);
        this.clientSpeedX.method03017(1.5F);
        this.clientSpeedX.method03025(() -> this.mode.method05600("Custom"));
        this.clientSpeedX.method03023(this);

        this.clientSpeedY.method10835(1.5F);
        this.clientSpeedY.method10838(2.5F);
        this.clientSpeedY.method10834(0.01F);
        this.clientSpeedY.method03017(1.5F);
        this.clientSpeedY.method03025(() -> this.mode.method05600("Custom"));
        this.clientSpeedY.method03023(this);

        // Initialize custom mode yaw/pitch settings
        String[] rangeLabels = {"0-5", "5-10", "10-15", "15-20", "20-25", "25-30", "30-35", "35-40", "40-45"};
        for (int i = 0; i < this.yawSettings.length; i++) {
            this.yawSettings[i] = new NumberSetting<Float>("Yaw " + rangeLabels[i])
                .method10835(DEFAULT_YAW)
                .method10838(2.5F)
                .method10834(0.01F)
                .method03017(DEFAULT_YAW)
                .method03025(() -> this.mode.method05600("Custom"))
                .method03023(this);
            this.pitchSettings[i] = new NumberSetting<Float>("Pitch " + rangeLabels[i])
                .method10835(DEFAULT_PITCH)
                .method10838(2.5F)
                .method10834(0.01F)
                .method03017(DEFAULT_PITCH)
                .method03025(() -> this.mode.method05600("Custom"))
                .method03023(this);
        }

        // Setup presets
        this.presets.method02723("Default", () -> applyPreset(DEFAULT_PRESET_YAW, DEFAULT_PRESET_PITCH))
            .method02723("Grim", () -> applyPreset(GRIM_PRESET_YAW, GRIM_PRESET_PITCH))
            .method02723("ReallyWorld Rage", () -> applyPreset(REALLYWORLD_RAGE_PRESET_YAW, REALLYWORLD_RAGE_PRESET_PITCH))
            .method03025(() -> this.mode.method05600("Custom"))
            .method03023(this);

        this.method03935("Elytra Booster");
        this.currentYaw = Float.NaN;
        this.currentPitch = Float.NaN;
    }

    private void applyPreset(float[] yawVals, float[] pitchVals) {
        for (int i = 0; i < this.yawSettings.length; i++) {
            this.yawSettings[i].method03014(yawVals[i]);
            this.pitchSettings[i].method03014(pitchVals[i]);
        }
    }

    public Vec3 getLastTargetVelocity() {
        return this.lastTargetVelocity;
    }

    public ModeSetting getVersion() {
        return this.version;
    }

    public ToggleSetting getSpeedAdaptation() {
        return this.speedAdaptation;
    }

    public int getLastBoostTick() {
        return this.lastBoostTick;
    }

    public float getCurrentPitch() {
        return this.currentPitch;
    }

    public int getBoostTicks() {
        return this.boostTicks;
    }

    public NumberSetting<Float> getClientSpeedX() {
        return this.clientSpeedX;
    }

    public ModeSetting getMode() {
        return this.mode;
    }

    public NumberSetting<Float>[] getYawSettings() {
        return this.yawSettings;
    }

    public int getLastMovePacketTick() {
        return this.lastMovePacketTick;
    }

    public NumberSetting<Float> getClientSpeedY() {
        return this.clientSpeedY;
    }

    public NumberSetting<Float>[] getPitchSettings() {
        return this.pitchSettings;
    }

    public ModeSetting getAntiCheatMode() {
        return this.antiCheatMode;
    }

    public NumberSetting<Float> getVectorDistance() {
        return this.vectorDistance;
    }

    public NumberSetting<Float> getSlowdownStrength() {
        return this.slowdownStrength;
    }

    public ActionListSetting getPresets() {
        return this.presets;
    }

    public Vec3 getLastDeltaMovement() {
        return this.lastDeltaMovement;
    }

    public int getLastDeltaMovementTick() {
        return this.lastDeltaMovementTick;
    }

    public float getCurrentYaw() {
        return this.currentYaw;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    public double getBoostFactor() {
        return this.boostFactor;
    }

    private Vec2 calculateOptimalRotation(Vec3 lookVec, Vec3 targetVel, Vec3 playerVel, double maxSpeed, double distance, double gravity) {
        float playerYaw = mc.player.getYRot();
        float playerPitch = mc.player.getXRot();
        float yawDiff = Mth.wrapDegrees(playerYaw - getPreviousYaw(playerYaw));
        float pitchDiff = playerPitch - getPreviousPitch(playerPitch);

        float[] pitches = new float[]{
            playerPitch,
            Mth.clamp(playerPitch + pitchDiff * 0.5F, -90.0F, 90.0F),
            Mth.clamp(playerPitch + pitchDiff, -90.0F, 90.0F)
        };

        Vec3[] lookDirs = new Vec3[]{
            lookVec,
            Vec3.directionFromRotation(pitches[1], playerYaw + yawDiff * 0.5F),
            Vec3.directionFromRotation(pitches[2], playerYaw + yawDiff)
        };

        Vec3[] minBounds = new Vec3[lookDirs.length];
        Vec3[] maxBounds = new Vec3[lookDirs.length];

        double predictionTime = calculatePredictionTime(lookVec);
        Vec3 predictedMin = new Vec3(
            calculateBound(lookVec.x, predictionTime, gravity, true),
            calculateBound(lookVec.y, predictionTime, gravity, true),
            calculateBound(lookVec.z, predictionTime, gravity, true)
        );
        Vec3 predictedMax = new Vec3(
            calculateBound(lookVec.x, predictionTime, gravity, false),
            calculateBound(lookVec.y, predictionTime, gravity, false),
            calculateBound(lookVec.z, predictionTime, gravity, false)
        );

        for (int i = 0; i < lookDirs.length; i++) {
            Vec3 projected = projectVelocity(targetVel, lookDirs[i], pitches[i]);
            minBounds[i] = new Vec3(
                projected.x + Math.min(0.0, predictedMin.x - targetVel.x),
                projected.y + Math.min(0.0, predictedMin.y - targetVel.y),
                projected.z + Math.min(0.0, predictedMin.z - targetVel.z)
            );
            maxBounds[i] = new Vec3(
                projected.x + Math.max(0.0, predictedMax.x - targetVel.x),
                projected.y + Math.max(0.0, predictedMax.y - targetVel.y),
                projected.z + Math.max(0.0, predictedMax.z - targetVel.z)
            );
        }

        double bestPitch = binarySearchPitch(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, maxSpeed, distance, gravity, true);
        double bestYaw = binarySearchPitch(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, bestPitch, maxSpeed, gravity, false);

        bestPitch = binarySearchPitch(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, maxSpeed, bestYaw, gravity, true);
        bestYaw = binarySearchPitch(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, bestPitch, maxSpeed, gravity, false);

        return new Vec2((float) bestYaw, (float) bestPitch);
    }

    private boolean isInBounds(double val, double min, double max, float tolerance) {
        return Math.max(-tolerance, (Math.min(tolerance, val) + Math.min(tolerance, max)) * 1.7) * 0.9
            + tolerance + Math.max(0.0, Math.max(tolerance, val) + Math.max(tolerance, max)) * 1.7
            + val * (0.25 + tolerance * 0.3)
            >= Math.min(val * (0.25 + tolerance * 0.3) + min * (0.1 + tolerance * 0.4),
            val * (0.25 + tolerance * 0.3) + val * 0.7)
            && val * (0.25 + tolerance * 0.3) + max * 0.7
            <= Math.max(val * (0.25 + tolerance * 0.3) + min * (0.1 + tolerance * 0.4),
            val * (0.25 + tolerance * 0.3) + val * 0.7);
    }

    private float getPreviousYaw(float current) {
        if (mc.player != null && this.lastMovePacketTick >= 0 && mc.player.tickCount - this.lastMovePacketTick <= 1) {
            return this.lastMovePacketTick == mc.player.tickCount ? this.currentYaw : this.lastYaw;
        }
        return current;
    }

    private float getPreviousPitch(float current) {
        if (mc.player != null && this.lastMovePacketTick >= 0 && mc.player.tickCount - this.lastMovePacketTick <= 1) {
            return this.lastMovePacketTick == mc.player.tickCount ? this.currentPitch : this.lastPitch;
        }
        return current;
    }

    private double calculatePredictionTime(Vec3 lookVec) {
        Vec3 predictedDir = getPredictedDirection(lookVec);
        double angle = Math.toDegrees(Math.acos(Mth.clamp(lookVec.dot(predictedDir), -1.0, 1.0)));
        return Math.toRadians(Mth.clamp(angle * 1.3, 8.0, 40.0));
    }

    private Vec3 getPredictedDirection(Vec3 lookVec) {
        float yaw = getPreviousYaw(Float.NaN);
        float pitch = getPreviousPitch(Float.NaN);
        return !Float.isNaN(yaw) && !Float.isNaN(pitch)
            ? Vec3.directionFromRotation(pitch, yaw)
            : lookVec;
    }

    private double calculateBound(double coord, double time, double gravity, boolean isMin) {
        double drag = isMin
            ? Math.max(-gravity, (Math.min(-0.05, coord) + Math.min(-0.05, time)) * 1.7)
            : Math.min(gravity, (Math.max(-0.05, coord) + Math.max(-0.05, time)) * 1.7);
        return isMin ? Math.max(-time, drag * 0.99) : Math.min(time, drag * 0.99);
    }

    private Vec3 projectVelocity(Vec3 targetVel, Vec3 lookDir, float pitch) {
        float rad = pitch * DEG_TO_RAD;
        double cosPitch = Math.cos(rad);
        double cosSq = cosPitch * cosPitch;
        double horizontalSpeed = Math.sqrt(targetVel.x * targetVel.x + targetVel.z * targetVel.z);
        double verticalSpeed = Math.abs(targetVel.y) < VEL_THRESHOLD && mc.player.hasEffect(MobEffects.SLOW_FALLING)
            ? Math.min(targetVel.y, 0.01)
            : targetVel.y;

        double gravity = mc.player.getAttributeValue(Attributes.GRAVITY);
        if (verticalSpeed <= 0.0 && mc.player.hasEffect(MobEffects.SLOW_FALLING)) {
            gravity = Math.min(gravity, 0.01);
        }

        double x = targetVel.x;
        double y = verticalSpeed + gravity * (1.0 + cosSq * -0.1);
        double z = targetVel.z;

        if (y < 0.0 && horizontalSpeed > 0.0) {
            double adjust = y * VERTICAL_DAMPING * cosSq;
            x += targetVel.x * adjust / horizontalSpeed;
            y += adjust;
            z += targetVel.z * adjust / horizontalSpeed;
        }

        if (rad < 0.0 && horizontalSpeed > 0.0) {
            double sinPitch = Math.sqrt(targetVel.x * targetVel.x + targetVel.z * targetVel.z) * -Math.sin(rad) * 0.04;
            x += -targetVel.x * sinPitch / horizontalSpeed;
            y += sinPitch * GRAVITY_FACTOR;
            z += -targetVel.z * sinPitch / horizontalSpeed;
        }

        if (horizontalSpeed > 0.0) {
            x += (targetVel.x / horizontalSpeed * Math.sqrt(targetVel.x * targetVel.x + targetVel.z * targetVel.z) - x) * LERP_FACTOR;
            z += (targetVel.z / horizontalSpeed * Math.sqrt(targetVel.x * targetVel.x + targetVel.z * targetVel.z) - z) * LERP_FACTOR;
        }

        return new Vec3(x * DRAG_1, y * DRAG_2, z * DRAG_3);
    }

    private double checkBounds(Vec3 lookVec, Vec3[] lookDirs, float[] pitches, Vec3[] minBounds, Vec3[] maxBounds, Vec3 targetVel, double speed, double distance, double time, boolean isPitch) {
        Vec3 projected = projectVelocity(lookVec, targetVel, (float) (isPitch ? speed : distance));
        double maxViolation = 0.0;

        for (int i = 0; i < lookDirs.length; i++) {
            Vec3 relPos = projectVelocity(projected, lookDirs[i], pitches[i]);
            double violation = isPitch
                ? Math.max(0.0, Math.max(relPos.y - maxBounds[i].y, minBounds[i].y - relPos.y))
                : Math.max(0.0, Math.max(relPos.x - maxBounds[i].x, minBounds[i].x - relPos.x))
                + Math.max(0.0, Math.max(relPos.z - maxBounds[i].z, minBounds[i].z - relPos.z));

            if (violation > maxViolation) {
                maxViolation = violation;
            }
        }

        return maxViolation;
    }

    private double binarySearchPitch(Vec3 lookVec, Vec3[] lookDirs, float[] pitches, Vec3[] minBounds, Vec3[] maxBounds, Vec3 targetVel, double speed, double distance, double gravity, boolean isPitch) {
        double time = isPitch ? distance : speed;
        if (time <= 0.0) {
            return time;
        }

        if (checkBounds(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, speed, distance, time, isPitch) <= 0.0) {
            return time;
        }

        double step = time / MAX_ITERATIONS;
        double bestTime = time;
        double bestViolation = Double.MAX_VALUE;

        for (int i = 1; i <= MAX_ITERATIONS; i++) {
            double testTime = time - step * i;
            double violation = checkBounds(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, speed, distance, testTime, isPitch);
            if (violation < bestViolation) {
                bestViolation = violation;
                bestTime = testTime;
            }
            if (violation <= 0.0) {
                return binarySearchRefine(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, speed, distance, testTime, testTime + step, isPitch);
            }
        }

        return bestTime;
    }

    private double binarySearchRefine(Vec3 lookVec, Vec3[] lookDirs, float[] pitches, Vec3[] minBounds, Vec3[] maxBounds, Vec3 targetVel, double speed, double distance, double minTime, double maxTime, boolean isPitch) {
        double time = isPitch ? distance : speed;
        if (Math.abs(time) < MIN_TIME) {
            return time;
        }

        double step = (maxTime - minTime) / MAX_ITERATIONS;
        double bestTime = time;
        double bestViolation = Double.MAX_VALUE;

        for (int i = 1; i <= 8; i++) {
            double testTime = minTime + step * i;
            double violation = checkBounds(lookVec, lookDirs, pitches, minBounds, maxBounds, targetVel, speed, distance, testTime, isPitch);
            if (violation < bestViolation) {
                bestViolation = violation;
                bestTime = testTime;
            }
            if (violation <= 0.0) {
                return testTime;
            }
        }

        return bestTime;
    }

    private Vec2 getNormalModeSettings() {
        if (mc.player == null) {
            return new Vec2(DEFAULT_YAW, DEFAULT_PITCH);
        }

        float yaw = Mth.wrapDegrees(mc.player.getYRot());
        float pitch = Math.abs(Mth.wrapDegrees(mc.player.getXRot()));
        AttackAura aura = ModuleManager.INSTANCE.method08219();
        ElytraTarget target = ModuleManager.INSTANCE.method08186();
        yaw = normalizeAngle(yaw);
        pitch = normalizeAngle(pitch);
        float yawSetting = getYawSetting(yaw);
        float pitchSetting = getPitchSetting(pitch);
        if (pitchSetting > yawSetting) {
            yawSetting = pitchSetting;
        }

        return new Vec2(yawSetting, pitchSetting);
    }

    private float normalizeAngle(float angle) {
        return Math.abs(angle);
    }

    private float getYawSetting(float yaw) {
        int index = (int) (yaw / YAW_RANGE);
        if (index >= this.yawSettings.length) {
            index = this.yawSettings.length - 1;
        }
        return this.yawSettings[index].method03022();
    }

    private float getPitchSetting(float pitch) {
        int index = (int) (pitch / PITCH_RANGE);
        if (index >= this.pitchSettings.length) {
            index = this.pitchSettings.length - 1;
        }
        return this.pitchSettings[index].method03022();
    }

    private Vec2 getClientModeSettings() {
        LocalPlayer player = mc.player;
        if (player == null) {
            return new Vec2(DEFAULT_YAW, DEFAULT_PITCH);
        }

        boolean isGrim = this.antiCheatMode.method05600("Grim");
        boolean is1_17 = this.version.method05600("1.17.1");
        double grimSpeed1 = isGrim ? (is1_17 ? GRIM_SPEED_1_17 : GRIM_SPEED_1_18_1) : GRIM_SPEED_1_18_2;
        double grimSpeed2 = isGrim ? GRIM_SPEED_1_18_3 : GRIM_SPEED_1_18_2;
        Vec3 lookVec = player.getLookAngle();
        Vec3 predictedDir = getPredictedDirection(Vec3.directionFromRotation(player.xRotO, player.yRotO));
        Vec3 velocity = player.getDeltaMovement();
        Vec3 smoothedVel = getSmoothedVelocity(velocity);
        Vec2 rotation = calculateOptimalRotation(lookVec, smoothedVel, velocity, grimSpeed2, grimSpeed1, GRIM_SPEED_1_18_2);

        if (!isGrim) {
            double yAdjust = grimSpeed1 * velocity.y + lookVec.y * (0.5 + 0.5 * rotation.y);
            double maxSpeed = yAdjust < 0.0 ? grimSpeed2 - 0.45 * -yAdjust : grimSpeed2;
            double time = calculateTimeToTarget(lookVec, velocity, maxSpeed);
            if (time < rotation.x) {
                rotation = calculateOptimalRotation(lookVec, smoothedVel, velocity, time, rotation.y, grimSpeed1);
            }
        }

        return rotation;
    }

    private Vec3 getSmoothedVelocity(Vec3 velocity) {
        if (this.lastDeltaMovement != null && mc.player != null && mc.player.tickCount - this.lastDeltaMovementTick <= 1) {
            return this.lastDeltaMovement;
        }
        return velocity;
    }

    private double calculateTimeToTarget(Vec3 lookVec, Vec3 velocity, double maxSpeed) {
        double xFactor = 0.5 * lookVec.x;
        double zFactor = 0.5 * lookVec.z;
        double horizDistSq = xFactor * xFactor + zFactor * zFactor;
        if (horizDistSq < MIN_SLOWDOWN_THRESHOLD) {
            return MAX_SLOWDOWN_SPEED;
        }

        double xVel = 0.5 * velocity.x + 0.5 * lookVec.x;
        double zVel = 0.5 * velocity.z + 0.5 * lookVec.z;
        double dotProduct = 2.0 * (xFactor * xVel + zFactor * zVel);
        double discriminant = dotProduct * dotProduct - 4.0 * horizDistSq * (xVel * xVel + zVel * zVel - maxSpeed * maxSpeed);
        return discriminant < 0.0 ? -dotProduct / (2.0 * horizDistSq) : (-dotProduct + Math.sqrt(discriminant)) / (2.0 * horizDistSq);
    }

    private float getSlowdownMultiplier() {
        if (!this.speedAdaptation.method05999()) {
            return 1.0F;
        }

        AttackAura aura = ModuleManager.INSTANCE.method08219();
        LivingEntity target = AttackAura.field00785;
        if (mc.player == null || target == null || !aura.method03937()) {
            return 1.0F;
        }

        if (!mc.player.isFallFlying()) {
            return 1.0F;
        }

        Vec3 eyePos = mc.player.getEyePosition();
        Vec3 targetPos = aura.field00788 != null ? aura.field00788 : target.getBoundingBox().getCenter();
        double distance = eyePos.distanceTo(targetPos);
        double maxDistance = this.vectorDistance.method03022().floatValue();
        if (distance >= maxDistance) {
            return 1.0F;
        }

        float strength = this.slowdownStrength.method03022();
        return (float) (1.0 - strength * (1.0 - distance / maxDistance));
    }

    @Subscribe(4)
    private void onPostMovementPacket(PostMovementPacketEvent event) {
        if (mc.player == null) {
            this.lastMovePacketTick = -1;
            this.lastYaw = Float.NaN;
            this.lastPitch = Float.NaN;
            this.currentYaw = Float.NaN;
            this.currentPitch = Float.NaN;
            this.lastDeltaMovement = null;
            this.lastDeltaMovementTick = -1;
        } else {
            this.lastDeltaMovement = mc.player.getDeltaMovement();
            this.lastDeltaMovementTick = mc.player.tickCount;
            this.currentYaw = this.lastYaw;
            this.currentPitch = this.lastPitch;
            this.lastYaw = mc.player.getYRot();
            this.lastPitch = mc.player.getXRot();
            this.lastMovePacketTick = mc.player.tickCount;
        }
    }

    @Subscribe
    private void onFireworkBoost(FireworkBoostEvent event) {
        if (!(event.method04089() instanceof LocalPlayer)) {
            return;
        }

        Vec2 rotation = switch ((String) this.mode.method03022()) {
            case "Custom" -> getNormalModeSettings();
            case "Client" -> getClientModeSettings();
            case "Normal" -> new Vec2(DEFAULT_YAW, DEFAULT_PITCH);
            default -> new Vec2(DEFAULT_YAW, DEFAULT_PITCH);
        };

        float slowdown = getSlowdownMultiplier();
        if (slowdown < 1.0F) {
            rotation = new Vec2(rotation.x * slowdown, rotation.y * slowdown);
        }

        event.method04093(rotation.x);
        event.method04091(rotation.y);
        event.method04090();
        event.method04092();
    }

    @Override
    public void method00245() {
        this.boostFactor = 0.0;
        this.lastTargetVelocity = null;
        super.method00245();
    }
}