package org.twingolfpapa.engine.lwjgl;

import java.util.Objects;

/** Immutable configuration for the initial desktop window. */
public record WindowConfiguration(int width, int height, String title, boolean resizable, boolean verticalSync) {
    public WindowConfiguration {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Window dimensions must be positive");
        }
        Objects.requireNonNull(title, "title");
        if (title.isBlank()) {
            throw new IllegalArgumentException("Window title cannot be blank");
        }
    }

    public static WindowConfiguration windowed(int width, int height, String title) {
        return new WindowConfiguration(width, height, title, true, true);
    }
}
