package engine.essential;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class TimeManager {

    private double lastTime;

    /**
     * @return Current time in seconds
     */
    public double getTime(){ return System.nanoTime() / 1.0E9; }

    public void init(){
        lastTime = getTime();
    }

    public float getDelta(){
        double tm = getTime();
        float dt = (float)(tm - lastTime);
        lastTime = tm;
        return dt;
    }

    public double getLastTime() { return lastTime; }
}
