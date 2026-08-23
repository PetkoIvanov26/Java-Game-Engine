package org.twingolfpapa.engine.lwjgl;

import org.twingolfpapa.engine.input.ButtonTransition;
import org.twingolfpapa.engine.input.DigitalButton;
import org.twingolfpapa.engine.input.Key;
import org.twingolfpapa.engine.input.MouseButton;

import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;

final class GlfwInputMapper {
    private GlfwInputMapper() {
    }

    static Optional<DigitalButton> mapButton(int glfwKey) {
        return switch (glfwKey) {
            case GLFW_KEY_W -> Optional.of(Key.W);
            case GLFW_KEY_A -> Optional.of(Key.A);
            case GLFW_KEY_S -> Optional.of(Key.S);
            case GLFW_KEY_D -> Optional.of(Key.D);
            case GLFW_KEY_SPACE -> Optional.of(Key.SPACE);
            case GLFW_KEY_ESCAPE -> Optional.of(Key.ESCAPE);
            case GLFW_MOUSE_BUTTON_LEFT -> Optional.of(MouseButton.LEFT);
            case GLFW_MOUSE_BUTTON_RIGHT -> Optional.of(MouseButton.RIGHT);
            case GLFW_MOUSE_BUTTON_MIDDLE -> Optional.of(MouseButton.MIDDLE);
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
