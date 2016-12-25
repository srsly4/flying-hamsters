package engine.essential;

import engine.EngineException;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public interface IEngineLogic {
    /**
     * Initializes EngineLogic. There should be all GL initializing functions
     * @throws EngineException
     */
    void init(Window window) throws EngineException;

    /**
     * Should be implemented with continous input control functions
     * Function is called before update, render with standard UPS
     * @param window A Window object with render context
     */
    void input(Window window);


    /**
     * Updates all game objects within standard UPS
     * @param interval Time in seconds that passed from the last frame
     */
    void update(float interval);


    /**
     * Renders all game objects with standard FPS
     * @param window A Window object with render context
     */
    void render(Window window);

    /**
     * Cleans all game objects and mark memory as free
     */
    default void cleanup() { }

}
