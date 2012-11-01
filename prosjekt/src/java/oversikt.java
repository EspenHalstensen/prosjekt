
import java.util.ArrayList;

/**
 *
 * @author
 * havardb
 */
public class oversikt {
    
    private String brukernavn;
    ArrayList<Treningsokt> treningsokter = new ArrayList<Treningsokt>();
    
    public oversikt(String brukernavn) {
        this.brukernavn = brukernavn;
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
    
    public boolean registrerNyOkt(int oktnr, String dato, int varighet, String kategori, String tekst) {
        if (treningsokter != null) {
                treningsokter.add(new Treningsokt(oktnr, dato, varighet, kategori, tekst));
                return true;
        }
        return false;
    }
    
    public boolean slettOkt(int oktnr) {
        for (Treningsokt t : treningsokter) {
            if (t.getOktnr() == oktnr) {
                treningsokter.remove(t);
                return true;
            }
        }
        return false;        
    }
}
