package com.fakefps.simulator;

import com.fakefps.config.FakeFPSConfig;
import net.minecraft.client.MinecraftClient;

import java.util.Random;

/**
 * Generates and smoothly animates fake FPS values.
 * Runs completely on the client rendering cycle without extra background threads.
 */
public class FakeFPSSimulator {
    private static final FakeFPSSimulator INSTANCE = new FakeFPSSimulator();

    private final Random random = new Random();
    private double currentFps = 999.0;
    private double targetFps = 999.0;
    private long lastTargetChangeTime = 0;
    private long lastUpdateTime = 0;

    public static FakeFPSSimulator getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves the FPS value to display based on current configuration and state.
     */
    public int getFps(FakeFPSConfig config, MinecraftClient client) {
        if (config.mode == null) {
            return config.fixedFps;
        }

        switch (config.mode) {
            case REAL:
                return client.getCurrentFps();

            case FIXED:
                return config.fixedFps;

            case RANDOM:
                return updateRandomFps(config);

            default:
                return config.fixedFps;
        }
    }

    /**
     * Updates and returns smooth random FPS within configured min and max range.
     */
    private int updateRandomFps(FakeFPSConfig config) {
        int min = Math.min(config.randomMinFps, config.randomMaxFps);
        int max = Math.max(config.randomMinFps, config.randomMaxFps);

        if (min == max) {
            return min;
        }

        long now = System.currentTimeMillis();

        // Initialize if out of range or first run
        if (currentFps < min || currentFps > max) {
            currentFps = (min + max) / 2.0;
            targetFps = currentFps;
            lastTargetChangeTime = now;
            lastUpdateTime = now;
        }

        // Change target FPS periodically to simulate natural hardware/scene fluctuations
        if (now - lastTargetChangeTime >= config.updateIntervalMs) {
            lastTargetChangeTime = now;

            // Authentic fluctuation: move towards a local sub-range or broader range
            int range = max - min;
            double localVariance = Math.max(10.0, range * 0.25);
            double newTarget = currentFps + (random.nextDouble() * 2.0 - 1.0) * localVariance;

            // Occasionally (15% chance) do a larger shift across the entire allowed range
            if (random.nextDouble() < 0.15) {
                newTarget = min + random.nextDouble() * range;
            }

            targetFps = Math.max(min, Math.min(max, newTarget));
        }

        // Smoothly interpolate towards target
        double deltaSec = (now - lastUpdateTime) / 1000.0;
        lastUpdateTime = now;

        if (deltaSec > 0.0 && deltaSec < 0.5) {
            // Exponential smoothing based on time delta
            double speed = Math.max(1.0, config.smoothingSpeed * 25.0);
            double blend = 1.0 - Math.exp(-speed * deltaSec);
            currentFps += (targetFps - currentFps) * blend;

            // Micro-jitter to give authentic frame-to-frame realism (± 1 frame)
            double jitter = (random.nextDouble() - 0.5) * 1.2;
            currentFps += jitter;
        }

        // Clamp to strictly stay within configured bounds
        currentFps = Math.max(min, Math.min(max, currentFps));

        return (int) Math.round(currentFps);
    }

    /**
     * Resets simulator state.
     */
    public void reset(int baseFps) {
        this.currentFps = baseFps;
        this.targetFps = baseFps;
        this.lastTargetChangeTime = System.currentTimeMillis();
        this.lastUpdateTime = System.currentTimeMillis();
    }
}
