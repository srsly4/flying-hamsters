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
        loadWatchers();
        collidables = new ArrayList<>();
    }

    private void loadWatchers(){
        watchers.clear();
        watchers.add(new GrabbableWatcher("rocket", randomInterval(2100, 1000), 1500f, 800f));
        watchers.add(new GrabbableWatcher("rocket", randomInterval(2200, 2000), 1750f, 1000f));
        watchers.add(new GrabbableWatcher("rocket", randomInterval(3200, 2000), 2222f, 1800f));
        watchers.add(new GrabbableWatcher("rocket", randomInterval(2100, 1000), 1877f, 2100f));
        watchers.add(new GrabbableWatcher("rocket", randomInterval(3200, 2500), 1200f, 3400f));
        watchers.add(new GrabbableWatcher("rocket", randomInterval(2200, 2000), 1800f, 3900f));
        watchers.add(new GrabbableWatcher("wind", randomInterval(2100, 1000), 1700f, 1500f));
        watchers.add(new GrabbableWatcher("wind", randomInterval(2300, 2000), 1400f, 2800f));
        watchers.add(new GrabbableWatcher("wind", randomInterval(2200, 2000), 1200f, 4300f));
        watchers.add(new GrabbableWatcher("rebound", randomInterval(3500, 3000), 2000f, Hamster.groundPos));
        watchers.add(new GrabbableWatcher("rebound", randomInterval(3500, 5000), 4500f, Hamster.groundPos));
        watchers.add(new GrabbableWatcher("bounce", randomInterval(2500, 2000), 2300f, 1400f));
        watchers.add(new GrabbableWatcher("bounce", randomInterval(3700, 2000), 1450f, 3100f));
        watchers.add(new GrabbableWatcher("bounce", randomInterval(2500, 2000), 1650f, 2250f));
        watchers.add(new GrabbableWatcher("super_bounce", randomInterval(4000, 2000), 3450f, 2600f));
        watchers.add(new GrabbableWatcher("super_bounce", randomInterval(4000, 2000), 2750f, 3600f));

        watchers.add(new GrabbableWatcher("cloud", randomInterval(2000, 750), 1000, 5600f));
        watchers.add(new GrabbableWatcher("cloud", randomInterval(2300, 1000), 1340, 6000f));
        watchers.add(new GrabbableWatcher("cloud", randomInterval(2700, 1000), 640, 5750f));
    }

    public void reset(){
        loadWatchers();
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

    private static float randomInterval(float start, float delta){
        return start + (float)Math.random()*delta;
    }
}
