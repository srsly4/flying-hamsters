package engine.render;

import engine.EngineException;
import engine.EngineResourceException;
import engine.ObjectParser;
import engine.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;



/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class StaticSprite implements IRenderable, Cloneable {
    protected Vector3f position;
    protected Vector3f rotation;
    protected float scale;
    protected Vector2f textureOrigin;
    protected float spriteWidth;
    protected float spriteHeight;
    protected boolean visibility = true;

    private Texture texture;
    private int vaoId;
    private int vboId;
    private int texVboId;
    private int vertexCount;

    protected StaticSprite(){
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = 1f;
        textureOrigin = new Vector2f(0, 0);
    }

    public StaticSprite(String resourceName) throws EngineException{
        this();

        ObjectParser parser = ObjectParser.getInstance();
        parser.openResource(resourceName);
        float spriteWidth = parser.getFloatValue("width");
        float spriteHeight = parser.getFloatValue("height");
        String texUri = parser.getStringValue("texture");
        float texWidth = parser.getFloatValue("textureWidth");
        float texHeight = parser.getFloatValue("textureHeight");
        int repeatX = parser.getIntValue("textureRepeatX");
        int repeatY = parser.getIntValue("textureRepeatY");

        float texLongest = Math.max(texWidth, texHeight);
        float wTexWidth = (texWidth/texLongest)*(float)repeatX;
        float wTexHeight = (texHeight/texLongest)*(float)repeatY;
        float[] texCoords = new float[]{
                0, 0, //lewy gorny
                0, wTexHeight, //lewy dolny
                wTexWidth, wTexHeight, //prawy dolny
                0f, 0f, //lewy gorny
                wTexWidth, 0f, //prawy gorny
                wTexWidth, wTexHeight //prawy dolny
        };

        initObject(texUri, spriteWidth, spriteHeight, texCoords);
    }

    protected void initObject(String texUri, float spriteWidth, float spriteHeight, float[] texCoords)
    throws EngineException{
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;

        texture = new Texture(texUri);

        float[] points = new float[]{
                -spriteWidth,  spriteHeight,  0,
                -spriteWidth, -spriteHeight,  0,
                spriteWidth, -spriteHeight,  0,
                -spriteWidth, spriteHeight, 0,
                spriteWidth, spriteHeight, 0,
                spriteWidth, -spriteHeight, 0
        };
        this.vertexCount = points.length/3;

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
        FloatBuffer texBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texBuffer.put(texCoords);
        texBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, texVboId);
        glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        //set handle to 0
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        StaticSprite cln = (StaticSprite)super.clone();
        cln.position = new Vector3f(0, 0, 0);
        cln.rotation = new Vector3f(0, 0, 0);
        cln.scale = 1f;
        cln.textureOrigin = new Vector2f(0, 0);
        return cln;
    }

    @Override
    public void render() {
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, this.vertexCount);
//        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(texVboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public Vector3f getPosition() { return position; }
    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }
    public void setPosition(Vector2f v){
        this.position.x = v.x;
        this.position.y = v.y;
    }
    public float getScale() {
        return scale;
    }
    public void setScale(float factor){
        scale = factor;
    }
    public void setRotation(float z){
        this.rotation.z = z;
    }
    public Vector3f getRotation(){ return this.rotation; }
    public Vector2f getTextureOrigin(){
        return textureOrigin;
    }

    @Override
    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setTextureOrigin(float x, float y){
        this.textureOrigin.x = x;
        this.textureOrigin.y = y;
    }
    public float getSpriteWidth() {
        return spriteWidth;
    }

    public float getSpriteHeight() {
        return spriteHeight;
    }
}
