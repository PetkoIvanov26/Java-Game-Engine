package org.twingolfpapa.engine.lwjgl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.twingolfpapa.engine.api.EngineHost;
import org.twingolfpapa.engine.input.*;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Optional;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

/** A single-window GLFW host for the desktop engine loop. */
public final class GlfwWindow implements EngineHost, AutoCloseable {
    private final long handle;
    private boolean closed;

    private GlfwWindow(long handle) {
        this.handle = handle;
    }

    public static GlfwWindow create(WindowConfiguration configuration, InputSink inputSink) {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(inputSink, "inputSink");
        GLFWErrorCallback.createPrint(System.err).set();
        GlfwWindow glfwWindow = null;

        if (!glfwInit()) {
            clearErrorCallback();
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        long window = NULL;
        try {
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, configuration.resizable() ? GLFW_TRUE : GLFW_FALSE);

            window = glfwCreateWindow(configuration.width(), configuration.height(), configuration.title(),
                    NULL, NULL);
            if (window == NULL) {
                throw new IllegalStateException("Unable to create the GLFW window");
            }

            glfwMakeContextCurrent(window);
            glfwSwapInterval(configuration.verticalSync() ? 1 : 0);
            GL.createCapabilities();

            updateViewport(window);
            glfwSetFramebufferSizeCallback(window, (ignored, width, height) ->
                    glViewport(0, 0, Math.max(width, 0), Math.max(height, 0))
            );
            glfwShowWindow(window);

            glfwWindow = new GlfwWindow(window);

            glfwSetKeyCallback(window, (ignoredWindow, glfwKey, scanCode, glfwAction, modifiers) -> {
                        Optional<DigitalButton> key = GlfwInputMapper.mapButton(glfwKey);
                        Optional<ButtonTransition> transition =
                                GlfwInputMapper.mapAction(glfwAction);

                        if (key.isPresent() && transition.isPresent()) {
                            inputSink.onButtonChanged(key.get(), transition.get());
                        }
                    }
            );

            glfwSetMouseButtonCallback(window, (ignoredWindow, glfwButton, glfwAction, modifiers) -> {
                        Optional<DigitalButton> button = GlfwInputMapper.mapButton(glfwButton);

                        Optional<ButtonTransition> transition = GlfwInputMapper.mapAction(glfwAction);

                        if (button.isPresent() && transition.isPresent()) {
                            inputSink.onButtonChanged(button.get(), transition.get());
                        }
                    }
            );

            glfwSetCursorPosCallback(window, (ignoredWindow, x, y) ->
                    inputSink.onCursorMoved(x, y)
            );

            glfwSetScrollCallback(window, (ignoredWindow, xOffset, yOffset) ->
                    inputSink.onScrolled(xOffset, yOffset)
            );

            glfwSetWindowFocusCallback(window, (ignoredWindow, focused) -> {
                        if (!focused) {
                            inputSink.onCursorTrackingInterrupted();
                        }
                    }
            );
        } catch (RuntimeException exception) {
            if (window != NULL) {
                glfwFreeCallbacks(window);
                glfwDestroyWindow(window);
            }
            glfwTerminate();
            clearErrorCallback();
            throw exception;
        }

        return glfwWindow;
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    @Override
    public void present() {
        glfwSwapBuffers(handle);
    }

    public long handle() {
        return handle;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;

        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        clearErrorCallback();
    }

    private static void updateViewport(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(window, width, height);
            glViewport(0, 0, width.get(0), height.get(0));
        }
    }

    private static void clearErrorCallback() {
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }
}
