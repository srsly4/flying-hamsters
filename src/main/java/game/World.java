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
    private float yPos = 0; //from bottom;
    private float grassXPos = 0;

    public final StaticSprite background;
    public final StaticSprite grass;

    public World() throws EngineException {
        background = new StaticSprite("/sprites/background.xml");

        grass = new StaticSprite("/sprites/grass.xml");
        grass.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f, 100f));
    }

    public void setXPos(float x){
        this.xPos = x;
    }

    public void setYPos(float y){
        this.yPos = y;
    }

    @Override
    public void update(float interval) {
        grassXPos = xPos/World.cameraWidth;
        while (grassXPos > 1f)
            grassXPos -= 1f;
        grass.setTextureOrigin(grassXPos, 0);

        //vertical position
        grass.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f, 100f - yPos));
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
