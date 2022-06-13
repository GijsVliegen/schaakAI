

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class AISpeler {
    Board board;
    boolean teamWhite; //false
    public AISpeler(Board board, boolean teamWhite){
        this.board = board;
        this.teamWhite = teamWhite;
    }
    public int heuristicValueForAI() {
        int som = 0;
        int enemyPieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Schaakstuk stuk = board.getStuk(i, j);
                if (stuk != null && stuk.teamWhite == this.teamWhite) { //zelfde team
                    //en pure value van de positie van een stuk
                    som += stuk.getWaardePositie(i, j);
                    //pure value van een stuk hebben
                    som += stuk.value;
                }
                else if (stuk != null) { //andere team
                    //pure value van een stuk van de tegenstander dat bestaat
                    som -= stuk.value;
                    enemyPieces += stuk.value;
                }
                //aantal mogelijke zetten die de enemy koning kan doen, anders wordt er nooit schaakmat gezet
                //per zet die de koning kan doen, 20 eraf
                if (stuk instanceof Koning && stuk.teamWhite == !this.teamWhite){
                    som -= 10*board.sanitizeZettenOfPiece(stuk.geefZetten(board), stuk).size();
                }
            }
        }
        int endgameWeight = 1;
        if(enemyPieces < 21400){//de tegenspeler heeft niet meer veel stukken = endgame
            //in de hoek drijven -> sneller schaakmat
            endgameWeight = 10;

        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Schaakstuk stuk = board.getStuk(i, j);
                if (stuk instanceof Koning && stuk.teamWhite == teamWhite){
                    som -= 10*endgameWeight*board.sanitizeZettenOfPiece(stuk.geefZetten(board), stuk).size();
                }
                if (enemyPieces < 21400){
                    som += Math.pow((3.5 - i), 2) + Math.pow((3.5-j),2)*5;
                }
            }
        }

        return som;
    }
    public int calculateLastLayer(Move move, boolean[] booleans, boolean[] enPasBools){
        int oldX = move.stuk.pos.x;
        int oldY = move.stuk.pos.y;
        int newX = move.newPos.x;
        int newY = move.newPos.y;
        int valueOfPosition;

        //do move
        Schaakstuk possiblePieceRemoved = board.doMove(move);

        valueOfPosition = heuristicValueForAI();
        /*if (move.stuk.teamWhite){
            valueOfPosition = -1*valueOfPosition; //omdat het de waarde van het andere team is
        }*/

        //undo move
        board.heuristicUndoMove(oldX, oldY, newX, newY, possiblePieceRemoved, booleans, enPasBools);
        return valueOfPosition;
    }

    public int goOneLayerDeeper(Move move, boolean searchMax, int deeperLayers, boolean[] booleans, boolean[] enPasBools){//true als we de hoogste zoeken, false als we de laagste zoeken

        int oldX = move.stuk.pos.x;
        int oldY = move.stuk.pos.y;
        int newX = move.newPos.x;
        int newY = move.newPos.y;

        //do move
        Schaakstuk possiblePieceRemoved = board.doMove(move);

        int heuristicOfDeeperLayer = getBestOfAllMoves(!move.stuk.teamWhite, deeperLayers-1, !searchMax).heuristicValue;

        //undo move
        board.heuristicUndoMove(oldX, oldY, newX, newY, possiblePieceRemoved, booleans, enPasBools);//do move

        return heuristicOfDeeperLayer;
    }
    //we hebben nu geprogrammeerd dat we de beste move vinden voor team zwart
    //rekening houdend met de volgende diepte, moeten we opzoek gaan naar de beste move
    //die aan wit de slechtse moves geeft, weliswaar waar de beste move van wit het laagste is.

    // -> stel dat we 3 zetten, zwart_1 -> wit -> zwart_2 zoeken
    // -> zwart_2 kiest de maximale Move -> wit kiest de minimale move van al deze max moves
    // -> zwart_2 kiest opnieuw de maximale moves
    // de beste move voor zwart tov zwart is de slechtste move die zwart kan doen tov wit
    // -> het minimale van de witte moves -> de laagste voor wit

    //bij elke zet telkens eerst diep gaan, en dan naar boven werken om de beste move te vinden
    public PossibleMove getBestOfAllMoves(boolean team, int deeperLayers, boolean searchMax){;
        PossibleMove bestMove = new PossibleMove(null, null, -99999);
        if(team){
            bestMove.heuristicValue = 99999; //als we zoeken naar min value -> beginnen met goede move
        }
        boolean[] booleans = new boolean[]{board.schaakWit, board.schaakZwart, board.rokadeWitLMogelijk, board.rokadeWitRMogelijk, board.rokadeZwartLMogelijk, board.rokadeZwartRMogelijk};
        boolean[] enPasBools = board.getEnPassentBools(team);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Schaakstuk stuk = board.getStuk(i, j);
                if (stuk != null && stuk.teamWhite == team) {
                    List<Positie> alleMogelijkeZetten = stuk.geefZetten(board);
                    alleMogelijkeZetten = board.sanitizeZettenOfPiece(alleMogelijkeZetten, stuk);
                    int currentHeuristicValue;
                    for(Positie pos: alleMogelijkeZetten) {
                        //ofwel nog dieper gaan
                        //ofwel de waarde van elke positie bekijken
                        if(deeperLayers > 1){
                            currentHeuristicValue = goOneLayerDeeper(new Move(stuk, pos), searchMax, deeperLayers, booleans, enPasBools);
                        }
                        else{
                            currentHeuristicValue = calculateLastLayer(new Move(stuk, pos), booleans, enPasBools);//heuristicValue();
                        }
                        if((searchMax && currentHeuristicValue > bestMove.heuristicValue) ||
                                (!searchMax && currentHeuristicValue < bestMove.heuristicValue)){
                            bestMove.newPos = pos;
                            bestMove.heuristicValue = currentHeuristicValue;
                            bestMove.stuk = stuk;
                        }
                    }
                }
            }
        }
        if(bestMove.newPos == null){
            //de AI heeft verloren want hij kan geen zet meer doen
        }
        return bestMove;
    }
    //momenteel werkt ie terug voor 1 laag, maar dat is uiteraard niet genoeg

    public Move getOptimalMove(){
        return getBestOfAllMoves(teamWhite, 2, true);
    }
}
