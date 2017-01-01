package game.grabbables;

import engine.EngineException;
import engine.render.AnimatedSprite;
import engine.render.IRenderable;
import engine.render.StaticSprite;
import engine.sound.SoundManager;

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
        renderableInstances.put("bounce", new StaticSprite("/sprites/bounce.xml"));
        renderableInstances.put("super_bounce", new StaticSprite("/sprites/super_bounce.xml"));

        SoundManager snd = SoundManager.getInstance();
        snd.loadSound("grabbable_rebound", "/sounds/rebound.wav");
        snd.loadSound("grabbable_rocket", "/sounds/rocket.wav");
        snd.loadSound("grabbable_wind", "/sounds/wind.wav");
        snd.loadSound("grabbable_bounce", "/sounds/pickup.wav");
        snd.createSoundSource("grabbables");
        snd.createSoundSource("grabbables2"); //wind up
        snd.createSoundSource("grabbables3"); //pickups

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
    public static Grabbable createBounceGrabbable(float x, float y){
        try {
            return new BounceGrabbable(
                    (StaticSprite)(renderableInstances.get("bounce").clone()),
                    x,
                    y
            );
        }
        catch (CloneNotSupportedException ce){
            throw new RuntimeException(ce);
        }
    }
    public static Grabbable createSuperBounceGrabbable(float x, float y){
        try {
            return new SuperBounceGrabbable(
                    (StaticSprite)(renderableInstances.get("super_bounce").clone()),
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
            case "bounce":
                return createBounceGrabbable(x, y);
            case "super_bounce":
                return createSuperBounceGrabbable(x, y);
            default:
                throw new RuntimeException("Unknown grabbable type");
        }
    }


}
