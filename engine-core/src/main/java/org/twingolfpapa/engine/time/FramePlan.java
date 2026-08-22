package org.twingolfpapa.engine.time;

/** The fixed updates and interpolation state produced for one rendered frame. */
public record FramePlan(int fixedUpdates, double interpolationAlpha, long acceptedFrameNanos, long discardedFrameNanos,
                        long droppedSimulationNanos) {
}
