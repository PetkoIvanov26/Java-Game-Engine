package org.twingolfpapa.engine.input;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BufferedInputTest {

    @Test
    void startsWithNoHeldKeysOrEvents() {
        BufferedInput input = new BufferedInput();

        for (Key key : Key.values()) {
            assertFalse(input.isButtonDown(key));
            assertFalse(input.wasButtonPressed(key));
            assertFalse(input.wasButtonReleased(key));
        }

        assertTrue(input.buttonEvents().isEmpty());
    }

    @Test
    void publishesPressOnlyWhenFixedUpdateBegins() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);

        // GLFW has reported the press, but simulation has not advanced.
        assertFalse(input.isButtonDown(Key.W));
        assertFalse(input.wasButtonPressed(Key.W));
        assertTrue(input.buttonEvents().isEmpty());

        input.beginFixedUpdate();

        // The press is now visible to this simulation tick.
        assertTrue(input.isButtonDown(Key.W));
        assertTrue(input.wasButtonPressed(Key.W));
        assertFalse(input.wasButtonReleased(Key.W));

        assertEquals(
                List.of(new ButtonEvent(Key.W, ButtonTransition.PRESSED)),
                input.buttonEvents()
        );
    }

    @Test
    void doesNotRepeatPressOnFollowingFixedUpdate() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertTrue(input.wasButtonPressed(Key.W));

        input.beginFixedUpdate();

        assertTrue(input.isButtonDown(Key.W));
        assertFalse(input.wasButtonPressed(Key.W));
        assertTrue(input.buttonEvents().isEmpty());
    }

    @Test
    void publishesReleaseOnNextUpdate() {
        BufferedInput input = new BufferedInput();
        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();
        assertTrue(input.isButtonDown(Key.W));

        input.onButtonChanged(Key.W, ButtonTransition.RELEASED);
        assertTrue(input.isButtonDown(Key.W));
        assertFalse(input.wasButtonReleased(Key.W));

        input.beginFixedUpdate();

        assertFalse(input.isButtonDown(Key.W));
        assertFalse(input.wasButtonPressed(Key.W));
        assertTrue(input.wasButtonReleased(Key.W));
        assertEquals(List.of(new ButtonEvent(Key.W, ButtonTransition.RELEASED)), input.buttonEvents());
    }

    @Test
    void pressAndReleaseBetweenTicks() {
        BufferedInput input = new BufferedInput();
        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);
        input.onButtonChanged(Key.W, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertFalse(input.isButtonDown(Key.W));
        assertTrue(input.wasButtonPressed(Key.W));
        assertTrue(input.wasButtonReleased(Key.W));
        assertEquals(List.of(new ButtonEvent(Key.W, ButtonTransition.PRESSED), new ButtonEvent(Key.W, ButtonTransition.RELEASED)),
                input.buttonEvents()
        );
    }

    @Test
    void ignoresSignalsThatDoNotChangeState() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);
        input.onButtonChanged(Key.W, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertEquals(List.of(new ButtonEvent(Key.W, ButtonTransition.PRESSED)), input.buttonEvents());

        input.onButtonChanged(Key.W, ButtonTransition.RELEASED);
        input.onButtonChanged(Key.W, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertEquals(List.of(new ButtonEvent(Key.W, ButtonTransition.RELEASED)), input.buttonEvents());
    }

    @Test
    void publishesPressOnlyWhenFixedUpdateBegins_Mouse() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);

        // GLFW has reported the press, but simulation has not advanced.
        assertFalse(input.isButtonDown(MouseButton.LEFT));
        assertFalse(input.wasButtonPressed(MouseButton.LEFT));
        assertTrue(input.buttonEvents().isEmpty());

        input.beginFixedUpdate();

        // The press is now visible to this simulation tick.
        assertTrue(input.isButtonDown(MouseButton.LEFT));
        assertTrue(input.wasButtonPressed(MouseButton.LEFT));
        assertFalse(input.wasButtonReleased(MouseButton.LEFT));

        assertEquals(
                List.of(new ButtonEvent(MouseButton.LEFT, ButtonTransition.PRESSED)),
                input.buttonEvents()
        );
    }

    @Test
    void doesNotRepeatPressOnFollowingFixedUpdate_Mouse() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertTrue(input.wasButtonPressed(MouseButton.LEFT));

        input.beginFixedUpdate();

        assertTrue(input.isButtonDown(MouseButton.LEFT));
        assertFalse(input.wasButtonPressed(MouseButton.LEFT));
        assertTrue(input.buttonEvents().isEmpty());
    }

    @Test
    void publishesReleaseOnNextUpdate_Mouse() {
        BufferedInput input = new BufferedInput();
        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);
        input.beginFixedUpdate();
        assertTrue(input.isButtonDown(MouseButton.LEFT));

        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.RELEASED);
        assertTrue(input.isButtonDown(MouseButton.LEFT));
        assertFalse(input.wasButtonReleased(MouseButton.LEFT));

        input.beginFixedUpdate();

        assertFalse(input.isButtonDown(MouseButton.LEFT));
        assertFalse(input.wasButtonPressed(MouseButton.LEFT));
        assertTrue(input.wasButtonReleased(MouseButton.LEFT));
        assertEquals(List.of(new ButtonEvent(MouseButton.LEFT, ButtonTransition.RELEASED)), input.buttonEvents());
    }

    @Test
    void pressAndReleaseBetweenTicks_Mouse() {
        BufferedInput input = new BufferedInput();
        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);
        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertFalse(input.isButtonDown(MouseButton.LEFT));
        assertTrue(input.wasButtonPressed(MouseButton.LEFT));
        assertTrue(input.wasButtonReleased(MouseButton.LEFT));
        assertEquals(List.of(new ButtonEvent(MouseButton.LEFT, ButtonTransition.PRESSED), new ButtonEvent(MouseButton.LEFT, ButtonTransition.RELEASED)),
                input.buttonEvents()
        );
    }

    @Test
    void ignoresSignalsThatDoNotChangeState_Mouse() {
        BufferedInput input = new BufferedInput();

        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);
        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.PRESSED);
        input.beginFixedUpdate();

        assertEquals(List.of(new ButtonEvent(MouseButton.LEFT, ButtonTransition.PRESSED)), input.buttonEvents());

        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.RELEASED);
        input.onButtonChanged(MouseButton.LEFT, ButtonTransition.RELEASED);
        input.beginFixedUpdate();

        assertEquals(List.of(new ButtonEvent(MouseButton.LEFT, ButtonTransition.RELEASED)), input.buttonEvents());
    }

    @Test
    void cursorEstablishBaseLineWithNoMovement() {
        BufferedInput input = new BufferedInput();
        input.onCursorMoved(100,  200);
        assertEquals(CursorState.ZERO, input.cursor());

        input.beginFixedUpdate();
        assertEquals(new CursorState(100, 200, 0, 0), input.cursor());
    }

    @Test
    void cursorPositionCalculationTest() {
        BufferedInput input = new BufferedInput();
        input.onCursorMoved(0,  0);
        input.onCursorMoved(10, 10);
        assertEquals(CursorState.ZERO, input.cursor());

        input.beginFixedUpdate();
        assertEquals(new CursorState(10.0, 10.0, 10, 10), input.cursor());

        input.onCursorMoved(10, 10);
        input.beginFixedUpdate();
        assertEquals(new CursorState(10.0, 10.0, 0, 0), input.cursor());
    }

    @Test
    void cursorPositionAtZeroValid() {
        BufferedInput input = new BufferedInput();
        input.onCursorMoved(0, 0);
        input.onCursorMoved(0, 0);

        input.beginFixedUpdate();
        assertEquals(CursorState.ZERO, input.cursor());
    }

    @Test
    void accumulatesScrollUntilFixedUpdateBegins() {
        BufferedInput input = new BufferedInput();

        input.onScrolled(0.0, 1.0);
        input.onScrolled(0.5, 2.0);

        assertEquals(ScrollDelta.ZERO, input.scrollDelta());

        input.beginFixedUpdate();

        assertEquals(
                new ScrollDelta(0.5, 3.0),
                input.scrollDelta()
        );
    }

    @Test
    void cursorTrackingInterruptionDiscardsPendingMovementAndEstablishesANewBaseline() {
        BufferedInput input = new BufferedInput();

        input.onCursorMoved(10.0, 10.0);
        input.onCursorMoved(15.0, 18.0);
        input.beginFixedUpdate();

        CursorState publishedBeforeInterruption = input.cursor();
        assertEquals(new CursorState(15.0, 18.0, 5.0, 8.0), publishedBeforeInterruption);

        input.onCursorMoved(20.0, 20.0);
        input.onCursorTrackingInterrupted();

        assertEquals(publishedBeforeInterruption, input.cursor());

        input.onCursorMoved(100.0, 200.0);
        input.onCursorMoved(103.0, 206.0);

        assertEquals(publishedBeforeInterruption, input.cursor());

        input.beginFixedUpdate();

        assertEquals(new CursorState(103.0, 206.0, 3.0, 6.0), input.cursor());
    }
}
