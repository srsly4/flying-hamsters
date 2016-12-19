package engine.render;


import engine.EngineException;
import engine.Transformation;
import engine.Utils;
import engine.essential.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Renderer {
    private final Transformation transformation;
    private final ShaderProgram sProgram;
    public Renderer() throws EngineException {
        transformation = new Transformation();
        sProgram = new ShaderProgram();
    }

    public void init(Window window) throws Exception {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        sProgram.createVertexShader(Utils.loadResource("/vertex.glsl"));
        sProgram.createFragmentShader(Utils.loadResource("/simple_color.glsl"));
        sProgram.link();

        sProgram.createUniform("projectionMatrix");
        sProgram.createUniform("worldMatrix");
        sProgram.createUniform("texture_sampler");
        sProgram.createUniform("texturePosition");
    }

    public void render(IRenderable[] items, Window window)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        sProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(window.getWidth(), window.getHeight());
        sProgram.setUniform("projectionMatrix", projectionMatrix);
        sProgram.setUniform("texture_sampler", 0);
        for (IRenderable item : items)
        {
            sProgram.setUniform("texturePosition", item.getTextureOrigin());
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    item.getPosition(),
                    item.getRotation(),
                    item.getScale());
            sProgram.setUniform("worldMatrix", worldMatrix);
            item.render();
        }
        sProgram.unbind();
    }

    public void cleanup(){
        sProgram.cleanup();
    }
}
