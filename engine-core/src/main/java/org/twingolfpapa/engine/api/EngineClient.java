package org.twingolfpapa.engine.api;

/**
 * Represents a game or tool that the engine hosts.
 *
 * <p>The engine owns the loop. A client owns its simulation state and rendering commands.</p>
 */
public interface EngineClient {

    default void initialize(EngineContext context) {
    }

    void fixedUpdate(double fixedDeltaSeconds);

    default void render(double interpolationAlpha) {
    }

    default void shutdown() {
    }
}
