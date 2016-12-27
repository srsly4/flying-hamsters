package game;

import engine.EngineException;
import engine.render.AnimatedSprite;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import game.grabbables.Grabbable;

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
    private float realYPos = 0f;
    private float currentSpeed = 0f;
    private int customAcceleration = 0;
    private float currentAngle = 0f;
    private float animAngle = 0f;
    private boolean fly = false;
    private float flightStrength = 100f;
    private HamsterState state;

    private StaticSprite sprite;
    private final AnimatedSprite flightSprite;
    private final StaticSprite tossSprite;
    private final StaticSprite readySprite;
    private final StaticSprite landedSprite;
    private final World world;

    private float width;
    private float height;

    public static final float groundPos = 100f;
    public static float maxYVel = 500f;
    public Hamster() throws EngineException {
        sprite = flightSprite = new AnimatedSprite("/sprites/hamster.xml");
        tossSprite = new StaticSprite("/sprites/hamster_toss.xml");
        readySprite = new StaticSprite("/sprites/hamster_ready.xml");
        landedSprite = new StaticSprite("/sprites/hamster_landed.xml");
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
        if (state == HamsterState.BeforeLaunch)
            sprite = this.readySprite;
        if (state == HamsterState.InAir)
            sprite = this.flightSprite;
        if (state == HamsterState.Launched)
            sprite = this.tossSprite;
        if (state == HamsterState.Grounded) {
            sprite = this.landedSprite;
            currentAngle  = 0f;
        }
        width = World.renderWidthToWorld(sprite.getSpriteWidth());
        height = World.renderHeightToWorld(sprite.getSpriteHeight());
        sprite.setPosition(world.xPositionToRender(xPos), World.worldYCoordToRender(realYPos));
    }

    @Override
    public void update(float interval) {
        //flight strength
        if (!fly && flightStrength < 100f)
            flightStrength += interval*20f; //5 sec to fullfill
        if (fly) {
            flightStrength -= interval * 100f; // 1 sec to use whole
            if (flightStrength <= 0f)
            {
                fly = false;
                flightStrength = 0f;
            }
        }
        if (flightStrength > 100f)
            flightStrength = 100f;

        if (this.state == HamsterState.BeforeLaunch)
        {
            xVel = 0;
            yVel = 0;
            sprite.setRotation(0);
        }
        if (this.state == HamsterState.Launched) {
            //only y coordinates change
            xVel = 0;
            yVel -= 800f * interval;

            yPos += yVel * interval;

            if (yPos <= groundPos) {
                setState(HamsterState.Grounded); //bad timing ;)
                yVel = 0f;
                yPos = groundPos;
            }

            sprite.setRotation(0);
            sprite.setRotation(animAngle);
            animAngle += interval * 2000f;
            if (animAngle > 360f)
                animAngle -= 360f;
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
                if (this.fly && this.flightStrength > 0f){
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
                    yVel = -yVel/4f;
                    xVel = 0.75f*xVel;
                    //we have to jump off the ground now
                    yPos += yVel*interval;
                    xPos += xVel*interval;
                }
                else {
                    setState(HamsterState.Grounded);
                    yPos = groundPos;
                    yVel = 0;
                    xVel = 0;
                }
            }
            //rotate with proper direction
            sprite.setRotation(currentAngle);

        }

        //camera
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

        currentSpeed = (float)Math.sqrt((xVel*xVel)+(yVel*yVel));

        if (sprite == flightSprite) {
            flightSprite.update(interval);
            flightSprite.setFps(
                    Math.max(4f, Math.min(60f, 40f*currentSpeed/3000f))
            );
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

    public float getFlightStrength() {
        return flightStrength;
    }

    public void setFlightStrength(float flightStrength) {
        this.flightStrength = flightStrength;
    }
}
