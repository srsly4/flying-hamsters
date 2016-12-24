package game;

import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

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
    private final World world;

//    private StaticSprite tlMarker, trMarker, blMarker, brMarker;

    public GrabbableManager(Hamster hamster) throws EngineException{
        GrabbableFactory.initialize();
        test = GrabbableFactory.createRocketGrabbable(1500f, 450f);
        this.hamster = hamster;
        world = World.getInstance();
        collidables = new ArrayList<>();
        collidables.add(test);

//        tlMarker = new StaticSprite("/sprites/marker.xml");
//        test.setMarkers(tlMarker);
//        try {
//            trMarker = tlMarker.clone();
//            blMarker = tlMarker.clone();
//            brMarker = tlMarker.clone();
//        }
//        catch (CloneNotSupportedException e)
//        {
//            //not possible
//        }
    }

    @Override
    public void update(float interval) {
//        tlMarker.setPosition(
//                world.xPositionToRender(hamster.getLeftBorder()),
//                world.yPositionToRender(hamster.getTopBorder()));
//        trMarker.setPosition(
//                world.xPositionToRender(hamster.getRightBorder()),
//                world.yPositionToRender(hamster.getTopBorder()));
//        blMarker.setPosition(
//                world.xPositionToRender(hamster.getLeftBorder()),
//                world.yPositionToRender(hamster.getBottomBorder()));
//        brMarker.setPosition(
//                world.xPositionToRender(hamster.getRightBorder()),
//                world.yPositionToRender(hamster.getBottomBorder()));

        test.update(interval);
        processCollidables(hamster, collidables);
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        test.updateRenderables(renderables);
//        renderables.add(tlMarker);
//        renderables.add(trMarker);
//        renderables.add(blMarker);
//        renderables.add(brMarker);
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
