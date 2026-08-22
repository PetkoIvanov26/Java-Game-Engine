package org.twingolfpapa.engine.time;

import org.twingolfpapa.engine.core.EngineConfiguration;

import java.util.Objects;

/** Converts variable render-frame durations into deterministic fixed simulation steps. */
public final class FixedStepScheduler {
    private final EngineConfiguration configuration;
    private long accumulatorNanos;

    public FixedStepScheduler(EngineConfiguration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration");
    }

    public FramePlan advance(long elapsedNanos) {
        if (elapsedNanos < 0) {
            throw new IllegalArgumentException("elapsedNanos cannot be negative");
        }

        long acceptedFrameNanos = Math.min(elapsedNanos, configuration.maxFrameNanos());
        long discardedFrameNanos = elapsedNanos - acceptedFrameNanos;
        accumulatorNanos = Math.addExact(accumulatorNanos, acceptedFrameNanos);

        long availableUpdates = accumulatorNanos / configuration.fixedStepNanos();
        int fixedUpdates = (int) Math.min(availableUpdates, configuration.maxUpdatesPerFrame());
        accumulatorNanos -= fixedUpdates * configuration.fixedStepNanos();

        long droppedSimulationNanos = 0L;
        if (accumulatorNanos >= configuration.fixedStepNanos()) {
            long remainder = accumulatorNanos % configuration.fixedStepNanos();
            droppedSimulationNanos = accumulatorNanos - remainder;
            accumulatorNanos = remainder;
        }

        double interpolationAlpha = accumulatorNanos / (double) configuration.fixedStepNanos();
        return new FramePlan(fixedUpdates, interpolationAlpha, acceptedFrameNanos,
                discardedFrameNanos, droppedSimulationNanos);
    }

    public void reset() {
        accumulatorNanos = 0L;
    }
}
