package game.grabbables;

import engine.EngineException;
import engine.render.AnimatedSprite;
import engine.render.IRenderable;
import engine.render.StaticSprite;

import java.util.HashMap;

/**
 * Created by Sirius on 23.12.2016.
 */
public class GrabbableFactory {

    private static HashMap<String, StaticSprite> renderableInstances;
    public static void initialize() throws EngineException{
        renderableInstances = new HashMap<>();
        renderableInstances.put("rocket", new StaticSprite("/sprites/rocket.xml"));
        renderableInstances.put("wind", new StaticSprite("/sprites/wind.xml"));
        renderableInstances.put("rebound", new AnimatedSprite("/sprites/rebound.xml"));
    }

    public static Grabbable createRocketGrabbable(float x, float y){
        try {
            return new RocketGrabbable(
                    (StaticSprite)(renderableInstances.get("rocket").clone()),
                    x,
                    y
            );
        }
        catch (CloneNotSupportedException ce){
            throw new RuntimeException(ce);
        }
    }

    public static Grabbable createWindGrabbable(float x, float y){
        try {
            return new WindGrabbable(
                    (StaticSprite)(renderableInstances.get("wind").clone()),
                    x,
                    y
            );
        }
        catch (CloneNotSupportedException ce){
            throw new RuntimeException(ce);
        }
    }

    public static Grabbable createReboundGrabbable(float x, float y){
        try {
            return new ReboundGrabbable(
                    (AnimatedSprite)(renderableInstances.get("rebound").clone()),
                    x,
                    y
            );
        }
        catch (CloneNotSupportedException ce){
            throw new RuntimeException(ce);
        }
    }

    public static Grabbable createGrabbable(String type, float x, float y){
        switch (type){
            case "rocket":
                return createRocketGrabbable(x, y);
            case "wind":
                return createWindGrabbable(x, y);
            case "rebound":
                return createReboundGrabbable(x, y);
            default:
                throw new RuntimeException("Unknown grabbable type");
        }
    }


}
