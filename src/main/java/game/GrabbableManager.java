package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.awt.*;

/**
 * Created by Sirius on 22.12.2016.
 */
public class GrabbableManager implements IGameObject {

    private final Hamster hamster;
    private List<ICollidable> collidables;
    private List<GrabbableWatcher> watchers;
    private final World world;

//    private StaticSprite tlMarker, trMarker, blMarker, brMarker;

    public GrabbableManager(Hamster hamster) throws EngineException{
        GrabbableFactory.initialize();
        this.hamster = hamster;
        world = World.getInstance();

        watchers = new LinkedList<>();
        watchers.add(new GrabbableWatcher("rocket", 2000f, 1500f, 600f));
        watchers.add(new GrabbableWatcher("rocket", 4250f, 1750f, 800f));
        watchers.add(new GrabbableWatcher("wind", 835f, 2000f, 380f));

        collidables = new ArrayList<>();
    }

    @Override
    public void update(float interval) {
        collidables.clear();
        for (GrabbableWatcher gw : watchers) {
            gw.process(interval);
            gw.updateCollidables(collidables);
        }

        processCollidables(hamster, collidables);
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        for (GrabbableWatcher gw : watchers) {
            gw.updateRenderables(renderables);
        }
    }


    public static void processCollidables(ICollidable subject, List<ICollidable> cases)
    {
        for (ICollidable col : cases){
            if ( col.getLeftBorder() < subject.getRightBorder() &&
                    col.getRightBorder() > subject.getLeftBorder() &&
                    col.getTopBorder() > subject.getBottomBorder() &&
                    col.getBottomBorder() < subject.getTopBorder())
            {
                subject.collidedWith(col);
            }
        }
    }
}
