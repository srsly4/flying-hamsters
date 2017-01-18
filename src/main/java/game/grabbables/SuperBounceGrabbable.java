package game.grabbables;

import engine.render.IRenderable;
import engine.sound.SoundManager;
import game.Hamster;

/**
 * Created by Sirius on 24.12.2016.
 */
public class SuperBounceGrabbable extends Grabbable {

    public SuperBounceGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {
        if (isUsed) return;
        SoundManager snd = SoundManager.getInstance();
        snd.loadSoundToSource("grabbable_bounce", "grabbables3").play();
        hamster.setBounceState(2);
        super.executeOn(hamster);
    }
}
