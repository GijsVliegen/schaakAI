import java.lang.reflect.Array;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

public class Koning extends Schaakstuk{
    public ImageResource image;
    private static final int[] KoningValue = new int[]{
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20};
    public Koning(int x, int y, Boolean team){
        super(x, y, team, 20000);
        if (teamWhite){
            image = new ImageResource("resource/KoningW.png");
        }
        else{
            image = new ImageResource("resource/KoningZ.png");
        }
    }

    @Override
    public int getWaardePositie(int x, int y){
        int temp_y = y;
        if(!teamWhite){
            temp_y = 7-y;
        }
        return KoningValue[(7-temp_y)*8+x];
    }
    @Override
    public void drawStuk(){
        Graphics.draw(this, image);
    }

    @Override
    public String soortStuk(){
        return "koning";
    }

    @Override
    public List<Positie> geefZetten(Board board){
        int x = pos.x;
        int y = pos.y;
        List<Positie> zettenLijst = new ArrayList<>();
        for (int i = Math.max(0, x-1); i < Math.min(7, x+1) + 1; i++) {
            for (int j = Math.max(0, y - 1); j < Math.min(7, y + 1) + 1; j++) {
                if (!sameTeam(board, i, j)){
                    zettenLijst.add(new Positie(i, j));
                }
            }
        }
        int temp_y = 0;
        boolean schaak = board.schaakWit;
        if (!teamWhite){
            temp_y = 7;
            schaak = board.schaakZwart;
        }
        if ((teamWhite && board.rokadeWitLMogelijk) || (!teamWhite && board.rokadeZwartLMogelijk)){
            if (board.getTeam(3, temp_y).equals("empty") && board.getTeam(2, temp_y).equals("empty") && board.getTeam(1, temp_y).equals("empty")
                    && !schaak){//deze laatste check is voor
                //als je onder schaak staat, want dan mag je geen rokade doen
                zettenLijst.add(new Positie(2, temp_y));
            }
            //check of rokade wel gaat, met ander team dat het kan aanvallen;
            //dit gebeurt in board.sanitize
        }
        if ((teamWhite && board.rokadeWitRMogelijk) || (!teamWhite && board.rokadeZwartLMogelijk)){
            if (board.getTeam(5, temp_y).equals("empty") && board.getTeam(6, temp_y).equals("empty")
                    && !schaak){//deze laatste check is voor
                //als je onder schaak staat, want dan mag je geen rokade doen
                zettenLijst.add(new Positie(6, temp_y));
            }
            //check of rokade wel gaat, met ander team dat het kan aanvallen;
            //dit gebeurt in board.sanitize
        }
        return zettenLijst;
    }
}
