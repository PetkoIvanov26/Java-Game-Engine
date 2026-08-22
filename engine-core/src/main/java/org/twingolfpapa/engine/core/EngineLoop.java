package org.twingolfpapa.engine.core;

import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.api.EngineContext;
import org.twingolfpapa.engine.api.EngineControl;
import org.twingolfpapa.engine.api.EngineHost;
import org.twingolfpapa.engine.input.BufferedInput;
import org.twingolfpapa.engine.time.FixedStepScheduler;
import org.twingolfpapa.engine.time.FramePlan;
import org.twingolfpapa.engine.time.MonotonicClock;

import java.util.Objects;

/** Coordinates event polling, fixed simulation updates, rendering, and presentation. */
public final class EngineLoop implements EngineControl {
    private final EngineConfiguration configuration;
    private final MonotonicClock clock;
    private final EngineHost host;
    private final EngineClient client;
    private final FixedStepScheduler scheduler;
    private final BufferedInput input;

    private boolean running;
    private boolean stopRequested;

    public EngineLoop(EngineConfiguration configuration, MonotonicClock clock, EngineHost host,
                      EngineClient client, BufferedInput input) {
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.host = Objects.requireNonNull(host, "host");
        this.client = Objects.requireNonNull(client, "client");
        this.input = Objects.requireNonNull(input, "input");
        this.scheduler = new FixedStepScheduler(configuration);
    }

    public void run() {
        if (running) {
            throw new IllegalStateException("Engine loop is already running");
        }

        running = true;
        stopRequested = false;
        scheduler.reset();

        try {
            client.initialize(new EngineContext(this, input));
            long previousNanos = clock.nowNanos();

            while (!stopRequested && !host.shouldClose()) {
                host.pollEvents();

                long currentNanos = clock.nowNanos();
                long elapsedNanos = Math.max(0L, currentNanos - previousNanos);
                previousNanos = currentNanos;

                FramePlan frame = scheduler.advance(elapsedNanos);
                for (int update = 0; update < frame.fixedUpdates() && !stopRequested; update++) {
                    input.beginFixedUpdate();
                    client.fixedUpdate(configuration.fixedDeltaSeconds());
                }

                if (!stopRequested) {
                    client.render(frame.interpolationAlpha());
                    host.present();
                }
            }
        } finally {
            try {
                client.shutdown();
            } finally {
                running = false;
            }
        }
    }

    @Override
    public void requestStop() {
        stopRequested = true;
    }

    @Override
    public boolean isStopRequested() {
        return stopRequested;
    }
}
