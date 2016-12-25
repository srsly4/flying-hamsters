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
    private final FontBuffer font;
    private final Window window;

    public UIInterface(Hamster hamster, Window window) throws EngineException {
        this.hamster = hamster;
        this.world = World.getInstance();
        this.window = window;

        font = new FontBuffer("/fonts/RifficFree-Bold.ttf", 48, new Vector3f(1f, 1f, 1f));

        position = new TextSprite(font, window);
        position.setText("xPos: ");
        position.setPosition(-1f, 0.5625f);

        velocity = new TextSprite(font, window);
        velocity.setText("xVel: ");
        velocity.setPosition(-1f, 0.5f);
    }

    @Override
    public void update(float interval) {
        position.setText(String.format("x: %.0f, y: %.0f", hamster.getxPos(), hamster.getyPos()));
        velocity.setText(String.format("vel: x: %.0f, y: %.0f", hamster.getxVel(), hamster.getyVel()));
    }

    @Override
    public void updateRenderables(final List<IRenderable> renderables) {
        renderables.add(position);
        renderables.add(velocity);
    }

    @Override
    public void cleanUp() {
        position.cleanUp();
        velocity.cleanUp();
        font.cleanup();
    }
}
