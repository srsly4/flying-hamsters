package engine.essential;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

/**
 * Created by Szymon Piechaczek on 18.12.2016.
 */
public class EngineThread implements Runnable {

    public static final int DESIRED_FPS = 60;
    public static final int DESIRED_UPS = 60;

    private final Window window;
    private final Thread gameLoopThread;
    private final TimeManager timeManager;
    private final IEngineLogic logic;

    public EngineThread(String windowTitle, int width, int height, boolean vSync, IEngineLogic logic){
        this.logic = logic;

        //creating renderable window
        this.window = new Window(windowTitle, width, height, vSync);

        //creating game logic thread
        gameLoopThread = new Thread(this, "Engine loop");
        timeManager = new TimeManager();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    //separable game logic thread
    public void run() {
        try {
            init();
            loop();
        }
        catch (Exception e)
        {
            //wysyp sie: TODO: error handler
            e.printStackTrace();
        }
    }

    protected void loop(){
        float delta;
        float update_int = 1.0f / DESIRED_UPS;
        float update_acc = 0.0f;

        boolean runFlag = true;
        while (runFlag && !window.shouldClose()){
            //the entire render loop
            delta = timeManager.getDelta(); //calculate and save delta time
            update_acc += delta;

            //handle inputs
            input();

            //we are creating update loop
            while (update_acc >= update_int){
                update(delta);
                update_acc -= delta;
            }

            render();

            //if there's no vsync enabled we have to limit render loop execution
            if (!window.isVsync())
                syncFramerate();
        }
    }

    protected void init() throws Exception {
        window.init();
        timeManager.init();
        logic.init();
    }

    protected void input(){
        logic.input(window); //pass the input logic to actually logic class
    }

    protected void update(float interval){
        logic.update(interval); //pass the update logic to actually logic class
    }

    protected void render(){
        logic.render(window); //pass the render logic to actually logic class
        window.update();
    }

    //sync render loop to desired framerate
    private void syncFramerate() {
        float dt = 1.0f / DESIRED_FPS;
        double nextFrameTime = timeManager.getLastTime() + dt;
        while (timeManager.getTime() < nextFrameTime)
        {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                //do nothing
            }
        }
    }
}
