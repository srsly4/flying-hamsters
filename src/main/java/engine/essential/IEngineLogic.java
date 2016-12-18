package engine.essential;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public interface IEngineLogic {
    /**
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Should be implemented with input controll functions
     * @param window A Window object with render context
     */
    void input(Window window);

    void update(float interval);

    void render(Window window);

    default void cleanup() { };

}
