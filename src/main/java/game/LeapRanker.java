package game;

import engine.EngineException;
import engine.essential.Window;
import engine.render.FontBuffer;
import engine.render.IRenderable;
import engine.render.TextSprite;
import org.joml.Vector3f;

import java.util.List;

/**
 * Created by Sirius on 25.12.2016.
 */
public class LeapRanker implements IGameObject {

    private final TextSprite groundRank;
    private final FontBuffer fontBuffer;
    private final Hamster hamster;
    private final World world;

    private final float hamsterLengthMeters = 0.1f;
    private boolean resultCalculated = false;


    public LeapRanker(Hamster hamster, Window window) throws EngineException{
        this.hamster = hamster;
        world = World.getInstance();

        fontBuffer = new FontBuffer("/fonts/RifficFree-Bold.ttf", 64, new Vector3f(1f, 1f, 1f));
        groundRank = new TextSprite(fontBuffer, window);
        groundRank.setScale(1f);
        groundRank.setText("0m");
        groundRank.setVisibility(false);
    }

    public void showResult(){
        groundRank.setText(String.format("%.2fm",
                hamster.getxPos()/(hamster.getWorldWidth()/hamsterLengthMeters) ));
        groundRank.setPosition(
                world.xPositionToRender(hamster.getxPos() + 100f),
                world.yPositionToRender(Hamster.groundPos+0.5f*hamster.getWorldHeight()));
        groundRank.setVisibility(true);
        resultCalculated = true;
    }

    public boolean isResultCalculated(){  return resultCalculated; }
    public void reset(){
        resultCalculated = false;
        groundRank.setVisibility(false);
    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(groundRank);
    }

    @Override
    public void cleanUp() {
        groundRank.cleanUp();
    }
}
