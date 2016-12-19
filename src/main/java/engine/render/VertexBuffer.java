package engine.render;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class VertexBuffer {
    private final int vaoId;
    private final int vboId;
    private final int vertexCount;

    public VertexBuffer(float[] points){
        vertexCount = points.length/3;

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(points.length);
        verticesBuffer.put(points);
        verticesBuffer.flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public int getVaoId() { return vaoId; }
    public int getVboId() { return vboId; }
    public int getVertexCount() { return vertexCount; }
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    public void render() {
        // Bind to the VAO that has all the information about the quad vertices
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Put everything back to default (deselect)
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
