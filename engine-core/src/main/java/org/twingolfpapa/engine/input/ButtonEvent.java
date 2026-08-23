package org.twingolfpapa.engine.input;

import java.util.Objects;

public record ButtonEvent(DigitalButton button, ButtonTransition transition) {

    public ButtonEvent {
        Objects.requireNonNull(button, "button");
        Objects.requireNonNull(transition, "transition");
    }
}