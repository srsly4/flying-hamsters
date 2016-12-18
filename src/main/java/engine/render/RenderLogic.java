package engine.render;

import engine.essential.IEngineLogic;
import engine.essential.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class RenderLogic implements IEngineLogic {

    private int direction = 0;
    private float color = 0.0f;

    private Renderer renderer;

    public RenderLogic() {
        renderer = new Renderer();
    }

    public void init() throws Exception {
        renderer.init();
    }

    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    private float ups_acc = 0.0f;
    private float max_ups = 0.0f;
    private int ups_count = 0;

    private float xpos = 50f;
    private float ypos = 50f;

    public void update(float interval) {
        ups_acc += interval;
        max_ups = Math.max(max_ups, interval);
        ups_count++;

        if (ups_acc >= 1.0f)
        {
            System.out.println("UPS: " + ups_count + ", max time-frame: " + (max_ups*1000.0) + "ms");
            max_ups = 0.0f;
            ups_count = 0;
            ups_acc -= 1.0f;
        }

        xpos += 3f;
        ypos += 1f;

        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }

    public void render(Window window) {
        glColor3f(0.8f, 0.8f, 0);
        glRectf(xpos, ypos, xpos+50, ypos+50);
        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();


    }
}
