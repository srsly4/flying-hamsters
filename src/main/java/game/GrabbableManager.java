package game;

import engine.EngineException;
import engine.render.IRenderable;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Created by Sirius on 22.12.2016.
 */
public class GrabbableManager implements IGameObject {

    private Grabbable test;
    private final Hamster hamster;
    private List<ICollidable> collidables;
    public GrabbableManager(Hamster hamster) throws EngineException{
        GrabbableFactory.initialize();
        test = GrabbableFactory.createRocketGrabbable(1500f, 450f);
        this.hamster = hamster;
        collidables = new ArrayList<>();
        collidables.add(test);
    }

    @Override
    public void update(float interval) {
        test.update(interval);
        processCollidables(hamster, collidables);
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        test.updateRenderables(renderables);
    }


    public static void processCollidables(ICollidable subject, List<ICollidable> cases)
    {
        for (ICollidable col : cases){
            if ( col.getLeftBorder() < subject.getRightBorder() &&
                    col.getRightBorder() > subject.getLeftBorder() &&
                    col.getTopBorder() < subject.getBottomBorder() &&
                    col.getBottomBorder() > subject.getTopBorder())
            {
                subject.collidedWith(col);
            }
        }
    }
}
