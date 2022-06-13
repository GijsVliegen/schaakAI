import java.util.ArrayList;
import java.util.List;

public class Toren extends Schaakstuk{
    public ImageResource image;
    private static final int[] TorenValue = new int[]{0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0};
    public Toren(int x, int y, Boolean team){
        super(x, y, team, 500);
        if (teamWhite){
            image = new ImageResource("resource/TorenW.png");
        }
        else{
            image = new ImageResource("resource/TorenZ.png");
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
        return TorenValue[(7-temp_y)*8+x];
    }

    @Override
    public String soortStuk(){
        return "toren";
    }

    @Override
    public List<Positie> geefZetten(Board board){
        int x = pos.x;
        int y = pos.y;
        List<Positie> zettenLijst = new ArrayList<>();
        for (int i = x+1; i < 8; i++){
            Schaakstuk piece = board.getStuk(i, y);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(i, y));
                }
                else{
                    zettenLijst.add(new Positie(i, y));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = x-1; i > -1; i--){
            Schaakstuk piece = board.getStuk(i, y);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(i, y));
                }
                else{
                    zettenLijst.add(new Positie(i, y));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = y+1; i < 8; i++){
            Schaakstuk piece = board.getStuk(x, i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x, i));
                }
                else{
                    zettenLijst.add(new Positie(x, i));
                    break;
                }
            }
            else {
                break;
            }
        }
        for (int i = y-1; i > -1; i--){
            Schaakstuk piece = board.getStuk(x, i);
            if(piece == null || piece.teamWhite == !teamWhite){//verschillende teams
                if(piece == null){
                    zettenLijst.add(new Positie(x, i));
                }
                else{
                    zettenLijst.add(new Positie(x, i));
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
