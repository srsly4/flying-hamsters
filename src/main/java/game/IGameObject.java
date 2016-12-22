package game;

import engine.render.IRenderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public interface IGameObject {
    /**
     * Object implementation of game scene change
     * @param interval - time between previous update execution
     */
    void update(float interval);

    /**
     * Function called on renderer demand
     * @param renderables - list of IRenderable entities which will be passed to renderer, should only add to list
     */
    void updateRenderables(final List<IRenderable> renderables);


    /**
     * Destroys renderable entities with proper GL functions
     */
    default void cleanUp(){ }
}
