package org.twingolfpapa.engine.api;

/**
 * A game or tool hosted by the engine.
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
