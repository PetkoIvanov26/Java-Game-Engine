package org.twingolfpapa.engine.lwjgl.opengl;

import org.joml.Matrix4fc;

import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public final class OpenGlShaderProgram implements AutoCloseable {
    private final int programId;
    private boolean closed;
    private final float[] matrixScratch = new float[16];

    private OpenGlShaderProgram(int programId) {
        this.programId = programId;
    }

    public static OpenGlShaderProgram create(String vertexSource, String fragmentSource) {
        int vertexShaderId = 0;
        int fragmentShaderId = 0;
        int programId = 0;

        try {
            vertexShaderId = compile(GL_VERTEX_SHADER, "vertex", vertexSource);
            fragmentShaderId = compile(GL_FRAGMENT_SHADER, "fragment", fragmentSource);
            programId = glCreateProgram();

            if (programId == 0) {
                throw new IllegalStateException("OpenGL could not create the shader program");
            }

            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);

            glLinkProgram(programId);
            int linkStatus = glGetProgrami(programId, GL_LINK_STATUS);
            if (linkStatus == GL_FALSE) {
                String linkerLog = glGetProgramInfoLog(programId);
                throw new IllegalArgumentException("Failed to link shader program: \n" + linkerLog);
            }

            glDetachShader(programId, vertexShaderId);
            glDetachShader(programId, fragmentShaderId);

            return new OpenGlShaderProgram(programId);
        } catch (RuntimeException e) {
            if (programId != 0) {
                glDeleteProgram(programId);
            }

            throw e;
        } finally {
            if (vertexShaderId != 0) {
                glDeleteShader(vertexShaderId);
            }

            if (fragmentShaderId != 0) {
                glDeleteShader(fragmentShaderId);
            }
        }
    }

    public void bind() {
        if (closed) {
            throw new IllegalStateException("Cannot bind a closed shader program");
        }

        glUseProgram(programId);
    }

    public int uniformLocation(String name) {
        if (closed) {
            throw new IllegalStateException("Cannot query a closed shader program");
        }

        Objects.requireNonNull(name, "name");

        int location = glGetUniformLocation(programId, name);

        if (location == -1) {
            throw new IllegalArgumentException("Uniform is missing or inactive: " + name);
        }

        return location;
    }

    public void setMatrix4(int location, Matrix4fc matrix) {
        if (closed) {
            throw new IllegalStateException();
        }

        Objects.requireNonNull(matrix);
        matrix.get(matrixScratch);
        glUniformMatrix4fv(location, false, matrixScratch);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;
        glDeleteProgram(programId);
    }

    private static int compile(int shaderType, String stageName, String source) {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0) {
            throw new IllegalStateException("OpenGl could not create the " + stageName + " shader");
        }

        try {
            glShaderSource(shaderId, source);
            glCompileShader(shaderId);

            int compilationStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS);

            if (compilationStatus == GL_FALSE) {
                String compilerLog = glGetShaderInfoLog(shaderId);

                throw new IllegalStateException("Failed to compile " + stageName + " shader: \n" + compilerLog);
            }

            return shaderId;
        } catch (RuntimeException e) {
            glDeleteShader(shaderId);
            throw e;
        }
    }
}
