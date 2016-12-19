package engine.render;

import engine.GLException;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private HashMap<String, Integer> uniforms;


    public ShaderProgram() throws GLException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new GLException("Could not create Shader", "glCreateProgram");
        }
        uniforms = new HashMap<>();
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

        if (glGetShaderi(shid, GL_COMPILE_STATUS) == 0) throw new GLException("Error while compiling shader - " +
                glGetShaderInfoLog(programId), "glCompileShader");

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

    public void createUniform(String name) throws GLException {
        int location = glGetUniformLocation(programId, name);
        if (location < 0) throw new GLException("Could not locate Shader's uniform '" + name + "'", "glGetUniformLocation");
        uniforms.put(name, location);
    }

    public void setUniform(String name, Matrix4f mat){
        FloatBuffer buff = BufferUtils.createFloatBuffer(16);
        mat.get(buff);
        glUniformMatrix4fv(uniforms.get(name), false, buff);
    }

    public void setUniform(String name, Vector2f vec){
        FloatBuffer buff = BufferUtils.createFloatBuffer(2);
        vec.get(buff);
        glUniform2fv(uniforms.get(name), buff);
    }

    public void setUniform(String name, int val){
        glUniform1i(uniforms.get(name), val);
    }

}
