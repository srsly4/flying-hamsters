package game.grabbables;

import engine.Event;
import engine.EventManager;
import engine.render.IRenderable;
import game.Hamster;
import game.grabbables.Grabbable;

/**
 * Created by Sirius on 25.12.2016.
 */
public class WindGrabbable extends Grabbable {

    private final static float boost = 2000f;
    private final static float duration = 0.25f;
    private static boolean windActive = false;

    public WindGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
        this.height *= 3f;
        this.setPosition(xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {
        if (!windActive)
        {
            hamster.setCustomAcceleration(0, boost);
            windActive = true;
            EventManager.getInstance().addEvent(new Event(duration, ()->{
                windActive = false;
                hamster.unsetCustomAcceleration(0, boost);
                return null;
            }));
        }


    }
}
