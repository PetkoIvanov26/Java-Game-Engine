package org.twingolfpapa.engine.api;

import org.twingolfpapa.engine.graphics.RenderSurface;

/**
 * Provides operating-system services to the core loop.
 *
 * <p>Desktop, headless test, and future server hosts can implement this boundary.</p>
 */
public interface EngineHost extends RenderSurface {

    void pollEvents();

    boolean shouldClose();

    void present();
}
