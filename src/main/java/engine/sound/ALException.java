package engine.sound;

import engine.EngineException;

/**
 * Created by Sirius on 27.12.2016.
 */
public class ALException extends EngineException {
    private String function;
    public ALException(String message, String function) {
        super(message);
        this.function = function;
    }

    @Override
    public String toString() {
        return "ALException on" +
                "function " + function +
                ": " + this.getMessage();
    }
}
