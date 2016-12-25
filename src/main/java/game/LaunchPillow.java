package game;

import engine.EngineException;
import engine.Event;
import engine.EventManager;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.List;

/**
 * Created by Sirius on 25.12.2016.
 */
public class LaunchPillow implements IGameObject {

    private final StaticSprite pillow;
    private final Hamster hamster;
    private final World world;

    private boolean isLaunched = false;

    private float height;

    private final static float pillowX = -150f;
    private final static float pillowY = 450f;
    private final static float launchedPillowX = 0f;
    private final static float launchBoost = 2000f;
    public LaunchPillow(Hamster hamster) throws EngineException{
        this.hamster = hamster;
        this.pillow = new StaticSprite("/sprites/pillow.xml");
        this.world = World.getInstance();
        height = World.renderHeightToWorld(this.pillow.getSpriteHeight());
    }

    @Override
    public void update(float interval) {
        pillow.setPosition(world.xPositionToRender(isLaunched ? launchedPillowX : pillowX), world.yPositionToRender(pillowY));
        if (hamster.getState() == HamsterState.Launched)
        {
            pillow.setRotation(180f-1f*(float) Math.toDegrees(Math.atan2(hamster.getyPos()-pillowY, pillowX)));
        }
        else pillow.setRotation(0);
    }

    public boolean isLaunched(){
        return isLaunched;
    }

    public void launch(){
        isLaunched = true;
        float delta = hamster.getyPos() - pillowY;
        if (delta < height && delta > -height)
        {
            //calculate angle from delta
            float angle = (float)Math.asin(2*delta/height);
            float velX = launchBoost*(float)Math.cos(angle);
            float velY = launchBoost*(float)Math.sin(angle);

            //launch hamster
            hamster.setState(HamsterState.InAir);
            hamster.setVelXY(velX, velY);
        }

        EventManager.getInstance().addEvent(new Event(1f, ()->{
            isLaunched = false;
            return null;
        }));

    }

    public void reset(){
        isLaunched = false;
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(pillow);
    }

    @Override
    public void cleanUp() {
        pillow.cleanUp();
    }
}
