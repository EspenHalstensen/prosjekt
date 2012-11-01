
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Locale;

/**
 *
 * @author
 * havardb
 */
@Named("bruker")
@SessionScoped

public class brukerBean implements Serializable {
    public int oktnr;
    public String dato;
    public int varighet;
    public String kategori;
    public String tekst;
    private oversikt oversikt = new oversikt();
    
    public int getoktnr(){return oversikt.getAlleOkter().getOktnr();}
    public String dato(){return oversikt.getAlleOkter().getDato();}
    public int varighet(){return oversikt.getAlleOkter().getVarighet();}
    public String kategori(){return oversikt.getAlleOkter().getKategori();}
    public String tekst(){ return oversikt.getAlleOkter().getTekst();}
    public Treningsokt getOversikt(){return oversikt.getAlleOkter();}  

 public void setNorsk(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("no"));
    }
    public void setEngelsk(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("en"));
    }
}