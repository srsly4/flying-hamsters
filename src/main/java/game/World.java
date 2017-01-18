package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class World implements IGameObject {
    public final static float cameraWidth = 1600f;
    public final static float cameraHeight = 900f;

    private float xPos = 0;
    private float yPos = 0; //from bottom;
    private float grassXPos = 0;
    private float backgroundXPos = 0;
    private float backgroundCloserXPos = 0;

    public final StaticSprite background;
//    public final StaticSprite movingBackground;
    public final StaticSprite grass;

    private float movingBackgroundAttachment;
    private World() throws EngineException {
        background = new StaticSprite("/sprites/background.xml");
        background.setDepth(-0.5f);
//        movingBackground = new StaticSprite("/sprites/mov_background.xml");
//        movingBackgroundAttachment = -0.6f+movingBackground.getSpriteHeight();
//        movingBackground.setPosition(0, movingBackgroundAttachment);
//        movingBackground.setDepth(-0.47f);
        grass = new StaticSprite("/sprites/ground.xml");
        grass.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f, 100f));
        grass.setDepth(-0.46f);
    }

    private static World instance;
    public static World initializeInstance() throws EngineException{
        instance = new World();
        return instance;
    }
    public static World getInstance() {
        return instance;
    }


    public void setXPos(float x){
        this.xPos = x;
    }

    public void setYPos(float y){
        this.yPos = y;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    @Override
    public void update(float interval) {
        grassXPos = (xPos/World.cameraWidth);
        backgroundCloserXPos = grassXPos*0.15f;
        backgroundXPos = grassXPos*0.05f;
        while (grassXPos > 1f)
            grassXPos -= 1f;
        while (backgroundXPos > 1f)
            backgroundXPos -= 1f;
        while (backgroundCloserXPos > 1f)
            backgroundCloserXPos -= 1f;
        grass.setTextureOrigin(grassXPos, 0);
        background.setTextureOrigin(backgroundXPos, 0);
//        movingBackground.setTextureOrigin(backgroundCloserXPos, 0);
        //vertical grass positioning
        if (yPos > 0.5f*World.cameraHeight)
        {
            grass.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f, 100f - yPos + 0.5f*World.cameraHeight));
        }
        else
        {
            grass.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f, 100f));
        }


//        movingBackground.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f,
//                200f-(yPos/20.0f)));
        background.setPosition(World.worldCoordsToRender(World.cameraWidth/2.0f,
                Math.min(((World.cameraHeight/2f)-0.125f*(yPos-0.5f*World.cameraHeight)), World.cameraHeight/2f)));

    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(background);
//        renderables.add(movingBackground);
        renderables.add(grass);
    }

    @Override
    public void cleanUp() {
        background.cleanUp();
        grass.cleanUp();
    }

    private final static Vector2f coords = new Vector2f();

    public float xPositionToRender(float x){
        return worldXCoordToRender(x-xPos);
    }

    public float yPositionToRender(float y){
        if (yPos > 0.5f*World.cameraHeight)
        {
            return worldYCoordToRender(0.5f*World.cameraHeight+y-yPos);
        }
        else
        {
            return worldYCoordToRender(y);
        }
    }

    public static float worldXCoordToRender(float x){
        return (2f*x/World.cameraWidth) -1f;
    }
    public static float worldYCoordToRender(float y){
        return 0.5625f*((2f*y/World.cameraHeight)-1f);
    }
    public static Vector2f worldCoordsToRender(float x, float y){
        //x, y are in-world values
        //x is from left to right (0+)
        //y is from down to up (0+)
        return World.coords.set(worldXCoordToRender(x), worldYCoordToRender(y));
    }

    public static float renderWidthToWorld(float width){
        return width*World.cameraWidth;
    }
    public static float renderHeightToWorld(float height){
        return height*World.cameraWidth;
    }
}
