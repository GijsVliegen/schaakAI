import java.util.ArrayList;
import java.util.List;

public class Koningin extends Schaakstuk{
    public ImageResource image;
    private static final int[] KoninginValue = new int[]{
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20};
    public Koningin(int x, int y, Boolean team){
        super(x, y, team, 900);
        if (teamWhite){
            image = new ImageResource("resource/KoninginW.png");
        }
        else{
            image = new ImageResource("resource/KoninginZ.png");
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
        return KoninginValue[(7-temp_y)*8+x];
    }
    @Override
    public String soortStuk(){
        return "koningin";
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
        //loperzetten
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
