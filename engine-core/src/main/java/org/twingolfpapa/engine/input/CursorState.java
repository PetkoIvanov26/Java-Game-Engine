package org.twingolfpapa.engine.input;

public record CursorState(double x, double y, double deltaX, double deltaY) {
    public static final CursorState ZERO = new CursorState(0.0, 0.0, 0.0, 0.0);
}
