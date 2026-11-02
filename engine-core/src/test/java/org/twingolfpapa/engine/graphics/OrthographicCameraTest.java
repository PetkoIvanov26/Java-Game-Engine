package org.twingolfpapa.engine.graphics;

import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;
import org.twingolfpapa.engine.graphics.camera.OrthographicCamera;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrthographicCameraTest {
    private static final float TOLERANCE = 0.0001f;

    @Test
    void writesInterpolatedInverseCameraPositionIntoViewMatrix() {
        OrthographicCamera camera = new OrthographicCamera(1.0f);
        Matrix4f destination = new Matrix4f();

        camera.beginFixedUpdate();
        camera.move(4.0f, -2.0f);

        camera.writeViewMatrix(0.5f, destination);

        assertEquals(-2.0f, destination.m30(), TOLERANCE);
        assertEquals(1.0f, destination.m31(), TOLERANCE);
    }
}
