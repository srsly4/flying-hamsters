package game.grabbables;

import engine.Event;
import engine.EventManager;
import engine.render.AnimatedSprite;
import engine.render.IRenderable;
import game.Hamster;

/**
 * Created by Sirius on 25.12.2016.
 */
public class ReboundGrabbable extends Grabbable {
    public ReboundGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
        width /= 1.5f;
        height /= 2f;
        this.setPosition(xPos, yPos);
    }

    @Override
    public void executeOn(Hamster hamster) {
        if (isUsed) return;
        isUsed = true;
        hamster.setVelXY(hamster.getxVel() + 1000f, Math.abs(hamster.getyVel())+500f);
        if (renderable instanceof AnimatedSprite)
        {
            AnimatedSprite spr = (AnimatedSprite)renderable;
            spr.setFrame(1);
        }
    }
}
