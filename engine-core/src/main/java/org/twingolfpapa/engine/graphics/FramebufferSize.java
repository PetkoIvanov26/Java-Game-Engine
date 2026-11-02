package org.twingolfpapa.engine.graphics;

public record FramebufferSize(int width, int height) {

    public FramebufferSize {
        if (width < 0) {
            throw new IllegalArgumentException("Framebuffer width cannot be negative");
        }

        if (height < 0) {
            throw new IllegalArgumentException("Framebuffer height cannot be negative");
        }
    }

    public boolean hasArea() {
        return width > 0 && height > 0;
    }
}
