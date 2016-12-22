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
    private final FontBuffer font;
    private final Window window;

    public UIInterface(Hamster hamster, World world, Window window) throws EngineException {
        this.hamster = hamster;
        this.world = world;
        this.window = window;

        font = new FontBuffer("/fonts/RifficFree-Bold.ttf", 48, new Vector3f(1f, 1f, 1f));
        position = new TextSprite(font, window);
        position.setText("xPos: ");
        position.setPosition(-1f, 0.5625f);
    }

    @Override
    public void update(float interval) {
        position.setText(String.format("x: %.0f, y: %.0f", hamster.getxPos(), hamster.getyPos()));
    }

    @Override
    public void updateRenderables(final List<IRenderable> renderables) {
        renderables.add(position);
    }

    @Override
    public void cleanUp() {
        position.cleanUp();
        font.cleanup();
    }
}
