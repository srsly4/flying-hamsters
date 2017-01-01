package engine.render;

import org.joml.Matrix4f;

/**
 * Created by Sirius on 01.01.2017.
 */
public abstract class Shaderable implements IRenderable {
    private ShaderProgram shaderProgram = null;
    protected IShaderFiller shaderFiller = null;
    public boolean hasCustomShader(){
        return shaderProgram != null;
    }
    public ShaderProgram getCustomShader(){
        return shaderProgram;
    }
    public void setCustomShader(ShaderProgram shader)
    {
        this.shaderProgram = shader;
    }

    public void setShaderFiller(IShaderFiller shaderFiller)
    {
        this.shaderFiller = shaderFiller;
    }

    public void prepareShaderUniforms(Matrix4f projection, Matrix4f world) {
        shaderProgram.setUniform("projectionMatrix", projection);
        shaderProgram.setUniform("texture_sampler", 0);
        shaderProgram.setUniform("texture_origin", getTextureOrigin());
        shaderProgram.setUniform("worldMatrix", world);
        if (shaderFiller != null)
            shaderFiller.fillShader(shaderProgram);
    }
}
