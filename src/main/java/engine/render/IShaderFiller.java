package engine.render;

/**
 * Created by Sirius on 01.01.2017.
 */
public interface IShaderFiller {
    void fillShader(ShaderProgram program);
    default String[] getCustomUniformNames(){
        return new String[]{};
    }
}
