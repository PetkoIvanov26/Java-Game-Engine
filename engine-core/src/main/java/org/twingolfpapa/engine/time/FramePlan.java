package org.twingolfpapa.engine.time;

/** Contains the fixed updates and interpolation state for one rendered frame. */
public record FramePlan(int fixedUpdates, double interpolationAlpha, long acceptedFrameNanos, long discardedFrameNanos,
                        long droppedSimulationNanos) {
}
