package org.twingolfpapa.engine.input;

import java.util.*;

public final class BufferedInput implements Input, InputSink {
    private final Set<DigitalButton> tickDownButtons = new HashSet<>();
    private List<ButtonEvent> tickButtonEvents = List.of();
    private final Set<DigitalButton> observedDownButtons = new HashSet<>();
    private final List<ButtonEvent> pendingButtonEvents = new ArrayList<>();

    private boolean cursorObserved;
    private double observedCursorX;
    private double observedCursorY;
    private double pendingCursorDeltaX;
    private double pendingCursorDeltaY;
    private CursorState tickCursor = CursorState.ZERO;

    private double pendingScrollX;
    private double pendingScrollY;

    private ScrollDelta tickScrollDelta = ScrollDelta.ZERO;

    @Override
    public boolean isButtonDown(DigitalButton key) {
        Objects.requireNonNull(key, "key");
        return tickDownButtons.contains(key);
    }

    @Override
    public boolean wasButtonPressed(DigitalButton key) {
        Objects.requireNonNull(key, "key");
        return containsTransition(key, ButtonTransition.PRESSED);
    }

    @Override
    public boolean wasButtonReleased(DigitalButton key) {
        Objects.requireNonNull(key, "key");
        return containsTransition(key, ButtonTransition.RELEASED);
    }

    @Override
    public List<ButtonEvent> buttonEvents() {
        return tickButtonEvents;
    }

    @Override
    public CursorState cursor() {
        return tickCursor;
    }

    @Override
    public ScrollDelta scrollDelta() {
        return tickScrollDelta;
    }

    @Override
    public void onButtonChanged(DigitalButton key, ButtonTransition transition) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(transition, "transition");

        boolean stateChanged = switch (transition) {
            case PRESSED -> observedDownButtons.add(key);
            case RELEASED -> observedDownButtons.remove(key);
        };

        if (stateChanged) {
            pendingButtonEvents.add(new ButtonEvent(key, transition));
        }
    }

    @Override
    public void onCursorMoved(double x, double y) {
        if (!cursorObserved) {
            observedCursorX = x;
            observedCursorY = y;
            cursorObserved = true;
            return;
        }

        pendingCursorDeltaX += x - observedCursorX;
        pendingCursorDeltaY += y - observedCursorY;
        observedCursorX = x;
        observedCursorY = y;
    }

    @Override
    public void onScrolled(double xOffset, double yOffset) {
        pendingScrollX += xOffset;
        pendingScrollY += yOffset;
    }

    @Override
    public void onCursorTrackingInterrupted() {
        cursorObserved = false;
        pendingCursorDeltaX = 0.0;
        pendingCursorDeltaY = 0.0;
    }

    public void beginFixedUpdate() {
        tickDownButtons.clear();
        tickDownButtons.addAll(observedDownButtons);

        tickButtonEvents = List.copyOf(pendingButtonEvents);
        pendingButtonEvents.clear();

        tickCursor = new CursorState(observedCursorX, observedCursorY, pendingCursorDeltaX, pendingCursorDeltaY);

        pendingCursorDeltaX = 0;
        pendingCursorDeltaY = 0;

        tickScrollDelta = new ScrollDelta(pendingScrollX, pendingScrollY);
        pendingScrollX = 0.0;
        pendingScrollY = 0.0;
    }

    private boolean containsTransition(DigitalButton key, ButtonTransition transition) {
        Objects.requireNonNull(key, "key");

        for (ButtonEvent event : tickButtonEvents) {
            if (event.button().equals(key) && event.transition() == transition) {
                return true;
            }
        }

        return false;
    }
}
