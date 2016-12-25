package game;

import engine.EngineException;
import engine.Event;
import engine.EventManager;
import engine.essential.*;
import engine.render.*;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallback;

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
    private Window window;


    private LeapRanker leapRanker;
    private UIInterface ui;
    public GameLogic() throws EngineException{
    }

    @Override
    public void init(Window window) throws EngineException {
        renderer = new Renderer();
        renderer.init(window);
        this.window = window;
        window.setClearColor(0.84f, 0.59f, 0.22f, 1.0f);

        //initialize asynchronous key callbacks as local class
        window.setKeyCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
                asynchronousInput(key, action, mods);
            }
        });

        //initialize game objects
        renderables = new ArrayList<>();
        ((ArrayList)renderables).ensureCapacity(256);

        world = World.initializeInstance();
        hamster = new Hamster();
        hamsterShadow = new HamsterShadow(hamster);

        grabbables = new GrabbableManager(hamster);
        pillow = new LaunchPillow(hamster);

        leapRanker = new LeapRanker(hamster, window);
        ui = new UIInterface(hamster, window);

        gameObjects = new IGameObject[]{world, hamsterShadow, pillow, grabbables, hamster, leapRanker, ui};

        //place the hamster on launch area
        pillow.reset();
    }

    @Override
    public void input(Window window) {
        hamster.setFly(window.isKeyPressed(GLFW_KEY_SPACE));

    }

    private void asynchronousInput(int key, int action, int mods){
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            window.setShouldClose();

        if (hamster.getState() == HamsterState.Launched
                && !pillow.isLaunched()
                && key == GLFW_KEY_SPACE
                && action == GLFW_PRESS)
            pillow.launch();

        if (hamster.getState() == HamsterState.BeforeLaunch
                && key == GLFW_KEY_SPACE
                && action == GLFW_PRESS)
            pillow.toss();

        if (hamster.getState() == HamsterState.Grounded
                && key == GLFW_KEY_SPACE
                && action == GLFW_PRESS)
        {
            leapRanker.reset();
            grabbables.reset();
            pillow.reset();
        }
    }

    @Override
    public void update(float interval) {
        world.setXPos(hamster.getxPos() - 200f); // world is behind hamster

        //check if hamster has landed
        if (!leapRanker.isResultCalculated() && hamster.getState() == HamsterState.Grounded)
        {
            leapRanker.showResult();
        }

        //update renderables
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
