import java.util.Objects;

public class Positie {
    public int x;
    public int y;
    public Positie(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Positie)){
            return false;
        }
        Positie positie = (Positie)object;
        return x == positie.x && y == positie.y;
    }
}
