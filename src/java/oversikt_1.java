
import java.util.ArrayList;

/**
 *
 * @author havardb
 */
public class oversikt_1 {

    private String brukernavn = "";
    ArrayList<Treningsokt> treningsokter = new ArrayList<Treningsokt>();

    public String getBrukernavn() {
        return brukernavn;
    }

    public Treningsokt getAlleOkter() {
        if (treningsokter != null) {
            for (Treningsokt t : treningsokter) {
                return t;
            }
        }
        return null;
    }

    public Treningsokt getAlleOkterEnMnd(String dato) { //"dd/MM/yyyy"
        if (treningsokter != null) {
            for (Treningsokt t : treningsokter) {
                if (dato.equals(t.getDato())) {
                    return t;
                }
            }
        }
        return null;
    }

    public void registrerNyOkt(Treningsokt t) {
        if (treningsokter != null) {
            treningsokter.add(t);
        }
    }
    public int getAntOkter(){
        return treningsokter.size();
    }
    public double getSum() {
        double sum = 0.0;
        for (Treningsokt t : treningsokter) {
            sum += t.getVarighet();
        }
        sum = sum / treningsokter.size();
        return sum;
    }

    public void slettOkt(Treningsokt t) {
        treningsokter.remove(t);
        
    }
}
