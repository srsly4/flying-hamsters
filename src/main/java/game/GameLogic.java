package game;

import engine.Event;
import engine.EventManager;
import engine.essential.*;
import engine.render.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class GameLogic implements IEngineLogic {

    private Renderer renderer;
    private IGameObject[] gameObjects;
    private List<IRenderable> renderables;
    private IRenderable[] renderablesCache;

    private World world;
    private Hamster hamster;
    private HamsterShadow hamsterShadow;
    private UIInterface ui;
    public GameLogic() throws Exception{
    }

    @Override
    public void init(Window window) throws Exception {
        renderer = new Renderer();
        renderer.init(window);

        window.setClearColor(0.84f, 0.59f, 0.22f, 1.0f);

        renderables = new ArrayList<>();
        ((ArrayList)renderables).ensureCapacity(256);

        world = new World();
        hamster = new Hamster(world);
        hamsterShadow = new HamsterShadow(world, hamster);

        ui = new UIInterface(hamster, world, window);

        gameObjects = new IGameObject[]{world, hamsterShadow, hamster, ui};

        //logic initialize
        EventManager ev = EventManager.getInstance();
        ev.addEvent(new Event(0.5f, () -> {
            hamster.setPosition(50f, 400f);
            hamster.setInAir(true);
            hamster.setVelXY(800f, 90f);
            return null;
        }));
    }

    @Override
    public void input(Window window) {
        hamster.setFly(window.isKeyPressed(GLFW_KEY_SPACE));

    }

    @Override
    public void update(float interval) {
        world.setXPos(hamster.getxPos());
        renderables.clear();
        for (IGameObject obj : gameObjects) {
            obj.update(interval);
            obj.updateRenderables(renderables);
        }

        renderablesCache = renderables.toArray(new IRenderable[]{});
    }

    @Override
    public void render(Window window) {
        renderer.render(renderablesCache, window);
    }

    @Override
    public void cleanup() {
        for (IGameObject obj : gameObjects)
            obj.cleanUp();
    }
}
