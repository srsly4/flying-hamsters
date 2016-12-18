package engine;

import java.io.*;
import java.util.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Utils {

    public static String loadResource(String name) throws EngineException{
        String result;
        try (InputStream in = Utils.class.getClass().getResourceAsStream(name);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        catch (IOException e) {
            EngineException ex = new EngineException("Could not load requested resource: " + name);
            ex.initCause(e);
            throw ex;
        }
        return result;
    }
}
