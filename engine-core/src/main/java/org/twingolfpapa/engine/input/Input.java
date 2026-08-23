package org.twingolfpapa.engine.input;

import java.util.List;

public interface Input {
    boolean isButtonDown(DigitalButton button);

    boolean wasButtonPressed(DigitalButton button);

    boolean wasButtonReleased(DigitalButton button);

    List<ButtonEvent> buttonEvents();

    CursorState cursor();

    ScrollDelta scrollDelta();
}
