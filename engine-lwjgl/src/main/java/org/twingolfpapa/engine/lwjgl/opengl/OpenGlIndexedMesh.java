package org.twingolfpapa.engine.lwjgl.opengl;

import java.util.Objects;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public final class OpenGlIndexedMesh implements AutoCloseable {
    private static final int POSITION_COMPONENTS = 3;
    private static final int COLOR_COMPONENTS = 3;
    private static final int FLOATS_PER_VERTEX =
            POSITION_COMPONENTS + COLOR_COMPONENTS;

    private static final int STRIDE_BYTES =
            FLOATS_PER_VERTEX * Float.BYTES;

    private static final long POSITION_OFFSET_BYTES = 0L;
    private static final long COLOR_OFFSET_BYTES =
            POSITION_COMPONENTS * Float.BYTES;

    private final int vertexArrayId;
    private final int vertexBufferId;
    private final int elementBufferId;
    private final int indexCount;

    private boolean closed;

    private OpenGlIndexedMesh(int vertexArrayId, int vertexBufferId, int elementBufferId, int indexCount) {
        this.vertexArrayId = vertexArrayId;
        this.vertexBufferId = vertexBufferId;
        this.elementBufferId = elementBufferId;
        this.indexCount = indexCount;
    }

    public static OpenGlIndexedMesh create(float[] vertices, int[] indices) {
        validateIndexedMeshCreation(vertices, indices);

        int vertexArrayId = 0;
        int vertexBufferId = 0;
        int elementBufferId = 0;

        try {
            vertexArrayId = glGenVertexArrays();
            glBindVertexArray(vertexArrayId);

            vertexBufferId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);

            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            //layout 0
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, POSITION_COMPONENTS, GL_FLOAT, false,
                    STRIDE_BYTES, POSITION_OFFSET_BYTES);

            //layout 1
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, COLOR_COMPONENTS, GL_FLOAT, false,
                    STRIDE_BYTES, COLOR_OFFSET_BYTES);

            elementBufferId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            return new OpenGlIndexedMesh(vertexArrayId, vertexBufferId, elementBufferId, indices.length);
        } catch (RuntimeException e) {
            glBindVertexArray(0);

            if (vertexArrayId != 0) {
                glDeleteVertexArrays(vertexArrayId);
            }

            if (elementBufferId != 0) {
                glDeleteBuffers(elementBufferId);
            }

            if (vertexBufferId != 0) {
                glDeleteBuffers(vertexBufferId);
            }

            throw e;
        }
    }

    public void draw() {
        if (closed) {
            throw new IllegalStateException("Cannot draw a closed mesh");
        }

        glBindVertexArray(vertexArrayId);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L);
        glBindVertexArray(0);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;

        glDeleteVertexArrays(vertexArrayId);
        glDeleteBuffers(vertexBufferId);
        glDeleteBuffers(elementBufferId);
    }

    private static void validateIndexedMeshCreation(float[] vertices, int[] indices) {
        Objects.requireNonNull(vertices, "vertices");
        Objects.requireNonNull(indices, "indices");

        if (vertices.length == 0) {
            throw new IllegalArgumentException("Vertex data cannot be empty");
        }

        if (indices.length == 0) {
            throw new IllegalArgumentException("Index data cannot be empty");
        }

        if (vertices.length % FLOATS_PER_VERTEX != 0) {
            throw new IllegalArgumentException("Vertex array length must be divisible by 6");
        }

        int vertexCount = vertices.length / FLOATS_PER_VERTEX;
        for (int index : indices) {
            if (index < 0 || index >= vertexCount) {
                throw new IllegalArgumentException("Index " + index + " is outside the vertex range [0, " + vertexCount + ")");
            }
        }
    }
}
