public class UndoMove extends Move{
    Positie oldPos;
    Schaakstuk removedPiece;
    boolean[] booleans;
    boolean[] enPasBools;
    public UndoMove(Move move, Positie oldPos, Schaakstuk removedPiece,
                    boolean[] booleans, boolean[] enPasBools){
        super(move.stuk, move.newPos);
        this.oldPos = oldPos;
        this.removedPiece = removedPiece;
        this.booleans = booleans;
        this.enPasBools = enPasBools;
    }

    public void setOldPos(Positie oldPos) {
        this.oldPos = oldPos;
    }
    public void setRemovedPiece(Schaakstuk removedPiece){
        this.removedPiece = removedPiece;
    }
}
