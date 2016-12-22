package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class Hamster implements IGameObject, ICollidable {
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

    private boolean grounded = false;

    public static final float groundPos = 100f;
    public static float maxYVel = 500f;
    public Hamster(World world) throws EngineException {
        sprite = new StaticSprite("/sprites/hamster.xml");
        sprite.setVisibility(false);
        this.world = world;
    }

    @Override
    public void cleanUp() {
        sprite.cleanUp();
    }

    public void setPosition(float x, float y){
        this.xPos = x;
        this.yPos = y;
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
                yVel += 500f*interval;
                xVel += 200f*interval;
            }
            else {
                yVel -= 300f*interval;
            }

            //air force
            if (xVel <= 0.0f)
                xVel = 0.0f;
            else
                xVel -= interval*0.001*(xVel*xVel);

            yVel = Math.min(yVel, maxYVel);

            yPos += yVel*interval;
            xPos += xVel*interval;

            float angle = (float)Math.toDegrees(Math.atan2(yVel, xVel));

            //hamster touched the ground
            if (yPos <= groundPos)
            {
                if (!grounded && xVel > 100f && Math.abs(angle) < 60f) //jump off the ground
                {
                    yVel = -yVel/2f;
                    xVel = 0.75f*xVel;
                    //we have to jump off the ground now
                    yPos += yVel*interval;
                    xPos += xVel*interval;
                }
                else {
                    grounded = true;
                    yPos = groundPos;
                    yVel = 0;
                    xVel = 0;
                }
            }

            if (yPos < 0.5f*World.cameraHeight){
                realYPos = yPos;
                highestCameraY = yPos;
            }
            else {
                realYPos = 0.5f*World.cameraHeight;

//                highestCameraY = Math.max(highestCameraY, yPos);
//                float delta = highestCameraY-yPos;
//                if (delta > 0.5f*World.cameraHeight)
//                    highestCameraY -= delta - 0.5f*World.cameraHeight;
//                realYPos = 0.75f*World.cameraHeight-delta;
////                realYPos = 0.75f*World.cameraHeight;
//
//                //falling down
//                if (yPos < 1.25f*World.cameraHeight) // 0.75 < yPos < 1.25
//                {
//                    if (highestCameraY - yPos >= 0.5f*World.cameraHeight) //we're falling from far top
//                    {
//                        realYPos += (1.25f*World.cameraHeight-yPos);
//                    }
//                    else {
//                        realYPos = 0.75f*World.cameraHeight;
//                    }
//
//                }
            }

            world.setYPos(yPos);
            sprite.setRotation(angle);
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
    public void updateRenderables(final List<IRenderable> renderables) {
        renderables.add(sprite);
    }

    @Override
    public float getLeftBorder() {
        return World.worldXCoordToRender(xPos)-(sprite.getSpriteWidth()/2f);
    }

    @Override
    public float getRightBorder() {
        return World.worldXCoordToRender(xPos)+(sprite.getSpriteWidth()/2f);
    }

    @Override
    public float getTopBorder() {
        return World.worldYCoordToRender(yPos)-(sprite.getSpriteHeight()/2f);
    }

    @Override
    public float getBottomBorder() {
        return World.worldYCoordToRender(yPos)+(sprite.getSpriteHeight()/2f);
    }
}
