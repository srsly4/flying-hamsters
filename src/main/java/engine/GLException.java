package engine;

import engine.EngineException;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class GLException extends EngineException {
    private String glFunction;
    public GLException(String message, String glFunction) {
        super(message);
        this.glFunction = glFunction;
    }

    @Override
    public String toString() {
        return "GLException for function " + glFunction + ": " + this.getMessage();
    }
}
