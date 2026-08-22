package org.twingolfpapa.engine.input;

public interface InputSink {
    void onKeyChanged(Key key, ButtonTransition buttonTransition);
}
