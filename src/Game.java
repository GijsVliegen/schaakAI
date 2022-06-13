import java.util.ArrayList;
import java.util.List;

public class Game {
    boolean spelerbeurt = true;
    List<Schaakstuk> spelerstukken = new ArrayList<>();
    AISpeler AI;
    boolean AI_AAN = true;
    public static Board board;
    public List<UndoMove> allMoves = new ArrayList<>();
    public int counter = 0;
    /* Done van de To do
        - en passant fiksen -> done
        - rokade fiksen -> done
        - schaak en schaakmat fiksen -> done
        - bordstructuur aanpassen zodat board 8*8 vakjes opslaat zodat stukken zoeken sneller is -> done
        - om beurten laten spelen -> done
        - verwijderde stukken langs het bord zetten -> done
        - ai maken die 1 stap kan vooruitdenken -> done
     */
    /*Todo
        - x en y uit schaakstukken verwijderen
        - rokade fiksen bij ht vooruitdenken (checkNextMove) en pion en passant aanpassingen -> done
        - ai meerdere stappen vooruit laten denken -> done
        - code mooier maken -> done
            - game.move verwijderen ofzo
            - game uit board.handleMousePressed halen
        - PossibleMove classe uitbreiden
        - AI optimaliseren
            - prunen van alpha beta boom
            - als er nog captures mogelijk zijn, blijven verder denken (dieper)
        - met pijltjes terug kunnen gaan in zetten
     */
    public void initStukken(){
        //speler is wit, bord loopt van 0 tot 8
        int h = 7;
        Boolean t = false;
        for(int d = 0; d < 2; d++){//eerst die voor zwart, dan die voor wit
            if (d == 1) {
                h = 0;
                t = true;
            }
            spelerstukken.add(new Koning(4,h,t));
            spelerstukken.add(new Koningin(3,h,t));
            for(int i = -1; i < 2; i = i+2){
                spelerstukken.add(new Toren((int) (3.5-i*3.5),h,t));
                spelerstukken.add(new Loper((int) (3.5-i*1.5),h,t));
                spelerstukken.add(new Paard((int) (3.5-i*2.5),h,t));
            }
        }
        for(int i = 0; i < 8; i++){
            spelerstukken.add(new Pion(i, 1, true));
            spelerstukken.add(new Pion(i, 6, false));
        }
    }

    public void goMoveBackwards(){
        System.out.println(counter);
        if (counter == 0){
            return;
        }
        counter = counter - 1;
        UndoMove moveToDo = allMoves.get(counter);
        board.heuristicUndoMove(moveToDo.oldPos.x, moveToDo.oldPos.y, moveToDo.newPos.x, moveToDo.newPos.y,
                moveToDo.removedPiece, moveToDo.booleans, moveToDo.enPasBools);
        if(AI_AAN && counter % 2 == 1){ //als AI aan staat altijd 2 zetten tegelijk terug doen
            goMoveBackwards();
        }
    }

    public void goMoveForwards(){
        System.out.println(counter);
        if (counter == allMoves.size()) {
            return;
        }
        UndoMove moveToDo = allMoves.get(counter);
        counter++;
        board.doMove(new Move(moveToDo.stuk, moveToDo.newPos));
        if(AI_AAN && counter % 2 == 1){ //als AI aan staat altijd 2 zetten tegelijk terug doen
            goMoveForwards();
        }
    }
    public void saveMove(Move move, Positie oldPos, Schaakstuk removedPiece){
        counter = counter + 1;
        boolean[] booleans = new boolean[]{board.schaakWit, board.schaakZwart, board.rokadeWitLMogelijk, board.rokadeWitRMogelijk, board.rokadeZwartLMogelijk, board.rokadeZwartRMogelijk};
        boolean[] enPasBools = board.getEnPassentBools(move.stuk.teamWhite);
        UndoMove undoMove = new UndoMove(move, oldPos, removedPiece, booleans, enPasBools);
        allMoves.add(undoMove);
        //enPasBools...
    }
    public void doOneTurn(Move whiteMove){ //name is niet accurate als AI af staat
        if (counter != allMoves.size()){
            allMoves.subList(0, counter);
        }
        if (board.someoneHasWon){
            return;
        }
        Positie oldPos = new Positie(whiteMove.stuk.pos.x, whiteMove.stuk.pos.y);
        Schaakstuk removedPiece = board.doMove(whiteMove);
        saveMove(whiteMove, oldPos, removedPiece);
        if(removedPiece != null){
            board.zetAanZijkant(removedPiece);
        }
        board.checkIfWon(spelerbeurt);
        spelerbeurt = !spelerbeurt;
        if (AI_AAN){
            Move bestEnemyMove = AI.getOptimalMove();
            oldPos = new Positie(bestEnemyMove.stuk.pos.x, bestEnemyMove.stuk.pos.y);
            Schaakstuk otherRemovedPiece = board.doMove(bestEnemyMove);
            saveMove(bestEnemyMove, oldPos, otherRemovedPiece);
            if(otherRemovedPiece != null){
                board.zetAanZijkant(otherRemovedPiece);
            }
            board.checkIfWon(spelerbeurt);
            spelerbeurt = !spelerbeurt;
        }

    }
    public void mousePress(int x, int y){
        int x_coord = (int) Math.floor((float)x/Renderer.getWindowWidth()*Renderer.unitsWide);
        int y_coord = (int)Renderer.getUnitsTall()-1-(int)Math.floor((float)y/Renderer.windowHeight*Renderer.getUnitsTall());
        if(!board.someoneHasWon && x_coord >= 0 && x_coord < 8 && y_coord >= 0 && y_coord < 8){
            Move move = board.handleMousePress(x_coord, y_coord, this.spelerbeurt);
            if (move != null){
                doOneTurn(move);
            }
        }
    }
    public Game(){
        initStukken();
        board = new Board(spelerstukken);
        AI = new AISpeler(board, !spelerbeurt);
    }
    public void loop(){
        board.drawBoard();
    }
}
