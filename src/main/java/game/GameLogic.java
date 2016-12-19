package game;

import engine.essential.*;
import engine.render.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class GameLogic implements IEngineLogic {

    private Renderer renderer;
    private IRenderable[] items;
    private IGameObject[] gameObjects;
    StaticSprite hamster;

    private World world;

    public GameLogic() throws Exception{
    }

    @Override
    public void init(Window window) throws Exception {
        renderer = new Renderer();
        renderer.init(window);

        window.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        ArrayList<IRenderable> rlist = new ArrayList<>();
        hamster = new StaticSprite("/sprites/hamster.xml");
        hamster.setPosition(-1f, 0f);
        hamster.setRotation(30f);
        rlist.add(hamster);

        world = new World();
        rlist.addAll(world.getRenderables());

        items = rlist.toArray(new IRenderable[]{});
        gameObjects = new IGameObject[]{world};
    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(float interval) {
        for (IGameObject obj : gameObjects)
            obj.update(interval);
    }

    @Override
    public void render(Window window) {
        renderer.render(items, window);
    }

    @Override
    public void cleanup() {
        for (IRenderable item : items)
            item.cleanUp();
    }
}
