package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.ArrayList;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class Hamster implements IGameObject {
    private float xPos = 200;
    private float yPos = 600;
    private float xVel = 5f;
    private float yVel = 0;
    private boolean fly = false;

    private final StaticSprite sprite;

    public Hamster() throws EngineException {
        sprite = new StaticSprite("/sprites/hamster.xml");
    }

    public void setFly(boolean fly)
    {
        this.fly = fly;
    }

    @Override
    public void update(float interval) {
        if (this.fly){
            yVel += 100f*interval;
        }
        else {
            yVel -= 50f*interval;
        }

        yPos += 10*yVel*interval;
        //xPos += 10*xVel*interval;
        if (yPos <= 50f)
        {
            yPos = 50f;
            yVel = 0;
        }


        sprite.setRotation((float)Math.toDegrees(Math.atan2(yVel, 10*xVel)));
        sprite.setPosition(World.worldCoordsToRender(xPos, yPos));
    }

    @Override
    public ArrayList<IRenderable> getRenderables() {
        ArrayList<IRenderable> rlist = new ArrayList<>();
        rlist.add(sprite);
        return rlist;
    }
}
