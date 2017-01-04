package engine.render;

import org.joml.Vector4f;

/**
 * Created by Szymon Piechaczek on 04.01.2017.
 */
public class TextShaderFiller implements IShaderFiller {

    private final Vector4f color;
    public TextShaderFiller(Vector4f color) {
        this.color = color;
    }

    public TextShaderFiller(){
        this(new Vector4f(1f, 1f, 1f, 1f));
    }

    public void setColors(float r, float g, float b, float a){
        color.x = r;
        color.y = g;
        color.z = b;
        color.w = a;
    }
    @Override
    public void fillShader(ShaderProgram program) {
        program.setUniform("faceColor", color);
    }

    @Override
    public String[] getCustomUniformNames() {
        return new String[]{"worldMatrix", "projectionMatrix", "texture_origin", "texture_sampler", "faceColor"};
    }
}
