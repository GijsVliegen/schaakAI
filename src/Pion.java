import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pion extends Schaakstuk{
    private static final int[] PionValue = new int[]{0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0};
    public ImageResource image;
    public boolean en_passent_possible = false;
    public Pion(int x, int y, Boolean team){
        super(x, y, team, 100);
        if (teamWhite){
            image = new ImageResource("resource/PionW.png");
        }
        else{
            image = new ImageResource("resource/PionZ.png");
        }
    }

    @Override
    public int getWaardePositie(int x, int y){
        int temp_y = y;
        if(!teamWhite){
            temp_y = 7-y;
        }
        return PionValue[(7-temp_y)*8+x];
    }
    @Override
    public void drawStuk(){
        Graphics.draw(this, image);
    }

    @Override
    public String soortStuk(){
        return "pion";
    }

    @Override
    public List<Positie> geefZetten(Board board){
        int x = pos.x;
        int y = pos.y;
        int temp_y = y;
        int inverted = 1;
        if (!teamWhite){
            temp_y = 7 - y;
            inverted = -1;
        }
        List<Positie> zettenLijst = new ArrayList<>();
        if (temp_y != 7){
            Schaakstuk voor = board.getStuk(x, y+inverted*1);        //de zet naar voren
            if (voor == null){
                zettenLijst.add(new Positie(x, y+inverted*1));
                if (temp_y == 1){                                       //dubbele zet in het begin
                    Schaakstuk dubbelVoor = board.getStuk(x, y+inverted*2);
                    if (dubbelVoor == null){
                        zettenLijst.add(new Positie(x, y+inverted*2));
                    }
                }
            }
            if (x > 0) {
                Schaakstuk linksVoor = board.getStuk(x - 1, y + inverted*1);
                if (linksVoor != null && linksVoor.teamWhite == !teamWhite) {                //verschillende teams
                    zettenLijst.add(new Positie(x-1, y+inverted*1));
                }
                else {
                    if (temp_y == 4) {
                        Schaakstuk links = board.getStuk(x -1, y);//, !(inverted == 1)); //het kan niet zijn dat er een pion van zelfde team staat waar enpassant mogelijk is
                        if (links instanceof Pion && ((Pion) links).en_passent_possible) {
                            zettenLijst.add(new Positie(x-1, y+inverted*1));
                        }
                    }
                }
            }
            if (x < 7) {
                Schaakstuk rechtsVoor = board.getStuk(x + 1, y + inverted * 1);
                if (rechtsVoor != null && rechtsVoor.teamWhite == !teamWhite) {               //verschillende teams
                    zettenLijst.add(new Positie(x+1, y+inverted*1));
                }
                else {
                    if (temp_y == 4) {
                        Schaakstuk rechts = board.getStuk(x + 1, y);//, !(inverted == 1)); //het kan niet zijn dat er een pion van zelfde team staat waar enpassant mogelijk is
                        if (rechts instanceof Pion && ((Pion) rechts).en_passent_possible) {
                            zettenLijst.add(new Positie(x+1, y+inverted*1));
                        }
                    }
                }
            }
        }
        return zettenLijst;
    }
}
