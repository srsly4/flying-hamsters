package engine.render;


import engine.EngineException;
import engine.Transformation;
import engine.essential.Window;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Renderer {
    private final Transformation transformation;
    private ShaderProgram defaultProgram;
    private Queue<Shaderable> shaderables;
    public Renderer() throws EngineException {
        transformation = new Transformation();
    }

    public void init(Window window) throws EngineException {
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        defaultProgram = ShaderLoader.getInstance().loadProgram("default",
                new String[]{"projectionMatrix", "worldMatrix", "texture_sampler", "texture_origin"});

        shaderables = new LinkedList<>();

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
            if (item instanceof Shaderable && ((Shaderable)item).hasCustomShader()){
                shaderables.add((Shaderable)item);
                continue;
            }
            defaultProgram.setUniform("texture_origin", item.getTextureOrigin());
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    item.getPosition(),
                    item.getRotation(),
                    item.getScale());
            defaultProgram.setUniform("worldMatrix", worldMatrix);
            item.render();
        }
        defaultProgram.unbind();

        //render custom-shadered objects
        ShaderProgram shdProg = null;
        while (!shaderables.isEmpty()){
            Shaderable shdObj = shaderables.poll();
            if (shdProg != shdObj.getCustomShader())
            {
                if (shdProg != null) shdProg.unbind();
                shdProg = shdObj.getCustomShader();
                shdProg.bind();
            }
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    shdObj.getPosition(),
                    shdObj.getRotation(),
                    shdObj.getScale());
            shdObj.prepareShaderUniforms(projectionMatrix, worldMatrix);

            shdObj.render();
        }
    }

    public void cleanup(){
        defaultProgram.cleanup();
    }
}
