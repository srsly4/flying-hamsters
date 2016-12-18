package engine.render;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Renderer {
    public Renderer() {
    }

    public void init() throws Exception {

    }

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
