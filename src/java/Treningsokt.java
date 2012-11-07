
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
/**
 *
 * @author Amund
 */
public class Treningsokt{
    private int oktnr;
    private String dato;
    private int varighet;
    private String kategori;
    private String tekst;
    private static int lopeNr = 0;
    private final String datoformat = "yyyy-MM-dd";
    
    public Treningsokt(int varighet, String kategori, String tekst){
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;
        dato = new SimpleDateFormat(datoformat).format(new GregorianCalendar().getTime());
        oktnr++;
    }
    
    public Treningsokt(){
        nullstill();
    }
            
    public String getDatoformat(){ return datoformat.toLowerCase(); }
    /**
     * @return the oktnr
     */
    public int getOktnr() {
        return oktnr;
    }

    /**
     * @param oktnr the oktnr to set
     */
    public void setOktnr(int oktnr) {
        this.oktnr = oktnr;
    }

    public static int setLopeNr() {
        lopeNr++;
        return lopeNr;
    }
    
    

    /**
     * @return the dato
     */
    public String getDato() {
        return dato;
    }

    /**
     * @param dato the dato to set
     */
    public void setDato(String dato) {
        this.dato = dato;
    }

    /**
     * @return the varighet
     */
    public int getVarighet() {
        return varighet;
    }

    /**
     * @param varighet the varighet to set
     */
    public void setVarighet(int varighet) {
        this.varighet = varighet;
    }

    /**
     * @return the kategori
     */
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String k){ this.kategori = k; }
    /**
     * @return the tekst
     */
    public String getTekst() {
        return tekst;
    }

    /**
     * @param tekst the tekst to set
     */
    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    
    public synchronized void nullstill(){
        varighet = 0;
        dato = new SimpleDateFormat(datoformat).format(new GregorianCalendar().getTime());
        tekst = "";
        kategori = "";
        oktnr++;
    }
}