import java.util.ArrayList;
import java.util.List;

public class Loper extends Schaakstuk{
    public ImageResource image;
    private static final int[] LoperValue = new int[]{-20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20};

    public Loper(int x, int y, Boolean team){
        super(x, y, team, 300);
        if (teamWhite){
            image = new ImageResource("resource/LoperW.png");
        }
        else{
            image = new ImageResource("resource/LoperZ.png");
        }
    }
    @Override
    public void drawStuk(){
        Graphics.draw(this, image);
    }

    @Override
    public int getWaardePositie(int x, int y){
        int temp_y = y;
        if(!teamWhite){
            temp_y = 7-y;
        }
        return LoperValue[(7-temp_y)*8+x];
    }
    @Override
    public String soortStuk(){
        return "loper";
    }

    @Override
    public List<Positie> geefZetten(Board board){
        int x = pos.x;
        int y = pos.y;
        List<Positie> zettenLijst = new ArrayList<>();
        for (int i = 1; i < 8 - Math.max(x, y); i++){ //schuin naar rechtsboven
            Schaakstuk piece = board.getStuk(x+i, y+i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x+i, y+i));
                }
                else{
                    zettenLijst.add(new Positie(x+i, y+i));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = 1; i < 8 - Math.max(x, 7-y); i++){ //schuin naar rechtsonder
            Schaakstuk piece = board.getStuk(x+i, y-i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x+i, y-i));
                }
                else{
                    zettenLijst.add(new Positie(x+i, y-i));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = 1; i < 8 - Math.max(7-x, y); i++){ //schuin naar linksboven
            Schaakstuk piece = board.getStuk(x-i, y+i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x-i, y+i));
                }
                else{
                    zettenLijst.add(new Positie(x-i, y+i));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = 1; i < 8 - Math.max(7-x, 7-y); i++){ //schuin naar linksonder
            Schaakstuk piece = board.getStuk(x-i, y-i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x-i, y-i));
                }
                else{
                    zettenLijst.add(new Positie(x-i, y-i));
                    break;
                }
            }
            else {
                break;
            }
        }
        return zettenLijst;
    }
}
