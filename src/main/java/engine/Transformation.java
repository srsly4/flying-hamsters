package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f worldMatrix;
    private final Vector2f textureTranslate;
    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        textureTranslate = new Vector2f(0, 0);
    }

    public final Matrix4f getProjectionMatrix(float width, float height) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.setOrtho2D(-1.0f, 1.0f, -1.0f / aspectRatio, 1.0f / aspectRatio);
//        projectionMatrix.ortho(-1.0f, 1.0f, -1.0f / aspectRatio, 1.0f / aspectRatio, -1.0f, 1.0f);
        return projectionMatrix;
    }

    public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
        worldMatrix.identity().translate(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }

    public Vector2f getTextureTranslate(float x, float y){
        textureTranslate.set(x, y);
        return textureTranslate;
    }
}
