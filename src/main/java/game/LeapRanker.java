package game;

import engine.EngineException;
import engine.Event;
import engine.EventManager;
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
    private final TextSprite heightRank;
    private final FontBuffer fontBuffer;
    private final Hamster hamster;
    private final World world;

    public static final float hamsterLengthMeters = 0.1f;
    private final float rankedStartHeight = 6000f;
    private boolean resultCalculated = false;

    private float maxYPos = rankedStartHeight;
    private float lastYPos = 0f;
    private float heightRankY = 0f;
    private float heightRankX = 0f;

    private float heightRankScale = 1f;
    private float groundRankScale = 1f;

    public LeapRanker(Hamster hamster, Window window) throws EngineException{
        this.hamster = hamster;
        world = World.getInstance();

        fontBuffer = new FontBuffer("/fonts/RifficFree-Bold.ttf", 64);
        groundRank = new TextSprite(fontBuffer, window);
        groundRank.setScale(1f);
        groundRank.setDepth(0.8f);
        groundRank.setText("0m");
        groundRank.setVisibility(false);

        heightRank = new TextSprite(fontBuffer, window);
        heightRank.setScale(1f);
        heightRank.setDepth(0.8f);
        heightRank.setText("50m - new record!");
        heightRank.setVisibility(false);
        heightRank.setColor(0,0,0, 0.5f);
    }

    public void showResult(){
        groundRank.setText(String.format("%.2fm, %.1fm/s",
                hamster.getxPos()/(hamster.getWorldWidth()/hamsterLengthMeters),
                hamster.getImpactVelocity()/(hamster.getWorldWidth()/hamsterLengthMeters)
                ));
        groundRank.setPosition(
                world.xPositionToRender(hamster.getxPos() + 100f),
                world.yPositionToRender(Hamster.groundPos+0.5f*hamster.getWorldHeight()));
        resultCalculated = true;

        EventManager.getInstance().addEvent(new Event(1f, ()->{
            hamster.setState(HamsterState.Ranked);
            groundRankScale = 0.5f;
            groundRank.setVisibility(true);
            return null;
        }));
    }

    public boolean isResultCalculated(){  return resultCalculated; }
    public void reset(){
        resultCalculated = false;
        maxYPos = rankedStartHeight;
        heightRank.setVisibility(false);
        groundRank.setVisibility(false);
    }

    public void setHeightRank(float x, float y){
        heightRankX = x;
        heightRankY = y;
        heightRank.setVisibility(true);
        heightRankScale = 0.5f;
        heightRank.setText(String.format("%.1fm!",
                maxYPos/(hamster.getWorldWidth()/hamsterLengthMeters)
                ));
    }

    @Override
    public void update(float interval) {
        //height rank
        maxYPos = Math.max(maxYPos, hamster.getyPos());
        if (maxYPos == lastYPos && hamster.getyPos() < lastYPos)
        {
            setHeightRank(hamster.getxPos(), maxYPos);
        }
        lastYPos = hamster.getyPos();

        //update height rank position
        heightRank.setPosition(
                world.xPositionToRender(
                        heightRankX + World.cameraWidth/2f - World.renderWidthToWorld(heightRank.getTextWidth())),
                world.yPositionToRender(heightRankY)
        );

        if (heightRankScale >= 1.5f) heightRankScale = 1.5f;
        else heightRankScale += 4f*interval;
        heightRank.setScale(heightRankScale);

        if (groundRankScale >= 1f) groundRankScale = 1f;
        else groundRankScale += 3f*interval;
        groundRank.setScale(groundRankScale);
    }

    @Override
    public void updateRenderables(List<IRenderable> renderables) {
        renderables.add(groundRank);
        renderables.add(heightRank);
    }

    @Override
    public void cleanUp() {
        groundRank.cleanUp();
    }

}
