package engine.render;

import java.nio.FloatBuffer;

import engine.EngineException;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLXStereoNotifyEventEXT;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class VertexBuffer implements IRenderable {
    private final int vaoId;
    private final int vboId;
    private final int texVboId;
    private final int vertexCount;
    private final Texture texture;

    public VertexBuffer(float[] points, float[] texturePosition, Texture texture) throws EngineException{
        vertexCount = points.length/3;

        //vertexes
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(points.length);
        verticesBuffer.put(points);
        verticesBuffer.flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //texture vaos
        texVboId = glGenBuffers();
        FloatBuffer texBuffer = BufferUtils.createFloatBuffer(texturePosition.length);
        texBuffer.put(texturePosition);
        texBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, texVboId);
        glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        //set handle to 0
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.texture = texture;
    }

    public int getVaoId() { return vaoId; }
    public int getVboId() { return vboId; }
    public int getVertexCount() { return vertexCount; }
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(texVboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    public void render() {
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        // Bind to the VAO that has all the information about the quad vertices
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, getVertexCount());
//        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Put everything back to default (deselect)
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
