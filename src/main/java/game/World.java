package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.ArrayList;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class World implements IGameObject {
    public final float cameraWidth = 1600f;
    public final float cameraHeight = 900f;

    public final StaticSprite grass;

    public World() throws EngineException {
        grass = new StaticSprite("/sprites/grass.xml");
        grass.setPosition(0, -0.5f);
    }

    @Override
    public void update(float interval) {

    }

    @Override
    public ArrayList<IRenderable> getRenderables() {
        ArrayList<IRenderable> rlist = new ArrayList<>();
        rlist.add(grass);
        return rlist;
    }
}
