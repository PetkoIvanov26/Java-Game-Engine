package org.twingolfpapa.engine.input;

import java.util.List;

public interface Input {
    boolean isKeyDown(Key key);

    boolean wasKeyPressed(Key key);

    boolean wasKeyReleased(Key key);

    List<KeyEvent> keyEvents();
}
