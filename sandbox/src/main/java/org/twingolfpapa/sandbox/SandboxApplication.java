package org.twingolfpapa.sandbox;

import org.joml.Matrix4f;
import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.api.EngineContext;
import org.twingolfpapa.engine.core.EngineConfiguration;
import org.twingolfpapa.engine.input.*;
import org.twingolfpapa.engine.lwjgl.DesktopApplication;
import org.twingolfpapa.engine.lwjgl.WindowConfiguration;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlFrame;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlIndexedMesh;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlShaderProgram;

import java.util.Objects;

/** Neutral playground used to exercise engine features without embedding a game in the engine. */
public final class SandboxApplication implements EngineClient {

    private static final String VERTEX_SHADER = """
        #version 330 core

        layout(location = 0) in vec3 position;
        layout(location = 1) in vec3 color;

        uniform mat4 model;

        out vec3 vertexColor;

        void main() {
            gl_Position = model * vec4(position, 1.0);
            vertexColor = color;
        }
        """;

    private static final String FRAGMENT_SHADER = """
        #version 330 core

        in vec3 vertexColor;

        layout(location = 0) out vec4 fragmentColor;

        void main() {
            fragmentColor = vec4(vertexColor, 1.0);
        }
        """;

    private static final float[] QUAD_VERTICES = {
            // position             // color
            -0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f,     0.0f, 1.0f, 0.0f,
            0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f,
            -0.5f,  0.5f, 0.0f,     1.0f, 1.0f, 0.0f
    };

    private static final int[] QUAD_INDICES = {
            0, 1, 2,
            2, 3, 0
    };
    private int modelLocation;
    private final Matrix4f modelMatrix = new Matrix4f();

    private static final float ROTATION_SPEED_RADIANS =
            (float) Math.toRadians(90.0);

    private float previousRotationRadians;
    private float currentRotationRadians;


    private EngineContext context;
    private boolean wHeld;
    private OpenGlShaderProgram shaderProgram;
    private OpenGlIndexedMesh mesh;

     static void main(String[] arguments) {
        DesktopApplication.run(
                WindowConfiguration.windowed(1280, 720, "Project Engine Sandbox"),
                EngineConfiguration.atFixedRate(60),
                new SandboxApplication()
        );
    }

    @Override
    public void initialize(EngineContext context) {
        this.context = context;
        shaderProgram = OpenGlShaderProgram.create(VERTEX_SHADER, FRAGMENT_SHADER);

        modelLocation = shaderProgram.uniformLocation("model");

        mesh = OpenGlIndexedMesh.create(QUAD_VERTICES, QUAD_INDICES);
    }

    @Override
    public void fixedUpdate(double fixedDeltaSeconds) {
         if (context.input().wasButtonPressed(Key.ESCAPE)) {
             context.control().requestStop();
         }

         wHeld = context.input().isButtonDown(MouseButton.LEFT);
        previousRotationRadians = currentRotationRadians;

        currentRotationRadians += ROTATION_SPEED_RADIANS * (float) fixedDeltaSeconds;
    }

    @Override
    public void render(double interpolationAlpha) {
        if (wHeld) {
            OpenGlFrame.clear(0.10f, 0.35f, 0.15f, 1.0f);
        } else {
            OpenGlFrame.clear(0.055f, 0.065f, 0.085f, 1.0f);
        }

        float alpha = (float) interpolationAlpha;

        float renderedRotation =
                previousRotationRadians
                        + (currentRotationRadians - previousRotationRadians)
                        * alpha;

        modelMatrix.identity()
                .translate(0.25f, 0.0f, 0.0f)
                .rotateZ(renderedRotation)
                .scale(0.65f);


        shaderProgram.bind();
        shaderProgram.setMatrix4(modelLocation, modelMatrix);
        mesh.draw();
    }

    @Override
    public void shutdown() {
        if (Objects.nonNull(mesh)) {
            mesh.close();
            mesh = null;
        }

        if (Objects.nonNull(shaderProgram)) {
            shaderProgram.close();
            shaderProgram = null;
        }
    }
}
