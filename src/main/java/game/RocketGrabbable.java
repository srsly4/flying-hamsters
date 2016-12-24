package game;

import engine.render.IRenderable;

/**
 * Created by Sirius on 24.12.2016.
 */
public class RocketGrabbable extends Grabbable {

    public RocketGrabbable(IRenderable renderableInstance, float xPos, float yPos) {
        super(renderableInstance, xPos, yPos);
}

    @Override
    public void executeOn(Hamster hamster) {
        hamster.setVelXY(5f*hamster.getxVel()+300f, 5f*hamster.getyVel() );
        super.executeOn(hamster);
    }
}
