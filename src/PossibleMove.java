import java.awt.*;

public class PossibleMove extends Move{
    int heuristicValue; //value of position after this move is done
    public PossibleMove(Schaakstuk stuk, Positie pos, int h){
        super(stuk, pos);
        heuristicValue = h;
    }
}
