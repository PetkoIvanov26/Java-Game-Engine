package org.twingolfpapa.engine.lwjgl.opengl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class OpenGlIndexedMeshTest {
    private static final int FLOATS_PER_VERTEX = 6;

    @Test
    void rejectsNullVertexData() {
        assertThrows(NullPointerException.class,
                () -> OpenGlIndexedMesh.create(null, new int[]{0}));
    }

    @Test
    void rejectsNullIndexData() {
        assertThrows(NullPointerException.class,
                () -> OpenGlIndexedMesh.create(new float[FLOATS_PER_VERTEX], null));
    }

    @Test
    void rejectsEmptyVertexData() {
        assertThrows(IllegalArgumentException.class,
                () -> OpenGlIndexedMesh.create(new float[0], new int[]{0}));
    }

    @Test
    void rejectsEmptyIndexData() {
        assertThrows(IllegalArgumentException.class,
                () -> OpenGlIndexedMesh.create(new float[FLOATS_PER_VERTEX], new int[0]));
    }

    @Test
    void rejectsIncompleteVertexData() {
        assertThrows(IllegalArgumentException.class,
                () -> OpenGlIndexedMesh.create(new float[FLOATS_PER_VERTEX - 1], new int[]{0}));
    }

    @Test
    void rejectsNegativeIndex() {
        assertThrows(IllegalArgumentException.class,
                () -> OpenGlIndexedMesh.create(new float[FLOATS_PER_VERTEX], new int[]{-1}));
    }

    @Test
    void rejectsIndexEqualToVertexCount() {
        float[] vertices = new float[4 * FLOATS_PER_VERTEX];

        assertThrows(IllegalArgumentException.class,
                () -> OpenGlIndexedMesh.create(vertices, new int[]{0, 1, 4}));
    }
}
