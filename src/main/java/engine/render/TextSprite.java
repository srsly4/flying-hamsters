package engine.render;

import engine.essential.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.system.MemoryStack;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirius on 22.12.2016.
 */
public class TextSprite implements IRenderable {

    private final Vector3f rotation;
    private final Vector3f position;
    private final Vector2f textureOrigin;
    private float scale;
    private boolean visibility = true;

    private final Window window;

    private FontBuffer font;
    private String text = "null";

    private int vaoId = 0;
    private int vboId;
    private int texVboId;
    private int vertexCount;

    public TextSprite(FontBuffer font, Window window){
        this.font = font;
        this.position = new Vector3f(0, 0,0);
        this.rotation = new Vector3f(0, 0,0);
        this.textureOrigin = new Vector2f(0, 0);
        this.scale = 1f;
        this.window = window;
    }

    public void setText(String text)
    {
        this.text = text;
        if (vaoId != 0)
            freeTextMeshMemory();
        buildTextMesh();
    }

    private void freeTextMeshMemory(){
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(texVboId);
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    private void buildTextMesh(){
        List<Float> vertexes = new ArrayList<>();
        List<Float> texturePoints = new ArrayList<>();

        final float lineSize = 2f*((float)font.getSize()*0.5625f)/(float)window.getHeight();

        float posX = 0f;
        float posY = 0f;
        for (int i = 0; i < this.text.length(); i++)
        {
            char c = this.text.charAt(i);
            FontBuffer.CharInfo ci = font.getCharInfo(c);
            float charWidth = ((float)ci.getWidth()*lineSize)/(float)font.getSize();
            float texStart = (float)ci.getStartX()/(float)font.getWidth();
            float texEnd = (float)(ci.getStartX()+ci.getWidth())/(float)font.getWidth();

            //left upper
            vertexes.add(posX);
            vertexes.add(posY);
            vertexes.add(0f);
            texturePoints.add(texStart);
            texturePoints.add(0f);

            //left bottom
            vertexes.add(posX);
            vertexes.add(posY - lineSize);
            vertexes.add(0f);
            texturePoints.add(texStart);
            texturePoints.add(1f);

            //right bottom
            vertexes.add(posX + charWidth);
            vertexes.add(posY - lineSize);
            vertexes.add(0f);
            texturePoints.add(texEnd);
            texturePoints.add(1f);

            //left upper
            vertexes.add(posX);
            vertexes.add(posY);
            vertexes.add(0f);
            texturePoints.add(texStart);
            texturePoints.add(0f);

            //right upper
            vertexes.add(posX + charWidth);
            vertexes.add(posY);
            vertexes.add(0f);
            texturePoints.add(texEnd);
            texturePoints.add(0f);

            //right bottom
            vertexes.add(posX + charWidth);
            vertexes.add(posY - lineSize);
            vertexes.add(0f);
            texturePoints.add(texEnd);
            texturePoints.add(1f);

            posX += charWidth;

        }

        this.vertexCount = vertexes.size();
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexes.size());
        for (Float f : vertexes)
            verticesBuffer.put(f);
        verticesBuffer.flip();

        FloatBuffer texBuffer = BufferUtils.createFloatBuffer(texturePoints.size());
        for (Float f : texturePoints)
            texBuffer.put(f);
        texBuffer.flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //texture vaos
        texVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texVboId);
        glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        //set handle to 0
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        glActiveTexture(GL_TEXTURE0);
        font.bind();

        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, this.vertexCount/3);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        freeTextMeshMemory();
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    @Override
    public Vector3f getRotation() {
        return this.rotation;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    @Override
    public Vector2f getTextureOrigin() {
        return this.textureOrigin;
    }

    @Override
    public boolean isVisible() {
        return visibility;
    }

    @Override
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public IRenderable clone() throws CloneNotSupportedException {
        return (IRenderable) super.clone();
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
