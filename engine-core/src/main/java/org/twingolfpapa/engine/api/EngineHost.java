package org.twingolfpapa.engine.api;

/**
 * The operating-system-facing services needed by the core loop.
 *
 * <p>Desktop, headless test, and future server hosts can implement this boundary.</p>
 */
public interface EngineHost {

    void pollEvents();

    boolean shouldClose();

    void present();
}
