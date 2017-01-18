package game.grabbables;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import game.Hamster;
import game.ICollidable;
import game.IGameObject;
import game.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirius on 24.12.2016.
 */
public abstract class Grabbable implements IGameObject, ICollidable {

    protected IRenderable renderable;
    protected float xPos;
    protected float yPos;
    protected float width = 50f;
    protected float height = 50f;
    protected World world;
    protected boolean isUsed;

    private float leftBorder;
    private float rightBorder;
    private float topBorder;
    private float bottomBorder;

    private boolean markerOn = false;
    private StaticSprite tlMarker, trMarker, blMarker, brMarker;

    public Grabbable(IRenderable renderableInstance, float xPos, float yPos){
        renderable = renderableInstance;

        //try to estimate width and height
        if (renderable instanceof StaticSprite){
            StaticSprite sprite = (StaticSprite)renderable;
            width = World.renderWidthToWorld(sprite.getSpriteWidth());
            height = World.renderHeightToWorld(sprite.getSpriteHeight());
        }
        this.setPosition(xPos, yPos);
        world = World.getInstance();
        isUsed = false;

    }

    public boolean isUsed(){
        return isUsed;
    }

    public void executeOn(Hamster hamster) {
        this.isUsed = true;
        this.renderable.setVisibility(false);
    }

    public void setMarkers(final StaticSprite pattern){
        try {
            tlMarker = (StaticSprite) pattern.clone();
            trMarker = (StaticSprite) pattern.clone();
            blMarker = (StaticSprite) pattern.clone();
            brMarker = (StaticSprite) pattern.clone();
            markerOn = true;
        }
        catch (CloneNotSupportedException e)
        {
            //not possible
        }
    }

    @Override
    public void update(float interval) {
        renderable.setPosition(world.xPositionToRender(xPos), world.yPositionToRender(yPos));
        if (markerOn){
            tlMarker.setPosition(
                    world.xPositionToRender(this.getLeftBorder()),
                    world.yPositionToRender(this.getTopBorder()));
            trMarker.setPosition(
                    world.xPositionToRender(this.getRightBorder()),
                    world.yPositionToRender(this.getTopBorder()));
            blMarker.setPosition(
                    world.xPositionToRender(this.getLeftBorder()),
                    world.yPositionToRender(this.getBottomBorder()));
            brMarker.setPosition(
                    world.xPositionToRender(this.getRightBorder()),
                    world.yPositionToRender(this.getBottomBorder()));
        }
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(renderable);
        if (markerOn){
            renderables.add(tlMarker);
            renderables.add(trMarker);
            renderables.add(blMarker);
            renderables.add(brMarker);
        }
    }

    @Override
    public void cleanUp() {
        renderable.cleanUp();
    }

    @Override
    public float getLeftBorder() {
        return leftBorder;
    }

    @Override
    public float getRightBorder() {
        return rightBorder;
    }

    @Override
    public float getTopBorder() {
        return topBorder;
    }

    @Override
    public float getBottomBorder() {
        return bottomBorder;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setPosition(float x, float y){
        this.xPos = x;
        this.yPos = y;

        //cache calculated border for performance
        leftBorder = xPos - (width/2f);
        rightBorder = xPos + (width/2f);
        topBorder = yPos + (height/2f);
        bottomBorder = yPos - (height/2f);

    }

    @Override
    public void collidedWith(ICollidable collidableObject) {
        //we don't expect bonuses to collide with hamster :)
    }
}
