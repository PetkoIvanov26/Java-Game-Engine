package org.twingolfpapa.sandbox;

import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.api.EngineContext;
import org.twingolfpapa.engine.core.EngineConfiguration;
import org.twingolfpapa.engine.input.*;
import org.twingolfpapa.engine.lwjgl.DesktopApplication;
import org.twingolfpapa.engine.lwjgl.WindowConfiguration;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlFrame;

/** Neutral playground used to exercise engine features without embedding a game in the engine. */
public final class SandboxApplication implements EngineClient {

    private EngineContext context;
    private boolean wHeld;

     static void main(String[] arguments) {
        DesktopApplication.run(
                WindowConfiguration.windowed(1280, 720, "Project Engine Sandbox"),
                EngineConfiguration.atFixedRate(60),
                new SandboxApplication()
        );
    }

    @Override
    public void initialize(EngineContext context) {
        this.context = context;
    }

    @Override
    public void fixedUpdate(double fixedDeltaSeconds) {
         if (context.input().wasButtonPressed(Key.ESCAPE)) {
             context.control().requestStop();
         }

         wHeld = context.input().isButtonDown(MouseButton.LEFT);
    }

    @Override
    public void render(double interpolationAlpha) {
        if (wHeld) {
            OpenGlFrame.clear(0.10f, 0.35f, 0.15f, 1.0f);
        } else {
            OpenGlFrame.clear(0.055f, 0.065f, 0.085f, 1.0f);
        }
    }
}
