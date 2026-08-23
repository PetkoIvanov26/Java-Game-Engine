package org.twingolfpapa.engine.input;

public record ScrollDelta(double x, double y) {
    public static final ScrollDelta ZERO = new ScrollDelta(0.0, 0.0);
}