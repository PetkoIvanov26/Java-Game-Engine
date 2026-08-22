package org.twingolfpapa.engine.core;

import java.time.Duration;
import java.util.Objects;

/** Immutable timing policy for an engine loop. */
public record EngineConfiguration(long fixedStepNanos, long maxFrameNanos, int maxUpdatesPerFrame) {
    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    public EngineConfiguration {
        if (fixedStepNanos <= 0) {
            throw new IllegalArgumentException("fixedStepNanos must be positive");
        }
        if (maxFrameNanos <= 0) {
            throw new IllegalArgumentException("maxFrameNanos must be positive");
        }
        if (maxUpdatesPerFrame <= 0) {
            throw new IllegalArgumentException("maxUpdatesPerFrame must be positive");
        }
    }

    public static EngineConfiguration atFixedRate(int updatesPerSecond) {
        return atFixedRate(updatesPerSecond, Duration.ofMillis(250), 5);
    }

    public static EngineConfiguration atFixedRate(int updatesPerSecond, Duration maxFrameTime, int maxUpdatesPerFrame) {
        if (updatesPerSecond <= 0) {
            throw new IllegalArgumentException("updatesPerSecond must be positive");
        }
        Objects.requireNonNull(maxFrameTime, "maxFrameTime");

        long fixedStepNanos = Math.round((double) NANOS_PER_SECOND / updatesPerSecond);
        return new EngineConfiguration(fixedStepNanos, maxFrameTime.toNanos(), maxUpdatesPerFrame);
    }

    public double fixedDeltaSeconds() {
        return fixedStepNanos / (double) NANOS_PER_SECOND;
    }
}
