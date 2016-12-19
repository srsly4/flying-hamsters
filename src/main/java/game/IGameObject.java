package game;

import engine.render.IRenderable;

import java.util.ArrayList;

/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public interface IGameObject {
    void update(float interval);
    ArrayList<IRenderable> getRenderables();
}
