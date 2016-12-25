package game.grabbables;

import engine.EngineException;
import engine.render.IRenderable;
import game.Hamster;
import game.ICollidable;
import game.IGameObject;
import game.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sirius on 22.12.2016.
 */
public class GrabbableManager implements IGameObject {

    private final Hamster hamster;
    private List<ICollidable> collidables;
    private List<GrabbableWatcher> watchers;
    private final World world;

    public GrabbableManager(Hamster hamster) throws EngineException{
        GrabbableFactory.initialize();
        this.hamster = hamster;
        world = World.getInstance();

        watchers = new LinkedList<>();
        watchers.add(new GrabbableWatcher("rocket", 2000f, 1500f, 800f));
        watchers.add(new GrabbableWatcher("rocket", 4250f, 1750f, 1000f));
        watchers.add(new GrabbableWatcher("rocket", 3100f, 2222f, 1800f));
        watchers.add(new GrabbableWatcher("rocket", 2050f, 1877f, 2100f));
        watchers.add(new GrabbableWatcher("wind", 2235f, 3000f, 680f));
        watchers.add(new GrabbableWatcher("wind", 2100f, 1700f, 1500f));
        watchers.add(new GrabbableWatcher("wind", 1760f, 1400f, 2800f));
        watchers.add(new GrabbableWatcher("rebound", 3670f, 2000f, Hamster.groundPos));
        watchers.add(new GrabbableWatcher("rebound", 3120f, 4500f, Hamster.groundPos));

        collidables = new ArrayList<>();
    }

    public void reset(){
        for (GrabbableWatcher w : watchers)
            w.reset();
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
