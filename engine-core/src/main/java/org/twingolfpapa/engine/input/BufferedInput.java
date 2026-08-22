package org.twingolfpapa.engine.input;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public final class BufferedInput implements Input, InputSink {
    private final EnumSet<Key> tickDownKeys = EnumSet.noneOf(Key.class);
    private List<KeyEvent> tickKeyEvents = List.of();
    private final EnumSet<Key> observedDownKeys = EnumSet.noneOf(Key.class);
    private final List<KeyEvent> pendingKeyEvents = new ArrayList<>();

    @Override
    public boolean isKeyDown(Key key) {
        Objects.requireNonNull(key, "key");
        return tickDownKeys.contains(key);
    }

    @Override
    public boolean wasKeyPressed(Key key) {
        Objects.requireNonNull(key, "key");
        return containsTransition(key, ButtonTransition.PRESSED);
    }

    @Override
    public boolean wasKeyReleased(Key key) {
        Objects.requireNonNull(key, "key");
        return containsTransition(key, ButtonTransition.RELEASED);
    }

    @Override
    public List<KeyEvent> keyEvents() {
        return tickKeyEvents;
    }

    private boolean containsTransition(Key key, ButtonTransition transition) {
        Objects.requireNonNull(key, "key");

        for (KeyEvent event : tickKeyEvents) {
            if (event.key() == key && event.transition() == transition) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onKeyChanged(Key key, ButtonTransition transition) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(transition, "transition");

        boolean stateChanged = switch (transition) {
            case PRESSED -> observedDownKeys.add(key);
            case RELEASED -> observedDownKeys.remove(key);
        };

        if (stateChanged) {
            pendingKeyEvents.add(new KeyEvent(key, transition));
        }
    }

    public void beginFixedUpdate() {
        tickDownKeys.clear();
        tickDownKeys.addAll(observedDownKeys);

        tickKeyEvents = List.copyOf(pendingKeyEvents);
        pendingKeyEvents.clear();
    }
}
