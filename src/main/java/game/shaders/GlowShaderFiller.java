package game.shaders;

import engine.render.IShaderFiller;
import engine.render.ShaderProgram;
import org.joml.Vector3f;

/**
 * Created by Sirius on 01.01.2017.
 */
public class GlowShaderFiller implements IShaderFiller {
    private float glowTime = 0f;
    private float intesity = 1f;
    private Vector3f glowColor;

    public void setGlowColor(Vector3f glowColor) {
        this.glowColor = glowColor;
    }

    public void update(double interval){
        glowTime += interval;
    }

    @Override
    public void fillShader(ShaderProgram program) {
//        program.setUniform("time", glowTime);
//        program.setUniform("intensity", intesity);
        program.setUniform("glowColor", glowColor);
    }

    @Override
    public String[] getCustomUniformNames() {
        return new String[]{"worldMatrix", "projectionMatrix", "texture_origin", "texture_sampler", "glowColor"};
    }
}
