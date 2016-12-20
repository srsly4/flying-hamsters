package game;

import engine.Event;
import engine.EventManager;
import engine.essential.*;
import engine.render.*;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class GameLogic implements IEngineLogic {

    private Renderer renderer;
    private IRenderable[] items;
    private IGameObject[] gameObjects;

    private World world;
    private Hamster hamster;

    public GameLogic() throws Exception{
    }

    @Override
    public void init(Window window) throws Exception {
        renderer = new Renderer();
        renderer.init(window);

        window.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        ArrayList<IRenderable> rlist = new ArrayList<>();

        world = new World();
        hamster = new Hamster();


        gameObjects = new IGameObject[]{world, hamster};

        //create renderables list
        for (IGameObject obj : gameObjects)
            rlist.addAll(obj.getRenderables());

        //add markers
//        for (float i = -1f; i <= 1f; i += 0.25f)
//            for (float j = -0.5625f; j <= +0.5625f; j += 0.125f)
//            {
//                StaticSprite marker = new StaticSprite("/sprites/marker.xml");
//                marker.setPosition(i, j);
//                rlist.add(marker);
//            }


        items = rlist.toArray(new IRenderable[]{});


        //logic initialize
        EventManager ev = EventManager.getInstance();
        ev.addEvent(new Event(2f, () -> {
            hamster.setInAir(true);
            return null;
        }));
        ev.addEvent(new Event(2.5f, () -> {
            hamster.setVelXY(150f, 10f);
            return null;
        }));
    }

    @Override
    public void input(Window window) {
        hamster.setFly(window.isKeyPressed(GLFW_KEY_SPACE));
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
