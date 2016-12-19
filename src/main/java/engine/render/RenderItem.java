package engine.render;

import engine.EngineException;
import engine.Utils;
import org.joml.Vector3f;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class RenderItem {
    private final VertexBuffer vertex;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public RenderItem(VertexBuffer vertexes) throws EngineException{
        this.vertex = vertexes;
        position = new Vector3f(0f, 0f, 0f);
        scale = 1f;
        rotation = new Vector3f(0f, 0f, 0f);
    }

    public Vector3f getPosition() { return position; }
    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }
    public void setPosition(float x, float y, float z){
        setPosition(x, y);
        this.position.z = z;
    }

    public void addPosition(Vector3f dvec){
        this.position.add(dvec);
    }

    public float getScale() {
        return scale;
    }
    public void setScale(float factor){
        scale = factor;
    }

    public Vector3f getRotation() { return rotation; }
    public void setRotation(float z){
        this.rotation.z = z;
    }
    public void setRotation(float x, float y, float z){
        setRotation(z);
        this.rotation.x = x;
        this.rotation.y = y;
    }
    public void addRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }
    public VertexBuffer getVertexBuffer(){ return vertex; }


}
