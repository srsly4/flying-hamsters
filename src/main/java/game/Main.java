package game;

import engine.essential.*;
import engine.render.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class Main {

    public static void main(String[] args){
        //defaults
        int width = 1280;
        int height = 720;
        boolean vsync = true;

        try {
            //load properties
            File configFile = new File("video.conf");
            Properties properties = new Properties();
            if (configFile.exists() && !configFile.isDirectory()){
                properties.load(new FileInputStream(configFile));
                width = Integer.parseInt((String)properties.get("width"));
                height = Integer.parseInt((String)properties.get("height"));
                vsync = Boolean.parseBoolean((String)properties.get("vsync"));
            }
            else {
                properties.put("width", Integer.toString(width));
                properties.put("height", Integer.toString(height));
                properties.put("vsync", Boolean.toString(vsync));
                properties.store(new FileOutputStream(configFile), "Flying Hamsters video configuration");
            }
        }
        catch (Exception e){
            System.err.println("Could not load video settings: " + e.toString());
        }

        try {
            GameLogic logic = new GameLogic();
            EngineThread engine = new EngineThread("Flying Hamsters", width, height, vsync, logic);
            engine.start();
        }
        catch (Exception e){
            System.err.println("A critical error has occured during engine execution. More info in stacktrace.");
            e.printStackTrace();
        }
    }
}
