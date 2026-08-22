package org.twingolfpapa.engine.input;

import java.util.Objects;

public record KeyEvent(Key key, ButtonTransition transition) {
    public KeyEvent {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(transition, "transition");
    }
}
