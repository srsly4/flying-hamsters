package engine;

import java.util.concurrent.Callable;

/**
 * Created by Szymon Piechaczek on 20.12.2016.
 */
public class Event implements Comparable<Event> {

    private Callable<Void> action;
    private float actionTime;
    private float addedTime = 0.0f;
    public Event(float actionTime, Callable<Void> action){
        this.action = action;
        this.actionTime = actionTime;
    }

    public void execute() throws Exception {
        this.action.call();
    }

    public float getExectionTime(){  return actionTime; }
    public void setAddedTime(float addedTime) { this.addedTime = addedTime; }
    public float getAddedTime() {
        return addedTime;
    }

    @Override
    public int compareTo(Event o) {
        return (int)Math.signum(this.actionTime - o.getExectionTime());
    }
}
