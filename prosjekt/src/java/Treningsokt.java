
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
    private final String datoformat = "dd/MM/yyyy";
    
    public Treningsokt(){
        dato = new SimpleDateFormat(datoformat).format(new GregorianCalendar().getTime());
        oktnr++;
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

    public void setKategori(String k){ kategori = k; }
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

    /**
     * @return the kategorier
     */
    public Kategorier getKategorier() {
        return kategorier;
    }

    /**
     * @param kategorier the kategorier to set
     */
    public void setKategorier(Kategorier kategorier) {
        this.kategorier = kategorier;
    }
}
