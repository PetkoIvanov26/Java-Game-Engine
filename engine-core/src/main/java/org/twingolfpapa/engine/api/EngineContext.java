package org.twingolfpapa.engine.api;

import org.twingolfpapa.engine.input.Input;

import java.util.Objects;

public record EngineContext(EngineControl control, Input input) {
    public EngineContext {
        Objects.requireNonNull(control, "control");
        Objects.requireNonNull(input, "input");
    }
}
