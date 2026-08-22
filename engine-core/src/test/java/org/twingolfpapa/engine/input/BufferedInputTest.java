package org.twingolfpapa.engine.input;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BufferedInputTest {

    @Test
    void startsWithNoHeldKeysOrEvents() {
        BufferedInput input = new BufferedInput();

        for (Key key : Key.values()) {
            assertFalse(input.isKeyDown(key));
            assertFalse(input.wasKeyPressed(key));
            assertFalse(input.wasKeyReleased(key));
        }

        assertTrue(input.keyEvents().isEmpty());
    }

    @Test
    void publishesPressOnlyWhenFixedUpdateBegins() {
        BufferedInput input = new BufferedInput();

        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);

        // GLFW has reported the press, but simulation has not advanced.
        assertFalse(input.isKeyDown(Key.W));
        assertFalse(input.wasKeyPressed(Key.W));
        assertTrue(input.keyEvents().isEmpty());

        input.beginFixedUpdate();

        // The press is now visible to this simulation tick.
        assertTrue(input.isKeyDown(Key.W));
        assertTrue(input.wasKeyPressed(Key.W));
        assertFalse(input.wasKeyReleased(Key.W));

        assertEquals(
                List.of(new KeyEvent(Key.W, ButtonTransition.PRESSED)),
                input.keyEvents()
        );
    }

    @Test
    void doesNotRepeatPressOnFollowingFixedUpdate() {
        BufferedInput input = new BufferedInput();

        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertTrue(input.wasKeyPressed(Key.W));

        input.beginFixedUpdate();

        assertTrue(input.isKeyDown(Key.W));
        assertFalse(input.wasKeyPressed(Key.W));
        assertTrue(input.keyEvents().isEmpty());
    }

    @Test
    void publishesReleaseOnNextUpdate() {
        BufferedInput input = new BufferedInput();
        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();
        assertTrue(input.isKeyDown(Key.W));

        input.onKeyChanged(Key.W, ButtonTransition.RELEASED);
        assertTrue(input.isKeyDown(Key.W));
        assertFalse(input.wasKeyReleased(Key.W));

        input.beginFixedUpdate();

        assertFalse(input.isKeyDown(Key.W));
        assertFalse(input.wasKeyPressed(Key.W));
        assertTrue(input.wasKeyReleased(Key.W));
        assertEquals(List.of(new KeyEvent(Key.W, ButtonTransition.RELEASED)), input.keyEvents());
    }

    @Test
    void pressAndReleaseBetweenTicks() {
        BufferedInput input = new BufferedInput();
        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);
        input.onKeyChanged(Key.W, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertFalse(input.isKeyDown(Key.W));
        assertTrue(input.wasKeyPressed(Key.W));
        assertTrue(input.wasKeyReleased(Key.W));
        assertEquals(List.of(new KeyEvent(Key.W, ButtonTransition.PRESSED), new KeyEvent(Key.W, ButtonTransition.RELEASED)),
                input.keyEvents()
        );
    }

    @Test
    void ignoresSignalsThatDoNotChangeState() {
        BufferedInput input = new BufferedInput();

        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);
        input.onKeyChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertEquals(List.of(new KeyEvent(Key.W, ButtonTransition.PRESSED)), input.keyEvents());

        input.onKeyChanged(Key.W, ButtonTransition.RELEASED);
        input.onKeyChanged(Key.W, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertEquals(List.of(new KeyEvent(Key.W, ButtonTransition.RELEASED)), input.keyEvents());
    }
}
