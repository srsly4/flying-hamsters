package engine.render;


import engine.EngineException;
import engine.Utils;

import java.util.HashMap;

/**
 * Created by Sirius on 01.01.2017.
 */
public class ShaderLoader {
    protected static String shaderResourcePath = "/shaders/";


    private HashMap<String, ShaderProgram> programs;
    private ShaderLoader(){
        programs = new HashMap<>();
    }

    /**
     * Loads a shader program from cache or from resource file and compiles both vertex and fragment shader
     * @param resourceName Filename without type and extension
     * @param uniforms Array of initialized uniforms
     * @return Instance of shader program
     * @throws EngineException
     */
    public ShaderProgram loadProgram(String resourceName, String[] uniforms) throws EngineException {
        if (programs.containsKey(resourceName))
            return programs.get(resourceName);

        //load and create
        ShaderProgram program = new ShaderProgram();
        program.createVertexShader(Utils.loadResource(shaderResourcePath + resourceName + ".vertex.glsl"));
        program.createFragmentShader(Utils.loadResource(shaderResourcePath + resourceName + ".frag.glsl"));

        //create uniforms
        program.link();
        for (String u : uniforms)
            program.createUniform(u);

        programs.put(resourceName, program);
        return program;
    }

    private static ShaderLoader instance;
    public static ShaderLoader getInstance(){
        if (instance == null)
            instance = new ShaderLoader();
        return instance;
    }
}
