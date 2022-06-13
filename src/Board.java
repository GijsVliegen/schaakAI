import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public Schaakstuk[][] stukken = new Schaakstuk[8][8];
    public boolean rokadeWitLMogelijk = true;
    public boolean rokadeWitRMogelijk = true;
    public boolean rokadeZwartLMogelijk = true;
    public boolean rokadeZwartRMogelijk = true;
    public boolean schaakWit = false;
    public boolean schaakZwart = false;
    public boolean someoneHasWon = false;
    public Positie laatstePosGeklikt;
    public List<Schaakstuk> removedPiecesWhite;
    public List<Schaakstuk> removedPiecesBlack;
    public List<Positie> mogelijkeZetten;
    public Board(List<Schaakstuk> stukken){
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                this.stukken[i][j] = null;
            }
        }
        for (Schaakstuk stuk: stukken){
            this.stukken[stuk.pos.x][stuk.pos.y] = stuk;
        }
        laatstePosGeklikt = null;
        mogelijkeZetten = new ArrayList<>();
        removedPiecesWhite = new ArrayList<>();
        removedPiecesBlack = new ArrayList<>();
    }

    public void updateEnPassant(boolean teamWit){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Schaakstuk stuk = stukken[x][y];
                if (stuk != null && stuk.teamWhite == teamWit){
                    if (stuk instanceof Pion) { //ofwel beide false (zwart) of beide true (wit)
                        ((Pion) stuk).en_passent_possible = false;
                    }
                }
            }
        }
    }

    public void updateRokades(boolean teamWit){
        if(teamWit) {
            if (rokadeWitLMogelijk && !(getStuk(0, 0) instanceof Toren) ||
                    !(getStuk(4, 0) instanceof Koning)) {
                rokadeWitLMogelijk = false;
            }
            if (rokadeWitRMogelijk && !(getStuk(7, 0) instanceof Toren) ||
                    !(getStuk(4, 0) instanceof Koning)) {
                rokadeWitRMogelijk = false;
            }
        }
        else{
            if (rokadeZwartLMogelijk && !(getStuk(0, 7) instanceof Toren) ||
                    !(getStuk(4, 7) instanceof Koning)) {
                rokadeZwartLMogelijk = false;
            }
            if (rokadeZwartRMogelijk && !(getStuk(7, 7) instanceof Toren) ||
                    !(getStuk(4, 7) instanceof Koning)) {
                rokadeZwartRMogelijk = false;
            }
        }
    }
    private void updateSchaak(List<Positie> enemyMoves, Boolean ownTeam){
        //System.out.println("kijken of schaak");
        int koningX = 8;
        int koningY = 8;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (stukken[i][j] instanceof Koning && stukken[i][j].teamWhite == ownTeam){
                    koningX = i;
                    koningY = j;
                }
            }
        }
        if (enemyMoves.contains(new Positie(koningX, koningY))){
            if(ownTeam){
                schaakWit = true;
                //System.out.println("wit is schaak");
            }
            else{
                schaakZwart = true;
                //System.out.println("zwart is schaak");
            }
        }
        else{
            if(ownTeam){
                schaakWit = false;
                //System.out.println("wit is niet schaak");
            }
            else{
                schaakZwart = false;
                //System.out.println("zwart is niet schaak");
            }

        }
    }
    public void checkIfWon(boolean teamWhite){
        List<Positie> mogelijkeTegenZetten = getTeamMovesSanitised(!teamWhite);
        if (mogelijkeTegenZetten.size() == 0) {
            if (teamWhite) {
                System.out.println("team wit heeft gewonnen hoera");
            }
            else {
                System.out.println("team zwart heeft gewonnen hoera");
            }
            someoneHasWon = true;
        }
    }
    public void promote(Schaakstuk stuk){
        System.out.println("gepromote");
    }
    public void depromote(Schaakstuk stuk) {System.out.println("gedepromote"); }
    //removedPiece moet worden teruggegeven zodat de EnPassentZet
    // ook teruggedaan kan worden
    public Schaakstuk doMove(Move move){
        Schaakstuk removedPiece = null;
        if(someoneHasWon){
            return removedPiece;
        }
        Schaakstuk schaakstuk = move.stuk;
        int new_x = move.newPos.x;
        int new_y = move.newPos.y;
        int old_x = schaakstuk.pos.x;
        int old_y = schaakstuk.pos.y;
        updateEnPassant(schaakstuk.teamWhite); //dit moet gebeuren voor de
        //enPassent van de huidige zet wordt gedaan

        //voor een pion -> en passent -> remove iets anders
        //ook voor een pion -> 2 stappen vooruit -> en passent mogelijk
        //ook voor een pion -> promotie
        int temp_new_y = 5;
        if (!schaakstuk.teamWhite){
            temp_new_y = 4;
        }
        if (schaakstuk instanceof Pion){
            //pakt een stuk en passant
            if (new_y == temp_new_y && new_x != old_x && getStuk(new_x, new_y) == null) {
                removedPiece = getStuk(new_x, old_y);
                remove(new_x, old_y);
            }
            //promotie
            else if (new_y == 7 || new_y == 0){
                promote(schaakstuk);
            }
            //en passant true zetten
            else if (Math.abs(new_y - old_y) == 2){
                ((Pion) schaakstuk).en_passent_possible = true;
            }
        }

        //general removen van stuk
        if (getStuk(new_x, new_y) != null){
            removedPiece = getStuk(new_x, new_y);
            remove(new_x, new_y);
        }

        //rokade -> ook toren bewegen
        if (schaakstuk instanceof Koning && Math.abs(new_x - old_x) == 2){
            if(new_x == 2){
                Schaakstuk toren = getStuk(0, new_y);
                toren.heuristicMove(3, new_y);
                stukken[0][new_y] = null;
                stukken[3][new_y] = toren;
            }
            else if(new_x == 6){
                Schaakstuk toren = getStuk(7, new_y);
                toren.heuristicMove(5, new_y);
                stukken[7][new_y] = null;
                stukken[5][new_y] = toren;
            }
            else{
                System.out.println("er is iets fout bij heuristicMove Rokade");
            }
        }

        //general moven van stuk op bord en stuk zelf
        schaakstuk.move(new_x, new_y);
        stukken[old_x][old_y] = null;
        stukken[new_x][new_y] = schaakstuk;

        //updaten van booleans
        updateRokades(schaakstuk.teamWhite);
        List<Positie> ownTeamMoves = getTeamMoves(schaakstuk.teamWhite);
        updateSchaak(ownTeamMoves, !schaakstuk.teamWhite); //kijken of het andere team is schaak gezet
        //er moet ook gekeken worden of de speler zelf niet schaak staat, dit komt omdat deze functie
        //ook gebruikt wordt door de sanitizer
        List<Positie> enemyTeamMoves = getTeamMoves(!schaakstuk.teamWhite);
        updateSchaak(enemyTeamMoves, schaakstuk.teamWhite);
        return removedPiece;
    }

    public void zetAanZijkant(Schaakstuk stuk){
        if(stuk.teamWhite){
            removedPiecesWhite.add(stuk);
            stuk.pos.x = (removedPiecesWhite.size()-1)%4 + 8; //-> telkens 4 stukken op x-as, dan y-as 1 naar boven
            stuk.pos.y = (removedPiecesWhite.size()-1) / 4;
        }
        else{
            removedPiecesBlack.add(stuk);
            stuk.pos.x = (removedPiecesBlack.size()-1)%4 + 8;
            stuk.pos.y = 15 - (removedPiecesBlack.size()-1) / 4;
        }
    }
    public void remove(int x, int y){
        //het is mogelijk ze via hier aan de zijkant te tekenen -> done
        //we kunnen dit best verplaatsen naar ergens waar het
        //definitief is dat de move echt is gedaan, anders is dat kut bij het
        //undo'en van de move
        //verder kan dan de x en y gewoon blijven opgeslagen in het Schaakstuk
        //wat het dan makkelijker maakt om het terug te plaatsen op het bord
        // -> allemaal done

        //kan bijvoorbeeld naar Game.zetaanZijkant() worden verplaatst
        //omdat het removedPiece toch wordt gereturned -> ook done

        //als een toren wordt gepakt -> rokade onmogelijk
        Schaakstuk stuk = stukken[x][y];
        if(stuk == null){
            return;
        }
        if(stuk.teamWhite){
            if(stuk instanceof Toren && x == 0 && y == 0){
                rokadeWitLMogelijk = false;
            }
            else if(stuk instanceof Toren && x == 7 && y == 0){
                rokadeWitRMogelijk = false;
            }
        }
        else{
            if(stuk instanceof Toren && x == 0 && y == 7){
                rokadeZwartLMogelijk = false;
            }
            else if(stuk instanceof Toren && x == 7 && y == 7){
                rokadeZwartRMogelijk = false;
            }
        }
        stukken[x][y] = null;
    }
    public Move handleMousePress(int x, int y, boolean spelerbeurt) {
        //game.spelerbeurt = true -> beurt aan wit
        //game.spelerbeurt = false -> beurt aan zwart){
        System.out.println("muis geklikt, x = " + x + ", y = " + y);
        int oldX = 8;
        int oldY = 8;
        if (laatstePosGeklikt != null){
            oldX = laatstePosGeklikt.x;
            oldY = laatstePosGeklikt.y;
        }
        laatstePosGeklikt = new Positie(x, y);
        Schaakstuk stuk = stukken[x][y];
        if (mogelijkeZetten.contains(laatstePosGeklikt)){
            Schaakstuk laatsteStukPressed = stukken[oldX][oldY];
            //System.out.println("move gevonden, x = " + x + ", y = " + y);
            //System.out.println("laatste stuk pressed x = " + laatsteStukPressed.pos.x + ", y = " + laatsteStukPressed.pos.y);
            laatstePosGeklikt = null;
            mogelijkeZetten.clear();
            return new Move(laatsteStukPressed, new Positie(x, y));
        }
        else if (stuk != null && spelerbeurt == stuk.teamWhite){ //beide true -> wit stuk voor witte speler, beide false -> zwart stuk voor zwarte speler

            //printBordInstantie("voor");
            mogelijkeZetten = stuk.geefZetten(Game.board);
            mogelijkeZetten = sanitizeZettenOfPiece(mogelijkeZetten, stuk);
            //printBordInstantie("na");
        }
        else{//gewoon ergens random geklikt
            mogelijkeZetten.clear();
            laatstePosGeklikt = null;
        }
        return null;
    }
    public List<Positie> getTeamMoves(boolean teamWit){
        List<Positie> allZettenLijst = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Schaakstuk stuk = stukken[i][j];
                if (stuk != null && stuk.teamWhite == teamWit){
                    allZettenLijst.addAll(stuk.geefZetten(this));
                }
            }
        }
        return allZettenLijst;
    }

    public void heuristicUndoMove(int prev_x, int prev_y, int new_x, int new_y,
                                  Schaakstuk lostPiece, boolean[] booleans, boolean[] enPasBools){
        Schaakstuk stukMoved = getStuk(new_x, new_y);
        stukMoved.heuristicMove(prev_x, prev_y);
        stukken[prev_x][prev_y] = stukMoved;
        stukken[new_x][new_y] = null;
        if(lostPiece != null){
            stukken[lostPiece.pos.x][lostPiece.pos.y] = lostPiece;//meestal zal dit
        }
        //new_x en new_y zijn, maar bijvoorbeeld als er enpassent moet teruggezet worden niet

        //bij promotie het stuk terug depromoten
        if(stukMoved.promoted){
            if (new_y == 7 || new_y == 0){
                depromote(stukMoved);
            }
        }

        //als een pion 2 stappen vooruit deed, moet de enPassentPossible terug false worden gezet
        if (stukMoved instanceof Pion){
            if (Math.abs(new_y - prev_y) == 2){
                ((Pion) stukMoved).en_passent_possible = false;
            }
        }

        //bij rokade de toren ook terug zetten
        if(stukMoved instanceof Koning && Math.abs(new_x - prev_x) == 2){//rokade
            if(new_x == 2){
                Schaakstuk Toren = getStuk(3, new_y);
                Toren.heuristicMove(0, new_y);
                stukken[3][new_y] = null;
                stukken[0][new_y] = Toren;
            }
            else if(new_x == 6){
                Schaakstuk Toren = getStuk(5, new_y);
                Toren.heuristicMove(7, new_y);
                stukken[5][new_y] = null;
                stukken[7][new_y] = Toren;
            }
            else{
                System.out.println("er is iets fout bij heuristicUndoMove Rokade");
            }
        }
        //eerst move terugdoen en dan bools terugzetten
        schaakWit = booleans[0];
        schaakZwart = booleans[1];
        rokadeWitLMogelijk = booleans[2];
        rokadeWitRMogelijk = booleans[3];
        rokadeZwartLMogelijk = booleans[4];
        rokadeZwartRMogelijk = booleans[5];

        //enkel pionen die op rij 4 (of 5) staan, kunnen mogelijks enPassent true hebben
        int temp_y = 4;
        if (!stukMoved.teamWhite){
            temp_y = 5;
        }
        for (int i = 0; i < 8; i++){
            if (getStuk(i, temp_y) instanceof Pion){
                ((Pion)getStuk(i, temp_y)).en_passent_possible = enPasBools[i];
            }
        }
    }
    public List<Positie> getTeamMovesSanitised(boolean teamWhite){
        List<Positie> allZettenLijst = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Schaakstuk stuk = stukken[i][j];
                if (stuk != null && stuk.teamWhite == teamWhite){
                    List<Positie> mogelijkeZettenUnsanitized = stuk.geefZetten(this);
                    allZettenLijst.addAll(sanitizeZettenOfPiece(mogelijkeZettenUnsanitized, stuk));
                }
            }
        }
        return allZettenLijst;

    }
    public List<Positie> sanitizeZettenOfPiece(List<Positie> mogelijkeZetten, Schaakstuk stuk){
        //er moet gedaan worden alsof de zet wordt gedaan, en dan kijken of er in die positie nog steeds schaak is
        //als dat zo is moet die zet verwijderd worden
        List<Positie> removeList = new ArrayList<>();
        //System.out.println("aantal mogelijke zetten voor sanitize() = " + mogelijkeZetten.size());
        int oldX = stuk.pos.x;
        int oldY = stuk.pos.y;
        boolean[] booleans = new boolean[]{schaakWit, schaakZwart, rokadeWitLMogelijk, rokadeWitRMogelijk, rokadeZwartLMogelijk, rokadeZwartRMogelijk};
        boolean[] enPasBools = getEnPassentBools(stuk.teamWhite);
        for(Positie pos: mogelijkeZetten) {
            Schaakstuk possiblePieceRemoved = doMove(new Move(stuk, pos));
            if ((stuk.teamWhite && schaakWit) || (!stuk.teamWhite && schaakZwart)) {
                removeList.add(pos);
            }
            heuristicUndoMove(oldX, oldY, pos.x, pos.y, possiblePieceRemoved, booleans, enPasBools);
        }
        mogelijkeZetten.removeAll(removeList);
        //System.out.println("aantal mogelijke zetten na sanitize() = " + mogelijkeZetten.size());
        return mogelijkeZetten;
    }
    public boolean[] getEnPassentBools(boolean teamWhite){
        int temp_y = 4;
        if (!teamWhite){
            temp_y = 5;
        }
        boolean[] bool = new boolean[8];
        for (int i = 0; i < 8; i++){
            if (getStuk(i, temp_y) instanceof Pion){
                bool[i] = ((Pion) getStuk(i, temp_y)).en_passent_possible;
            }
            else{
                bool[i] = false;
            }
        }
        return bool;
    }
    public Schaakstuk getStuk(int x, int y){
        return stukken[x][y];
    }
    public String getTeam(int x, int y){
        Schaakstuk stuk = getStuk(x, y);
        if (stuk == null){
            return "empty";
        }
        if (stuk.teamWhite){
            return "w";
        }
        return "z";
    }
    public void drawRemovedPieces(){
        for (Schaakstuk stuk: removedPiecesWhite){
            stuk.drawStuk();
        }
        for (Schaakstuk stuk: removedPiecesBlack){
            stuk.drawStuk();
        }

    }
    public void drawMogelijkeZetten(){
        if (laatstePosGeklikt != null){
            Graphics.setColor(0.5f, .5f, .5f, .9f);
            for(Positie pos: mogelijkeZetten){
                Graphics.fillRect(pos.x +.3f, pos.y+.3f, .4f, .4f);
            }
        }
    }
    public void drawBoard(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if ((i+j)%2 == 0){
                    Graphics.setColor(.5f, .5f, 0, 1);
                }
                else{
                    Graphics.setColor(0, .5f, .5f, 1);
                }
                Graphics.fillRect(i, j, 1, 1);
                Schaakstuk stuk = stukken[i][j];
                if (stuk != null){
                    stuk.drawStuk();
                }
            }
            for(int j = 8; j < 10; j++){
                Graphics.setColor(.5f, .5f, .5f, 1);
                Graphics.fillRect(j, i, 1, 1);
            }
        }
        drawMogelijkeZetten();
        drawRemovedPieces();
    }
    public void printBordInstantie(String bericht){
        System.out.println(bericht);
        System.out.println("rokadeWitLMogelijk: " + rokadeWitLMogelijk);
        System.out.println("rokadeWitRMogelijk: " + rokadeWitRMogelijk);
        System.out.println("rokadeZwartLMogelijk: " + rokadeZwartLMogelijk);
        System.out.println("rokadeZwartRMogelijk: " + rokadeZwartRMogelijk);
        System.out.println("witSchaak: " + schaakWit);
        System.out.println("zwartSchaak: " + schaakZwart);
        System.out.println("mogelijke zetten");
        for (Positie pos: mogelijkeZetten){
            System.out.println("x = " + pos.x + ", y = " + pos.y);
        }
        if (laatstePosGeklikt != null){
            System.out.println("laatste pos geklikt: x = " + laatstePosGeklikt.x + ", y = " + laatstePosGeklikt.y);
        }
    }
}
