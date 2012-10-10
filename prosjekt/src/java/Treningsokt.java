
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Date;
/**
 *
 * @author Amund
 */
@SessionScoped
@Named("Treningsokt")

public class Treningsokt implements Serializable{
    private int oktnr;
    private String dato;
    private int varighet;
    private String kategori;
    private String tekst;
    private String sprak;
    
    public Treningsokt(){
        setDagens();
    }
            
    private void setDagens(){
        Date d = new Date();
        dato = d.getDate()+"/"+d.getMonth()+"-"+d.getYear();
    }
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
    
    public void setSprak(String s){ sprak = s; }
    public String getSprak(){ return sprak; }
}
