package org.twingolfpapa.engine.core;

import org.junit.jupiter.api.Test;
import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.api.EngineContext;
import org.twingolfpapa.engine.api.EngineControl;
import org.twingolfpapa.engine.api.EngineHost;
import org.twingolfpapa.engine.input.BufferedInput;
import org.twingolfpapa.engine.input.ButtonTransition;
import org.twingolfpapa.engine.input.Key;
import org.twingolfpapa.engine.time.MonotonicClock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EngineLoopTest {

    @Test
    void runsFixedUpdatesThenRendersAndShutsDownExactlyOnce() {
        OneFrameHost host = new OneFrameHost();
        RecordingClient client = new RecordingClient();
        BufferedInput input = new BufferedInput();
        MonotonicClock clock = new SequenceClock(0, 35);
        EngineConfiguration configuration = new EngineConfiguration(10, 100, 5);

        EngineLoop engine = new EngineLoop(configuration, clock, host, client, input);
        engine.run();

        assertNotNull(client.context.control());
        assertEquals(3, client.fixedUpdates);
        assertEquals(0.5, client.lastAlpha);
        assertEquals(1, client.renderCalls);
        assertEquals(1, client.shutdownCalls);
        assertEquals(1, host.presentCalls);
        assertSame(engine, client.context.control());
        assertSame(input, client.context.input());
    }

    @Test
    void aClientCanStopTheLoopDuringSimulation() {
        OneFrameHost host = new OneFrameHost();
        RecordingClient client = new RecordingClient() {
            @Override
            public void fixedUpdate(double fixedDeltaSeconds) {
                super.fixedUpdate(fixedDeltaSeconds);
                requestStop();
            }
        };

        new EngineLoop(new EngineConfiguration(10, 100, 5),
                new SequenceClock(0, 35), host, client, new BufferedInput())
                .run();

        assertEquals(1, client.fixedUpdates);
        assertEquals(0, client.renderCalls);
        assertEquals(0, host.presentCalls);
        assertEquals(1, client.shutdownCalls);
        assertTrue(client.context.control().isStopRequested());
    }

    @Test
    void publishesInputOnceAcrossCatchUpUpdates() {
        BufferedInput input = new BufferedInput();

        List<Boolean> pressedDuringUpdates = new ArrayList<>();
        List<Boolean> heldDuringUpdates = new ArrayList<>();

        EngineClient client = fixedDeltaSeconds -> {
            pressedDuringUpdates.add(input.wasKeyPressed(Key.W));
            heldDuringUpdates.add(input.isKeyDown(Key.W));
        };

        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);

        new EngineLoop(new EngineConfiguration(10, 100, 5),
                new SequenceClock(0, 35), new OneFrameHost(), client, input)
                .run();

        assertEquals(List.of(true, false, false), pressedDuringUpdates);

        assertEquals(List.of(true, true, true), heldDuringUpdates);
    }

    @Test
    void pendingInputNotPublishedBeforeBeginFixedUpdate() {
        MultiFrameHost host = new MultiFrameHost(2);
        BufferedInput input = new BufferedInput();

        List<Boolean> pressedDuringUpdates = new ArrayList<>();
        List<Boolean> heldDuringUpdates = new ArrayList<>();

        EngineClient client = fixedDeltaSeconds -> {
            pressedDuringUpdates.add(input.wasKeyPressed(Key.W));
            heldDuringUpdates.add(input.isKeyDown(Key.W));
        };

        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);

        new EngineLoop(new EngineConfiguration(10, 100, 5),
                new SequenceClock(0, 5, 10), host, client, input)
                .run();

        assertEquals(List.of(true), pressedDuringUpdates);

        assertEquals(List.of(true), heldDuringUpdates);
        assertEquals(2, host.presentCalls);

    }

    private static class RecordingClient implements EngineClient {
        private EngineContext context;
        private int fixedUpdates;
        private int renderCalls;
        private int shutdownCalls;
        private double lastAlpha;

        @Override
        public void initialize(EngineContext context) {
            this.context = context;
        }

        @Override
        public void fixedUpdate(double fixedDeltaSeconds) {
            fixedUpdates++;
        }

        @Override
        public void render(double interpolationAlpha) {
            renderCalls++;
            lastAlpha = interpolationAlpha;
        }

        @Override
        public void shutdown() {
            shutdownCalls++;
        }

        protected final void requestStop() {
            context.control().requestStop();
        }
    }

    private static final class OneFrameHost implements EngineHost {
        private int presentCalls;

        @Override
        public void pollEvents() {
        }

        @Override
        public boolean shouldClose() {
            return presentCalls >= 1;
        }

        @Override
        public void present() {
            presentCalls++;
        }
    }

    private static final class MultiFrameHost implements EngineHost {
        private int presentCalls;
        private final int numberOfFrames;

        public MultiFrameHost(int numberOfFrames) {
            this.numberOfFrames = numberOfFrames;
        }

        @Override
        public void pollEvents() {
        }

        @Override
        public boolean shouldClose() {
            return presentCalls >= numberOfFrames;
        }

        @Override
        public void present() {
            presentCalls++;
        }
    }

    private static final class SequenceClock implements MonotonicClock {
        private final long[] readings;
        private int index;

        private SequenceClock(long... readings) {
            this.readings = readings.clone();
        }

        @Override
        public long nowNanos() {
            return readings[Math.min(index++, readings.length - 1)];
        }
    }
}
