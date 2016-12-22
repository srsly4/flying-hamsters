package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirius on 21.12.2016.
 */
public class HamsterShadow implements IGameObject {

    private final World world;
    private final Hamster hamster;
    public final StaticSprite shadow;

    public HamsterShadow(World world, Hamster hamster) throws EngineException {
        this.world = world;
        this.hamster = hamster;
        shadow = new StaticSprite("/sprites/shadow.xml");
    }

    @Override
    public void update(float interval) {
        if (hamster.getyPos() > 0.5f*World.cameraHeight)
        {
            shadow.setPosition(World.worldCoordsToRender(200f, 100f - hamster.getyPos() + 0.5f*World.cameraHeight));
        }
        else
        {
            shadow.setPosition(World.worldCoordsToRender(200f, 100f));
        }
        shadow.setScale(Math.max(0.1f, 100.0f/((hamster.getyPos()/2.0f)+50f  )  ));
    }

    @Override
    public void cleanUp() {
        shadow.cleanUp();
    }

    @Override
    public void updateRenderables(final List<IRenderable> renderables) {
        renderables.add(shadow);
    }
}
