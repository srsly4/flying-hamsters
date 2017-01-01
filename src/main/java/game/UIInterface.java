package game;

import engine.EngineException;
import engine.essential.Window;
import engine.render.FontBuffer;
import engine.render.IRenderable;
import engine.render.TextSprite;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirius on 22.12.2016.
 */
public class UIInterface implements IGameObject {

    private final Hamster hamster;
    private final World world;
    private final TextSprite position;
    private final TextSprite velocity;
    private final TextSprite strength;
    private final TextSprite version;
    private final FontBuffer font;
    private final Window window;

    public UIInterface(Hamster hamster, Window window) throws EngineException {
        this.hamster = hamster;
        this.world = World.getInstance();
        this.window = window;

        font = new FontBuffer("/fonts/RifficFree-Bold.ttf", 48, new Vector3f(1f, 1f, 1f));
        FontBuffer debugFont = new FontBuffer("/fonts/RifficFree-Bold.ttf", 24, new Vector3f(1f, 1f, 1f));
        position = new TextSprite(debugFont, window);
        position.setText("xPos: ");
        position.setPosition(-1f, 0.5625f);
        position.setDepth(0.9f);

        velocity = new TextSprite(debugFont, window);
        velocity.setText("xVel: ");
        velocity.setPosition(-1f, 0.53f);
        velocity.setDepth(0.9f);

        version = new TextSprite(debugFont, window);
        version.setText("alpha1");
        version.setPosition(-1f, -0.53f);
        version.setDepth(0.9f);

        strength = new TextSprite(font, window);
        strength.setPosition(0.80f, 0.5625f);
        strength.setText("100%");
        strength.setDepth(0.9f);

    }

    @Override
    public void update(float interval) {
        position.setText(String.format("x: %.0f, y: %.0f", hamster.getxPos(), hamster.getyPos()));
        velocity.setText(String.format("vel: x: %.0f, y: %.0f", hamster.getxVel(), hamster.getyVel()));
        if (hamster.getState() == HamsterState.InAir)
            strength.setText(String.format("%.0f%%", hamster.getFlightStrength()));
    }

    @Override
    public void updateRenderables(final List<IRenderable> renderables) {
        renderables.add(position);
        renderables.add(velocity);
        if (hamster.getState() == HamsterState.InAir)
            renderables.add(strength);
        renderables.add(version);
    }

    @Override
    public void cleanUp() {
        position.cleanUp();
        velocity.cleanUp();
        font.cleanup();
    }
}
