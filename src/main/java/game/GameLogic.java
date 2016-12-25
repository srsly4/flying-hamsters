package game;

import engine.EngineException;
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
    private LaunchPillow pillow;
    private GrabbableManager grabbables;
    private UIInterface ui;
    public GameLogic() throws Exception{
    }

    @Override
    public void init(Window window) throws EngineException {
        renderer = new Renderer();
        renderer.init(window);

        window.setClearColor(0.84f, 0.59f, 0.22f, 1.0f);

        renderables = new ArrayList<>();
        ((ArrayList)renderables).ensureCapacity(256);

        world = World.initializeInstance();
        hamster = new Hamster();
        hamsterShadow = new HamsterShadow(hamster);

        grabbables = new GrabbableManager(hamster);
        pillow = new LaunchPillow(hamster);


        ui = new UIInterface(hamster, window);

        gameObjects = new IGameObject[]{world, hamsterShadow, pillow, grabbables, hamster, ui};

        EventManager ev = EventManager.getInstance();

        //place the hamster on launch area
        hamster.setPosition(0, Hamster.groundPos);
        hamster.setState(HamsterState.BeforeLaunch);
        ev.addEvent(new Event(2f, () -> {
            hamster.setState(HamsterState.Launched);
            hamster.setVelXY(0, 1000f);
            return null;
        }));
    }

    @Override
    public void input(Window window) {
        hamster.setFly(window.isKeyPressed(GLFW_KEY_SPACE));
        if (hamster.getState() == HamsterState.Launched
                && !pillow.isLaunched()
                && window.isKeyPressed(GLFW_KEY_SPACE))
            pillow.launch();
    }

    @Override
    public void update(float interval) {
        world.setXPos(hamster.getxPos() - 200f); // world is behind hamster
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
