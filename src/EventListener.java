import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;


//begin tekst = hajime

//dood tekst = (misschien shippai (failure))
public class EventListener implements GLEventListener {
    public static GL2 gl = null;
    public static String state = "begin"; //begin, game, settings, won, lost

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(1, 0, 0, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);

        //zou op een of andere manier transparency mogelijk maken, maar lijkt fout te werken
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    //called every fps
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

        gl = glAutoDrawable.getGL().getGL2();

        //elke keer aan het begin van display alles clearen
        // zodat alles opnieuw getekend kan worden
        //gl.glClearColor(0, 0,0 , 1);
        //gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        Renderer.drawCurrentLoop(gl);
    }

    //zorgt er voor dat scherm mee opschuift als player naar links/boven/rechts... beweegt
    public static void updateScreenDisplacement(){

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(Renderer.leftPos, Renderer.leftPos + Renderer.unitsWide, Renderer.lowPos, Renderer.lowPos + Renderer.getUnitsTall(), -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

    }

    //called everytime window is reshaped
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        //aantal rijen waarin window wordt verdeeld hangt af van de height, en gebaseerd op aantal kolommen
        // 2d -> orthografische projectie
        // x left, x right, y bottom, y top
        // door y top negatief te maken worden de coordinaten logischer
        gl.glOrtho(Renderer.leftPos, Renderer.leftPos + Renderer.unitsWide, Renderer.lowPos, Renderer.lowPos + Renderer.getUnitsTall(), -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

    }

}
