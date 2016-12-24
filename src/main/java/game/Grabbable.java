package game;

import engine.render.IRenderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirius on 24.12.2016.
 */
public abstract class Grabbable implements IGameObject, ICollidable {

    private IRenderable renderable;
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

    public Grabbable(IRenderable renderableInstance, float xPos, float yPos){
        renderable = renderableInstance;
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
    };

    @Override
    public void update(float interval) {
        renderable.setPosition(world.xPositionToRender(xPos), world.yPositionToRender(yPos));
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(renderable);
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
        topBorder = yPos - (height/2f);
        bottomBorder = yPos + (height/2f);

    }

    @Override
    public void collidedWith(ICollidable collidableObject) {
        //we don't expect bonuses to collide with hamster :)
    }
}
