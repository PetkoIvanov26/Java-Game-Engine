package org.twingolfpapa.engine.input;

public interface InputSink {
    void onButtonChanged(DigitalButton button, ButtonTransition transition);
    void onCursorMoved(double x, double y);
    void onScrolled(double xOffset, double yOffset);
    void onCursorTrackingInterrupted();
}
