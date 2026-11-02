package org.twingolfpapa.engine.graphics.camera;

import org.joml.Matrix4f;

import java.util.Objects;

public final class OrthographicCamera {
    private float previousX;
    private float previousY;

    private float currentX;
    private float currentY;

    private float previousViewHalfHeight;
    private float currentViewHalfHeight;

    public OrthographicCamera(float initialViewHalfHeight) {
        if (!Float.isFinite(initialViewHalfHeight) || initialViewHalfHeight <= 0.0f) {
            throw new IllegalArgumentException("View half-height must be finite and positive");
        }

        previousViewHalfHeight = initialViewHalfHeight;
        currentViewHalfHeight = initialViewHalfHeight;
    }

    public void beginFixedUpdate() {
        previousX = currentX;
        previousY = currentY;
        previousViewHalfHeight = currentViewHalfHeight;
    }

    public void move(float deltaX, float deltaY) {
        currentX += deltaX;
        currentY += deltaY;
    }

    public float viewHalfHeight() {
        return currentViewHalfHeight;
    }

    public void setViewHalfHeight(float viewHalfHeight) {
        validateViewHalfHeight(viewHalfHeight);
        currentViewHalfHeight = viewHalfHeight;
    }

    public Matrix4f writeViewMatrix(float interpolationAlpha, Matrix4f destination) {
        Objects.requireNonNull(destination, "destination");

        float renderedX = interpolate(previousX, currentX, interpolationAlpha);
        float renderedY = interpolate(previousY, currentY, interpolationAlpha);
        return destination.identity().translate(-renderedX, -renderedY, 0.0f);
    }

    public Matrix4f writeProjectionMatrix(float aspectRatio, float interpolationAlpha, Matrix4f destination) {
        Objects.requireNonNull(destination, "destination");

        if (!Float.isFinite(aspectRatio) || aspectRatio <= 0.0f) {
            throw new IllegalArgumentException(
                    "Aspect ratio must be finite and positive"
            );
        }

        float renderedHalfHeight = interpolate(previousViewHalfHeight, currentViewHalfHeight, interpolationAlpha);
        float renderedHalfWidth = renderedHalfHeight * aspectRatio;
        return destination.identity()
                .ortho2D(
                        -renderedHalfWidth,
                        renderedHalfWidth,
                        -renderedHalfHeight,
                        renderedHalfHeight
                );
    }

    private static float interpolate(float previous, float current, float alpha) {
        return previous + (current - previous) * alpha;
    }

    private static void validateViewHalfHeight(float viewHalfHeight) {
        if (!Float.isFinite(viewHalfHeight) || viewHalfHeight <= 0.0f) {
            throw new IllegalArgumentException("View half-height must be finite and positive");
        }
    }
}
