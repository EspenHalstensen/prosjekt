
import java.io.Serializable;

/**
 *
 * @author
 * havardb
 */
public class TreningsoktStatus implements Serializable {

    private boolean skalslettes;
    private Treningsokt treningsokt;

    public TreningsoktStatus() {
        treningsokt = new Treningsokt();
        skalslettes = false;
    }

    public TreningsoktStatus(Treningsokt t) {
        this.treningsokt = t;
         skalslettes = false;
    }

    public synchronized boolean getSkalslettes() {
       System.out.println("Get");
        return skalslettes;
    }

    public void setSkalslettes(boolean skalslettes) {
        System.out.println("set");
        this.skalslettes = skalslettes;
    }

    public  Treningsokt getTreningsokt() {
        return treningsokt;
    }

    public void setTreningsokt(Treningsokt treningsokt) {
        this.treningsokt = treningsokt;
    }
}
