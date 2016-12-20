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
    private float realXPos = 200f;
    private float realYPos = 0f;
    private float highestCameraY = 0f;
    private boolean fly = false;
    private boolean inAir = false;
    private final StaticSprite sprite;
    private final World world;


    public Hamster(World world) throws EngineException {
        sprite = new StaticSprite("/sprites/hamster.xml");
        sprite.setVisibility(false);
        this.world = world;
    }

    public void setFly(boolean fly)
    {
        this.fly = fly;
    }

    public void setInAir(boolean inAir){
        this.inAir = inAir;
        this.sprite.setVisibility(inAir);
    }

    public void setVelXY(float x, float y)
    {
        this.xVel = x;
        this.yVel = y;
    }

    @Override
    public void update(float interval) {
        if (this.inAir)
        {
            if (this.fly){
                yVel += 100f*interval;
                xVel += 20f*interval;
            }
            else {
                yVel -= 90f*interval;
                xVel -= 5f*interval;
            }

            //air force
            if (xVel <= 0.0f)
                xVel = 0.0f;
            else
                xVel -= interval*0.001*(xVel*xVel);

            yPos += 10*yVel*interval;
            xPos += 10*xVel*interval;
            if (yPos <= 50f)
            {
                yPos = 50f;
                yVel = 0;
                xVel = 0;
            }

            if (yPos < 0.75f*World.cameraHeight){
                realYPos = yPos;
                highestCameraY = yPos;
                world.setYPos(0);
            }
            else {
                highestCameraY = Math.max(highestCameraY, yPos);
                float delta = highestCameraY-yPos;
                if (delta > 0.5f*World.cameraHeight)
                    highestCameraY -= delta - 0.5f*World.cameraHeight;
                realYPos = 0.75f*World.cameraHeight-delta;
//                realYPos = 0.75f*World.cameraHeight;

                //falling down
                if (yPos < 1.25f*World.cameraHeight) // 0.75 < yPos < 1.25
                {
                    if (highestCameraY - yPos >= 0.5f*World.cameraHeight) //we're falling from far top
                    {
                        realYPos += (1.25f*World.cameraHeight-yPos);
                    }
                    else {
                        realYPos = 0.75f*World.cameraHeight;
                    }

                }
                world.setYPos(yPos - 0.75f*World.cameraHeight);
            }

            sprite.setRotation((float)Math.toDegrees(Math.atan2(yVel, 2*xVel)));
            sprite.setPosition(World.worldCoordsToRender(realXPos, realYPos));
        }

    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    @Override
    public ArrayList<IRenderable> getRenderables() {
        ArrayList<IRenderable> rlist = new ArrayList<>();
        rlist.add(sprite);
        return rlist;
    }
}
