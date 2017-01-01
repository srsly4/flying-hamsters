package engine.render;

import org.joml.Matrix4f;

/**
 * Created by Sirius on 01.01.2017.
 */
public abstract class Shaderable implements IRenderable {
    private ShaderProgram shaderProgram = null;

    public boolean hasCustomShader(){
        return shaderProgram == null;
    }
    public ShaderProgram getCustomShader(){
        return shaderProgram;
    }
    public void setCustomShader(ShaderProgram shader)
    {
        this.shaderProgram = shader;
    }

    public abstract void prepareShaderUniforms(Matrix4f projection, Matrix4f world);
}
