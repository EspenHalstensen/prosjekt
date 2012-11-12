package problemdomenet;


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
        return skalslettes;
    }

    public void setSkalslettes(boolean skalslettes) {
        this.skalslettes = skalslettes;
    }

    public Treningsokt getTreningsokt() {
        return treningsokt;
    }

    public void setTreningsokt(Treningsokt treningsokt) {
        this.treningsokt = treningsokt;
    }
}
