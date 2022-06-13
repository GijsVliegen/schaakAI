import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Schaakstuk {
    public Positie pos;
    public boolean teamWhite;
    public int value;
    public boolean promoted = false; //nodig voor het terugzetten van een promotie
    public Schaakstuk(int xer, int yer, Boolean team, int value) {
        pos = new Positie(xer, yer);
        teamWhite = team;
        this.value = value;
    }
    public boolean oppositeTeam(String string){
        if(teamWhite){
            return string.charAt(0) == 'z';
        }
        else{
            return string.charAt(0) == 'w';
        }
    }
    public boolean sameTeam(Board board, int x, int y){
        String string = board.getTeam(x, y);
        if(teamWhite){
            return string.charAt(0) == 'w';
        }
        else{
            return string.charAt(0) == 'z';
        }
    }
    //deze doet exacht hetzelfde als move, behalve dat de toren bij rokade niet wordt verplaatst
    public void heuristicMove(int new_x, int new_y){
        pos.x = new_x;
        pos.y = new_y;
    }
    public void move(int new_x, int new_y){
        pos.x = new_x;
        pos.y = new_y;
    }
    public boolean isCheck(){
        return false;
        //return secondBoard.isCheck();
    }
    public int getWaardePositie(int x, int y){
        return 0;
    }
    public String soortStuk(){
        return "defaultStuk";
    }
    public void drawStuk(){
        System.out.println("oei, is gewoon een stuk");
    }
    public List<Positie> geefZetten(Board board){
        return new ArrayList<>();
    }
}
