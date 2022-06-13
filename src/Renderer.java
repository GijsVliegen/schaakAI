import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import java.security.Key;

public class Renderer {
    private static GLWindow window = null;

    public static int windowHeight = 800;
    public static int windowWidth = 1000;

    //public static float[][] speed = new float[5][2];
    //window opdelen in 10 kolommen
    //if unitsWide is increased: looks like it zooms out
    public static float unitsWide = 10;
    public static float unitsTall; //==8
    public static float leftBound = 0;
    public static float lowerBound = 0;
    public static float rightBound = 8;
    public static float upperBound = 8;
    public static float leftPos = 0;
    public static float lowPos = 0;
    public static float previousUnitsTall = 0;
    public static float currentUnitsTall = 0;
    private static GLProfile profile;
    public static String state = "begin";
    public static Game game;
    public static void init() {

        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        window = GLWindow.create(capabilities);
        window.setSize(windowWidth, windowHeight);
        window.setResizable(true);
        //als dit wordt gedaan is het onmogelijk het terug af te sluiten
        //window.setFullscreen(true);
        window.setVisible(true);
        window.setTitle("schaken");

        //zorgen dat de muis wordt geregistreerd op window
        window.requestFocus();
        Mouse mouse = new Mouse();
        Keyboard keyboard = new Keyboard();

        unitsTall = unitsWide/window.getWidth()*window.getHeight();

        //om de windows te kunnen updaten
        previousUnitsTall = Renderer.getUnitsTall();
        currentUnitsTall = Renderer.getUnitsTall();


        game = new Game();
        mouse.setGame(game);
        keyboard.setGame(game);
        window.addMouseListener(mouse);
        window.addKeyListener(keyboard);
        FPSAnimator animator = new FPSAnimator(window, 30);
        animator.start();
        window.addGLEventListener(new EventListener());

    }
    public static void drawCurrentLoop(GL2 gl){
        gl.glClearColor(0, 0,0 , 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        game.loop();
    }
    public static int getWindowWidth(){
        return window.getWidth();
    }
    public static int getWindowHeight(){
        return window.getHeight();
    }
    public static float getUnitsTall() {return Renderer.getWindowHeight() / (Renderer.getWindowWidth()/Renderer.unitsWide);}
    public static void main(String[] args){
        init();
    }
    public static GLProfile getProfile() {
        return profile;
    }
}
