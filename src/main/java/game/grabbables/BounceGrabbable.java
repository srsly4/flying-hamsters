package game.grabbables;

import engine.Event;
import engine.EventManager;
import engine.render.IRenderable;
import engine.sound.SoundManager;
import game.Hamster;

/**
 * Created by Sirius on 24.12.2016.
 */
public class BounceGrabbable extends Grabbable {

    public BounceGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {
        if (isUsed) return;
        SoundManager snd = SoundManager.getInstance();
        snd.loadSoundToSource("grabbable_bounce", "grabbables3").play();
        hamster.setBounceState(1);
        super.executeOn(hamster);
    }
}
