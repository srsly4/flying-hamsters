package game.grabbables;

import engine.Event;
import engine.EventManager;
import engine.render.IRenderable;
import engine.sound.SoundManager;
import game.Hamster;

/**
 * Created by Sirius on 25.12.2016.
 */
public class CloudGrabbable extends Grabbable {
    private final float velocityMultiplierX = 0.75f;
    private final float velocityMultiplierY = 0.6666f;
    private boolean cloudActivated = false;
    public CloudGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {
        if (cloudActivated) return;
        hamster.setVelXY(
                hamster.getxVel()*velocityMultiplierX,
                hamster.getyVel()*velocityMultiplierY);
        cloudActivated = true;
        SoundManager.getInstance().loadSoundToSource("grabbable_cloud", "grabbables2").play();
        EventManager.getInstance().addEvent(new Event(0.5f, ()->{
            cloudActivated = false;
            return null;
        }));

    }
}
