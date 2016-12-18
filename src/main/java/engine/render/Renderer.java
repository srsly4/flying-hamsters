package engine.render;


import engine.Utils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Renderer {
    ShaderProgram sProgram;
    private FloatBuffer verticesBuffer;
    private int vaoId;
    private int vboId;

    public Renderer() {
    }

    public void init() throws Exception {
        sProgram = new ShaderProgram();
        sProgram.createVertexShader(Utils.loadResource("/simple_vertex.glsl"));
        sProgram.createFragmentShader(Utils.loadResource("/simple_color.glsl"));
        sProgram.link();


        float[] vertices = {
                // Left bottom triangle
                0f, 0.1f, 0f,
                -0.1f, -0.1f, 0f,
                0.1f, -0.1f, 0f
        };
        // Sending data to OpenGL requires the usage of (flipped) byte buffers
        verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        glBindVertexArray(0);


    }

    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        sProgram.bind();
        // Bind to the VAO that has all the information about the quad vertices
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Put everything back to default (deselect)
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        sProgram.unbind();
    }

    public void cleanup(){
        if (sProgram != null) {
            sProgram.cleanup();
        }

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
