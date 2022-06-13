import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Keyboard implements KeyListener {
    Game game;
    public void setGame(Game game){
        this.game = game;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //149 is pijl naar links
        //150 is pijl naar boven
        //151 is pijl naar rechts
        //152 is pijl naar onder

        switch (e.getKeyCode()) {
            case 151 -> game.goMoveForwards();
            case 149 -> game.goMoveBackwards();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

}
