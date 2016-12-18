package engine.render;

import engine.GLException;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram() throws GLException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new GLException("Could not create Shader", "glCreateProgram");
        }
    }

    public int createVertexShader(String name) throws GLException {
        return createShader(name, GL_VERTEX_SHADER);
    }

    public int createFragmentShader(String name) throws GLException {
        return createShader(name, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String name, int type) throws GLException {
        int shid = glCreateShader(type);
        if (shid == 0) throw new GLException("Error while creating shader", "glCreateShader");

        glShaderSource(shid, name);
        glCompileShader(shid);

        if (glGetShaderi(shid, GL_COMPILE_STATUS) == 0) throw new GLException("Error while compiling shader", "glCompileShader");

        glAttachShader(programId, shid);
        return shid;
    }

    public void link() throws GLException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new GLException("Could not link Shader Program: " + glGetProgramInfoLog(programId, 1024), "glLinkProgram");

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
            throw new GLException("Could not validate Shader Program" + glGetProgramInfoLog(programId, 1024), "glValidateProgram");
    }

    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
    }

}