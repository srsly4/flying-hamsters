package engine;


import com.sun.xml.internal.ws.api.pipe.Engine;
import engine.essential.TimeManager;

import java.util.PriorityQueue;


/**
 * Created by Szymon Piechaczek on 20.12.2016.
 */
public class EventManager {
    private final TimeManager time;
    private PriorityQueue<Event> events;
    private EventManager(TimeManager time)
    {
        this.time = time;
        events = new PriorityQueue<>();
    }

    public void update() throws EngineException{
        try {
            float currTime = (float) time.getTime();
            while (!events.isEmpty()) {
                Event ev = events.peek();
                if (ev.getExectionTime()+ev.getAddedTime() <= currTime)
                {
                    events.poll();
                    ev.execute();
                }
                else break;
            }
        }
        catch (Exception e)
        {
            throw (EngineException)(new EngineException("Could not execute event").initCause(e));
        }
    }

    public void addEvent(Event ev){
        ev.setAddedTime((float)time.getTime());
        events.add(ev);
    }

    public static EventManager instance;
    public static void initializeInstance(TimeManager time){
        instance = new EventManager(time);
    }
    public static EventManager getInstance() throws EngineException{
        if (instance == null) throw new EngineException("EventManager has not benn initialized");
        return instance;
    }
}
