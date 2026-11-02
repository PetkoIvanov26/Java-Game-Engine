package org.twingolfpapa.sandbox;

import org.joml.Matrix4f;
import org.twingolfpapa.engine.api.EngineClient;
import org.twingolfpapa.engine.api.EngineContext;
import org.twingolfpapa.engine.core.EngineConfiguration;
import org.twingolfpapa.engine.graphics.FramebufferSize;
import org.twingolfpapa.engine.graphics.camera.OrthographicCamera;
import org.twingolfpapa.engine.input.*;
import org.twingolfpapa.engine.lwjgl.DesktopApplication;
import org.twingolfpapa.engine.lwjgl.WindowConfiguration;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlFrame;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlIndexedMesh;
import org.twingolfpapa.engine.lwjgl.opengl.OpenGlShaderProgram;

import java.util.Objects;

/** Provides a neutral playground for engine features without embedding a game in the engine. */
public final class SandboxApplication implements EngineClient {

    private static final String VERTEX_SHADER = """
        #version 330 core

        layout(location = 0) in vec3 position;
        layout(location = 1) in vec3 color;

        uniform mat4 view;
        uniform mat4 projection;
        uniform mat4 model;

        out vec3 vertexColor;

        void main() {
            gl_Position = projection * view * model * vec4(position, 1.0);
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
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final OrthographicCamera camera = new OrthographicCamera(1.0f);

    private int viewLocation;
    private int projectionLocation;
    private int modelLocation;

    private final Matrix4f modelMatrix = new Matrix4f();

    private static final float ROTATION_SPEED_RADIANS =
            (float) Math.toRadians(90.0);

    private float previousRotationRadians;
    private float currentRotationRadians;

    private static final float CAMERA_SPEED = 1.5f;
    private static final float MINIMUM_VIEW_HALF_HEIGHT = 0.25f;
    private static final float MAXIMUM_VIEW_HALF_HEIGHT = 10.0f;
    private static final float ZOOM_SENSITIVITY = 0.15f;

    private EngineContext context;
    private boolean wHeld;
    private OpenGlShaderProgram shaderProgram;
    private OpenGlIndexedMesh mesh;

     static void main(String[] arguments) {
        DesktopApplication.   run(
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
        viewLocation = shaderProgram.uniformLocation("view");
        projectionLocation = shaderProgram.uniformLocation("projection");

        viewMatrix.identity();
        mesh = OpenGlIndexedMesh.create(QUAD_VERTICES, QUAD_INDICES);
    }

    @Override
    public void fixedUpdate(double fixedDeltaSeconds) {
        if (context.input().wasButtonPressed(Key.ESCAPE)) {
            context.control().requestStop();
        }

        camera.beginFixedUpdate();
        previousRotationRadians = currentRotationRadians;

        float directionX = 0.0f;
        float directionY = 0.0f;

        if (context.input().isButtonDown(Key.A)) {
            directionX -= 1.0f;
        }

        if (context.input().isButtonDown(Key.D)) {
            directionX += 1.0f;
        }

        if (context.input().isButtonDown(Key.W)) {
            directionY += 1.0f;
        }

        if (context.input().isButtonDown(Key.S)) {
            directionY -= 1.0f;
        }

        float directionLength =
                (float) Math.sqrt(
                        directionX * directionX +
                                directionY * directionY
                );

        if (directionLength > 0.0f) {
            directionX /= directionLength;
            directionY /= directionLength;
        }

        float cameraDeltaX = directionX * CAMERA_SPEED * (float) fixedDeltaSeconds;
        float cameraDeltaY = directionY * CAMERA_SPEED * (float) fixedDeltaSeconds;

        camera.move(cameraDeltaX, cameraDeltaY);

        double scrollY = context.input().scrollDelta().y();

        float requestedViewHalfHeight = camera.viewHalfHeight()
                * (float) Math.exp(-scrollY * ZOOM_SENSITIVITY);

        float clampedViewHalfHeight = Math.max(MINIMUM_VIEW_HALF_HEIGHT, Math.min(MAXIMUM_VIEW_HALF_HEIGHT, requestedViewHalfHeight));

        camera.setViewHalfHeight(clampedViewHalfHeight);

        wHeld = context.input().isButtonDown(MouseButton.LEFT);

        currentRotationRadians += ROTATION_SPEED_RADIANS * (float) fixedDeltaSeconds;
    }

    @Override
    public void render(double interpolationAlpha) {
        if (wHeld) {
            OpenGlFrame.clear(0.10f, 0.35f, 0.15f, 1.0f);
        } else {
            OpenGlFrame.clear(0.055f, 0.065f, 0.085f, 1.0f);
        }
        FramebufferSize size = context.renderSurface().getFramebufferSize();

        if (!size.hasArea()) {
            return;
        }

        float alpha = (float) interpolationAlpha;

        float aspectRatio = (float) size.width() / size.height();
        camera.writeProjectionMatrix(aspectRatio, alpha, projectionMatrix);

        float renderedRotation = previousRotationRadians + (currentRotationRadians - previousRotationRadians) * alpha;

        modelMatrix.identity().translate(0.25f, 0.0f, 0.0f).rotateZ(renderedRotation).scale(0.65f);
        camera.writeViewMatrix(alpha, viewMatrix);
        shaderProgram.bind();
        shaderProgram.setMatrix4(modelLocation, modelMatrix);
        shaderProgram.setMatrix4(viewLocation, viewMatrix);
        shaderProgram.setMatrix4(projectionLocation, projectionMatrix);

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
