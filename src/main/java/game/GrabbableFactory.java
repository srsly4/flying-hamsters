package game;

import com.sun.xml.internal.ws.api.pipe.Engine;
import engine.EngineException;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.HashMap;

/**
 * Created by Sirius on 23.12.2016.
 */
public class GrabbableFactory {

    private static HashMap<String, IRenderable> renderableInstances;
    public static void initialize() throws EngineException{
        renderableInstances = new HashMap<>();
        renderableInstances.put("rocket", new StaticSprite("/sprites/rocket.xml"));

    }

    public static Grabbable createRocketGrabbable(float x, float y){
        return new RocketGrabbable(
                renderableInstances.get("rocket"),
                x,
                y
        );
    }

}