package engine.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public interface IRenderable {
    void render();
    void cleanUp();
    Vector3f getPosition();
    Vector3f getRotation();
    float getScale();
    Vector2f getTextureOrigin();
    boolean isVisible();
}