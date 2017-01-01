package engine.render;


import com.sun.prism.ps.ShaderFactory;
import engine.EngineException;
import engine.Transformation;
import engine.Utils;
import engine.essential.Window;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Renderer {
    private final Transformation transformation;
    private ShaderProgram defaultProgram;
    public Renderer() throws EngineException {
        transformation = new Transformation();
    }

    public void init(Window window) throws EngineException {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        defaultProgram = ShaderLoader.getInstance().loadProgram("default",
                new String[]{"projectionMatrix", "worldMatrix", "texture_sampler", "texture_origin"});

    }

    public void render(IRenderable[] items, Window window)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //at first, render all standard-texturized objects
        defaultProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(window.getWidth(), window.getHeight());
        defaultProgram.setUniform("projectionMatrix", projectionMatrix);
        defaultProgram.setUniform("texture_sampler", 0);
        for (IRenderable item : items)
        {
            if(!item.isVisible()) continue;
            defaultProgram.setUniform("texture_origin", item.getTextureOrigin());
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    item.getPosition(),
                    item.getRotation(),
                    item.getScale());
            defaultProgram.setUniform("worldMatrix", worldMatrix);
            item.render();
        }
        defaultProgram.unbind();
    }

    public void cleanup(){
        defaultProgram.cleanup();
    }
}
