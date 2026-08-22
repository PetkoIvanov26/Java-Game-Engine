package org.twingolfpapa.engine.lwjgl;

import org.twingolfpapa.engine.input.ButtonTransition;
import org.twingolfpapa.engine.input.Key;

import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;

public final class GlfwInputMapper {
    private GlfwInputMapper() {
    }

    static Optional<Key> mapKey(int glfwKey) {
        return switch (glfwKey) {
            case GLFW_KEY_W -> Optional.of(Key.W);
            case GLFW_KEY_A -> Optional.of(Key.A);
            case GLFW_KEY_S -> Optional.of(Key.S);
            case GLFW_KEY_D -> Optional.of(Key.D);
            case GLFW_KEY_SPACE -> Optional.of(Key.SPACE);
            case GLFW_KEY_ESCAPE -> Optional.of(Key.ESCAPE);
            default -> Optional.empty();
        };
    }

    static Optional<ButtonTransition> mapAction(int glfwAction) {
        return switch (glfwAction) {
            case GLFW_PRESS -> Optional.of(ButtonTransition.PRESSED);
            case GLFW_RELEASE -> Optional.of(ButtonTransition.RELEASED);
            default -> Optional.empty();        };
    }
}
