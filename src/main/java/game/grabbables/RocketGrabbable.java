package game.grabbables;

import engine.Event;
import engine.EventManager;
import engine.render.IRenderable;
import game.Hamster;
import game.grabbables.Grabbable;

/**
 * Created by Sirius on 24.12.2016.
 */
public class RocketGrabbable extends Grabbable {

    private final static float boost = 2500f;
    private final static float xRelative = 1.25f;
    private final static float duration = 0.25f;
    public RocketGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {

        float angle = (float)Math.atan2(hamster.getyVel(), hamster.getxVel());
        float dx = xRelative*boost*(float)Math.cos(angle);
        float dy = boost*(float)Math.sin(angle);
        hamster.setCustomAcceleration(dx, dy);
        EventManager.getInstance().addEvent(new Event(duration, ()->{
            hamster.unsetCustomAcceleration(dx, dy);
            return null;
        }));
        super.executeOn(hamster);
    }
}
