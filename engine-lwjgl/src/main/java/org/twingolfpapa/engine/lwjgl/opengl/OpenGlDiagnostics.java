package org.twingolfpapa.engine.lwjgl.opengl;

import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import java.io.PrintStream;
import java.util.Objects;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class OpenGlDiagnostics implements AutoCloseable {
    private final Callback debugCallback;
    private boolean closed;

    private OpenGlDiagnostics(Callback debugCallback) {
        this.debugCallback = debugCallback;
    }

    public static OpenGlDiagnostics install(PrintStream output) {
        Objects.requireNonNull(output, "output");

        output.println("OpenGL vendor: " + glGetString(GL_VENDOR));
        output.println("OpenGL renderer: " + glGetString(GL_RENDERER));
        output.println("OpenGL version: " + glGetString(GL_VERSION));
        output.println("GLSL version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));

        Callback debugCallback = GLUtil.setupDebugMessageCallback(output);

        if (Objects.isNull(debugCallback)) {
            output.println("OpenGL debug output is not supported");
        }

        return new OpenGlDiagnostics(debugCallback);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;

        if (Objects.nonNull(debugCallback)) {
            debugCallback.free();
        }
    }
}
