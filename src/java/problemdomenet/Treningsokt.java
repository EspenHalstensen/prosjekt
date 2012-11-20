package problemdomenet;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
/**
 *
 * @author Amund
 */
public class Treningsokt{
    private int oktnr;
    private final String datoformat = "yyyy-MM-dd";
    private String dato = new SimpleDateFormat(datoformat).format(new GregorianCalendar().getTime());
    private int varighet;
    private String kategori;
    private String tekst;
    
    /**
     * 
     * @param oktnr
     * @param varighet
     * @param kategori
     * @param tekst
     * @param dato 
     */
    public Treningsokt(int oktnr, int varighet, String kategori, String tekst,String dato){
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;
        this.dato = dato;
        this.oktnr = oktnr;
    }
    /**
     * 
     * @param varighet
     * @param kategori
     * @param tekst
     * @param dato 
     */
    public Treningsokt(int varighet, String kategori, String tekst,String dato){
        this.varighet = varighet;
        this.kategori = kategori;
        this.tekst = tekst;
        this.dato = dato;
        oktnr++;
    }
    /**
     * Konstruktør som lager en treningsøkt med tomme verdier og dagens dato
     */
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
    
    /**
     * Setter objektvariablene til "tomme" verdier og datot til dagens dato.
     */
    public synchronized void nullstill(){
        varighet = 0;
        dato = new SimpleDateFormat(datoformat).format(new GregorianCalendar().getTime());
        tekst = "";
        kategori = "";
    }
}
