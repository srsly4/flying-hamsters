package engine.render;

import engine.EngineException;
import engine.ObjectParser;

/**
 * Created by Sirius on 25.12.2016.
 */
public class AnimatedSprite extends StaticSprite {
    private int currentFrame = 0;
    private int frames;
    private float fps;
    private float period;
    private float frameWidth;
    private double timeAcc = 0f;

    public AnimatedSprite(String resourceName) throws EngineException {
        super();

        ObjectParser parser = ObjectParser.getInstance();
        parser.openResource(resourceName);

        float spriteWidth = parser.getFloatValue("width");
        float spriteHeight = parser.getFloatValue("height");
        String texUri = parser.getStringValue("texture");
        float texWidth = parser.getFloatValue("frameWidth");
        float texHeight = parser.getFloatValue("frameHeight");
        float sheetSize = parser.getFloatValue("sheetSize");
        frames = parser.getIntValue("frames");
        fps = parser.getFloatValue("fps");
        period = 1.0f/fps;
        float wTexWidth = (texWidth/sheetSize);
        float wTexHeight = (texHeight/sheetSize);
        float[] texCoords = new float[]{
                0, 0, //lewy gorny
                0, wTexHeight, //lewy dolny
                wTexWidth, wTexHeight, //prawy dolny
                0f, 0f, //lewy gorny
                wTexWidth, 0f, //prawy gorny
                wTexWidth, wTexHeight //prawy dolny
        };

        frameWidth = wTexWidth;
        initObject(texUri, spriteWidth, spriteHeight, texCoords);
    }

    @Override
    public void render() {
        super.render();
    }

    public void update(double interval)
    {
        timeAcc += interval;
        while (timeAcc > period)
        {
            currentFrame++;
            if (currentFrame >= frames)
                currentFrame = 0;
            timeAcc -= period;
        }
        setTextureOrigin((float)currentFrame*frameWidth, 0f);
    }

    public void setFrame(int frame){
        currentFrame = Math.min(frame, frames-1);
    }

    public int getFrames() {
        return frames;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
        this.period = 1f/fps;
    }
}
