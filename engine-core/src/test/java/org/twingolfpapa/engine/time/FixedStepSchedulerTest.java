package org.twingolfpapa.engine.time;

import org.junit.jupiter.api.Test;
import org.twingolfpapa.engine.core.EngineConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FixedStepSchedulerTest {

    @Test
    void createsFixedUpdatesAndKeepsTheInterpolationRemainder() {
        FixedStepScheduler scheduler = new FixedStepScheduler(configuration(10, 100, 5));

        FramePlan frame = scheduler.advance(35);

        assertEquals(3, frame.fixedUpdates());
        assertEquals(0.5, frame.interpolationAlpha());
        assertEquals(35, frame.acceptedFrameNanos());
        assertEquals(0, frame.discardedFrameNanos());
        assertEquals(0, frame.droppedSimulationNanos());
    }

    @Test
    void carriesPartialTimeIntoTheNextFrame() {
        FixedStepScheduler scheduler = new FixedStepScheduler(configuration(10, 100, 5));

        FramePlan first = scheduler.advance(5);
        FramePlan second = scheduler.advance(5);

        assertEquals(0, first.fixedUpdates());
        assertEquals(0.5, first.interpolationAlpha());
        assertEquals(1, second.fixedUpdates());
        assertEquals(0.0, second.interpolationAlpha());
    }

    @Test
    void clampsAnUnreasonablyLongRenderedFrame() {
        FixedStepScheduler scheduler = new FixedStepScheduler(configuration(10, 25, 5));

        FramePlan frame = scheduler.advance(100);

        assertEquals(2, frame.fixedUpdates());
        assertEquals(0.5, frame.interpolationAlpha());
        assertEquals(25, frame.acceptedFrameNanos());
        assertEquals(75, frame.discardedFrameNanos());
    }

    @Test
    void limitsCatchUpWorkAndReportsDroppedSimulationTime() {
        FixedStepScheduler scheduler = new FixedStepScheduler(configuration(10, 100, 3));

        FramePlan frame = scheduler.advance(85);

        assertEquals(3, frame.fixedUpdates());
        assertEquals(0.5, frame.interpolationAlpha());
        assertEquals(50, frame.droppedSimulationNanos());
    }

    @Test
    void rejectsNegativeElapsedTime() {
        FixedStepScheduler scheduler = new FixedStepScheduler(configuration(10, 100, 5));

        assertThrows(IllegalArgumentException.class, () -> scheduler.advance(-1));
    }

    private static EngineConfiguration configuration(long step, long maxFrame, int maxUpdates) {
        return new EngineConfiguration(step, maxFrame, maxUpdates);
    }
}
