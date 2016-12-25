package game;

import engine.EngineException;
import engine.render.AnimatedSprite;
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
    private float xAcc = 0f;
    private float yAcc = 0f;
    private int customAcceleration = 0;
    private float currentAngle = 0f;
    private boolean fly = false;
    private HamsterState state;


    private final AnimatedSprite sprite;
    private final World world;

    private float width;
    private float height;

    public static final float groundPos = 100f;
    public static float maxYVel = 500f;
    public Hamster() throws EngineException {
        sprite = new AnimatedSprite("/sprites/hamster.xml");
        this.world = World.getInstance();
        width = World.renderWidthToWorld(sprite.getSpriteWidth());
        height = World.renderHeightToWorld(sprite.getSpriteHeight());
        state = HamsterState.BeforeLaunch;
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


    public void setVelXY(float x, float y)
    {
        this.xVel = x;
        this.yVel = y;
    }

    public float getxVel() {
        return xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setCustomAcceleration(float xAcc, float yAcc){
        this.xAcc = xAcc;
        this.yAcc = yAcc;
        this.customAcceleration++;
    }

    public void unsetCustomAcceleration(float xAcc, float yAcc){
        this.xAcc -= xAcc;
        this.yAcc -= yAcc;
        this.customAcceleration--;
    }

    public HamsterState getState() {
        return state;
    }

    public void setState(HamsterState state) {
        this.state = state;
    }

    @Override
    public void update(float interval) {
        if (this.state == HamsterState.BeforeLaunch)
        {
            xVel = 0;
            yVel = 0;
            sprite.setRotation(0);
        }
        if (this.state == HamsterState.Launched)
        {
            //only y coordinates change
            xVel = 0;
            yVel -= 800f*interval;

            yPos += yVel*interval;

            if (yPos <= groundPos)
            {
                state = HamsterState.Grounded; //bad timing ;)
                yVel = 0f;
                yPos = groundPos;
            }

            sprite.setRotation(0);
        }

        if (this.state == HamsterState.InAir)
        {
            if (this.customAcceleration > 0)
            {
                xVel += xAcc*interval;
                yVel += yAcc*interval;
            }
            else {
                //standard air/gravity conditions
                if (this.fly){
                    yVel += 500f*interval;
                    xVel += 200f*interval;
                }
                else {
                    yVel -= 400f*interval;
                }

                //air force
                if (xVel <= 0.0f)
                    xVel = 0.0f;
                else
                    xVel -= interval*0.25f*(xVel);

//                yVel = Math.min(yVel, maxYVel); NOOOOPE!
            }

            yPos += yVel*interval;
            xPos += xVel*interval;

            float angle = (float)Math.toDegrees(Math.atan2(yVel, xVel));
            currentAngle = currentAngle + 0.25f*(angle - currentAngle);
            //hamster touched the ground
            if (yPos <= groundPos)
            {
                if (state != HamsterState.Grounded && xVel > 100f && Math.abs(angle) < 60f) //jump off the ground
                {
                    yVel = -yVel/2f;
                    xVel = 0.75f*xVel;
                    //we have to jump off the ground now
                    yPos += yVel*interval;
                    xPos += xVel*interval;
                }
                else {
                    state = HamsterState.Grounded;
                    yPos = groundPos;
                    yVel = 0;
                    xVel = 0;
                }
            }
            //rotate with proper direction
            sprite.setRotation(currentAngle);

        }

        //camera
        float realYPos;
        if (yPos < 0.5f*World.cameraHeight){
            realYPos = yPos;
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
        sprite.setPosition(world.xPositionToRender(xPos), World.worldYCoordToRender(realYPos));
        world.setYPos(yPos);

        sprite.update(interval);
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
        return xPos - (width/2f);
    }

    @Override
    public float getRightBorder() {
        return xPos+(width/2f);
    }

    @Override
    public float getTopBorder() {
        return yPos+(height/2f);
    }

    @Override
    public float getBottomBorder() {
        return yPos-(height/2f);
    }


    @Override
    public void collidedWith(ICollidable collidableObject) {
        if (collidableObject instanceof Grabbable)
        {
            Grabbable grabbable = (Grabbable)collidableObject;
            if (!grabbable.isUsed())
                grabbable.executeOn(this);
        }
    }

    public float getWorldWidth(){
        return width;
    }

    public float getWorldHeight(){
        return height;
    }
}
