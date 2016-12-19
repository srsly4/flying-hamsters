package engine.render;

import engine.essential.IEngineLogic;
import engine.essential.Window;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class RenderLogic implements IEngineLogic {

    private int direction = 0;
    private float color = 0.0f;

    private Renderer renderer;
    private RenderItem[] renderItems;
    private RenderItem item;
    public RenderLogic() throws Exception {
    }

    public void init(Window window) throws Exception {
        renderer = new Renderer();
        renderer.init(window);

        float[] positions = new float[]{
                -0.25f,  0.25f,  0,
                -0.25f, -0.25f,  0,
                0.25f, -0.25f,  0
        };
        VertexBuffer vbuff = new VertexBuffer(positions);
        item = new RenderItem(vbuff);
        item.setPosition(-0.2f, 0.02f);
        item.setScale(1f);
        item.setRotation(45);

        RenderItem item2 = new RenderItem(vbuff);
        item2.setPosition(-0.75f, 0.5f);
        item2.setScale(0.5f);
        item2.setRotation(90f);

        renderItems = new RenderItem[]{item, item2};
    }

    private final Vector3f rMove = new Vector3f(0.01f, 0, 0);
    private final Vector3f lMove = new Vector3f(-0.01f, 0, 0);

    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT))
            item.addRotation(3f, 2f, 1.5f);
        if (window.isKeyPressed(GLFW_KEY_LEFT))
            item.addRotation(-3f, -2f, -1.5f);
    }

    private float ups_acc = 0.0f;
    private float max_ups = 0.0f;
    private int ups_count = 0;


    public void update(float interval) {
//        ups_acc += interval;
        max_ups = Math.max(max_ups, interval);
//        ups_count++;
//
//        if (ups_acc >= 1.0f)
//        {
//            System.out.println("UPS: " + ups_count + ", max time-frame: " + (max_ups*1000.0) + "ms");
//            max_ups = 0.0f;
//            ups_count = 0;
//            ups_acc -= 1.0f;
//        }


        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }

    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(renderItems, window);


    }

    public void cleanup(){
        renderer.cleanup();
    }
}
