package org.twingolfpapa.engine.api;

/** Controls the lifetime of the currently running engine loop. */
public interface EngineControl {

    void requestStop();

    boolean isStopRequested();
}
