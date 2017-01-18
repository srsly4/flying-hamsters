package game.grabbables;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import game.ICollidable;
import game.World;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sirius on 25.12.2016.
 */
public class GrabbableWatcher {

    private final World world;
    private Deque<Grabbable> series;
    private float nextX;
    private String grabbableType;
    private float xPosModulo;
    private float xPosStart;
    private float yPos;

    private StaticSprite marker;

    public GrabbableWatcher(String grabbableType, float xPosModulo, float xPosStart, float yPos)
    {
        world = World.getInstance();
        series = new LinkedList<>();
        this.grabbableType = grabbableType;
        this.xPosModulo = xPosModulo;
        this.xPosStart = xPosStart;
        this.yPos = yPos;

        nextX = xPosStart;

    }

    /**
     * Clears all the grabbables from watcher and reset position to start
     */
    public void reset(){
        nextX = xPosStart;
        series.clear();
    }

    /**
     * Removes series grabbables two World.cameraWidth behind current position
     * and creates object two World.cameraWidth after current position.
     * Also calls update on all left Grabbables
     */
    public void process(float interval){
        while (nextX < world.getxPos()+2f*World.cameraWidth)
        {
            series.addLast(GrabbableFactory.createGrabbable(grabbableType, nextX, yPos));
//            series.peekLast().setMarkers(marker);
            nextX += xPosModulo;
        }

        while (series.peekFirst() != null && series.peekFirst().getxPos() < (world.getxPos() - World.cameraWidth))
        {
            series.pollFirst();
        }




        for (Grabbable g : series)
            g.update(interval);
    }

    public void updateCollidables(List<ICollidable> collidables){
        collidables.addAll(series);
    }

    public void updateRenderables(List<IRenderable> renderables){
        for (Grabbable g : series){
            g.updateRenderables(renderables);
        }
    }
}
