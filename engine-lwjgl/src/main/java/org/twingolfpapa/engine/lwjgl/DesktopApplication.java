package org.twingolfpapa.engine.lwjgl;

import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.core.EngineConfiguration;
import org.twingolfpapa.engine.core.EngineLoop;
import org.twingolfpapa.engine.input.BufferedInput;
import org.twingolfpapa.engine.time.MonotonicClock;

import java.util.Objects;

/** Creates the desktop host and runs an engine client inside it. */
public final class DesktopApplication {
    private DesktopApplication() {
    }

    public static void run(WindowConfiguration windowConfiguration, EngineConfiguration engineConfiguration, EngineClient client) {
        Objects.requireNonNull(windowConfiguration, "windowConfiguration");
        Objects.requireNonNull(engineConfiguration, "engineConfiguration");
        Objects.requireNonNull(client, "client");
        BufferedInput input = new BufferedInput();

        try (GlfwWindow window = GlfwWindow.create(windowConfiguration, input)) {
            EngineLoop engine = new EngineLoop(engineConfiguration, MonotonicClock.system(), window, client, input);
            engine.run();
        }
    }
}
