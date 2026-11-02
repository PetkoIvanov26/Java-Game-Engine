package org.twingolfpapa.engine.api;

import org.twingolfpapa.engine.graphics.RenderSurface;
import org.twingolfpapa.engine.input.Input;

import java.util.Objects;

public record EngineContext(EngineControl control, Input input, RenderSurface renderSurface) {
    public EngineContext {
        Objects.requireNonNull(control, "control");
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(renderSurface, "render surface");
    }
}
