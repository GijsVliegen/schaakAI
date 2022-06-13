import java.util.ArrayList;
import java.util.List;

public class Paard extends Schaakstuk{
    public ImageResource image;
    private static final int[] PaardValue = new int[]{-50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50};
    public Paard(int x, int y, Boolean team){
        super(x, y, team, 300);
        if (teamWhite){
            image = new ImageResource("resource/PaardW.png");
        }
        else{
            image = new ImageResource("resource/PaardZ.png");
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
        return PaardValue[(7-temp_y)*8+x];
    }
    @Override
    public String soortStuk(){
        return "paard";
    }

    @Override
    public List<Positie> geefZetten(Board board){
        int x = pos.x;
        int y = pos.y;
        List<Positie> zettenLijst = new ArrayList<>();
        if (x < 7 && y < 6){
            if (!sameTeam(board, x+1 , y + 2)){
                zettenLijst.add(new Positie(x+1, y+2));
            }
        }
        if (x < 6 && y < 7){
            if (!sameTeam(board, x+2 , y + 1)){
                zettenLijst.add(new Positie(x+2, y+1));
            }
        }
        if (x < 7 && y > 1){
            if (!sameTeam(board, x+1 , y - 2)){
                zettenLijst.add(new Positie(x+1, y-2));
            }
        }
        if (x < 6 && y > 0){
            if (!sameTeam(board, x+2 , y - 1)){
                zettenLijst.add(new Positie(x+2, y-1));
            }
        }
        if (x > 0 && y < 6){
            if (!sameTeam(board, x-1 , y + 2)){
                zettenLijst.add(new Positie(x-1, y+2));
            }
        }
        if (x > 1 && y < 7){
            if (!sameTeam(board, x-2 , y + 1)){
                zettenLijst.add(new Positie(x-2, y+1));
            }
        }
        if (x > 0 && y > 1){
            if (!sameTeam(board, x-1 , y - 2)){
                zettenLijst.add(new Positie(x-1, y-2));
            }
        }
        if (x > 1 && y > 0){
            if (!sameTeam(board, x-2 , y - 1)){
                zettenLijst.add(new Positie(x-2, y-1));
            }
        }
        return zettenLijst;
    }
}
