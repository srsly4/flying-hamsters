package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class World implements IGameObject {
    public final static float cameraWidth = 1600f;
    public final static float cameraHeight = 900f;

    private float xPos = 0;
    private float grassXPos = 0;

    public final StaticSprite background;
    public final StaticSprite grass;

    public World() throws EngineException {
        background = new StaticSprite("/sprites/background.xml");

        grass = new StaticSprite("/sprites/grass.xml");
        grass.setPosition(0, -0.5f);
    }

    @Override
    public void update(float interval) {
        grassXPos += 0.5f*interval;
        if (grassXPos > 1f)
            grassXPos -= 1f;
        grass.setTextureOrigin(grassXPos, 0);
    }

    @Override
    public ArrayList<IRenderable> getRenderables() {
        ArrayList<IRenderable> rlist = new ArrayList<>();
        rlist.add(background);
        rlist.add(grass);
        return rlist;
    }

    private final static Vector2f coords = new Vector2f();
    public static Vector2f worldCoordsToRender(float x, float y){
        //x, y are in-world values
        //x is from left to right (0+)
        //y is from down to up (0+)
        x /= World.cameraWidth;
        x *= 2f;
        x -= 1.0f;
        y /= World.cameraHeight;
        y *= 2f;
        y -= 1.0f;
        y *= 0.5625f;
        return World.coords.set(x, y);
    }
}
