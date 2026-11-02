package org.twingolfpapa.engine.lwjgl.opengl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/** Provides temporary bootstrap operations until the project adds a renderer module. */
public final class OpenGlFrame {
    private OpenGlFrame() {
    }

    public static void clear(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
