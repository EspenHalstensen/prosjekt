
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author
 * havardb
 */
@Named("bruker")
@SessionScoped

public class brukerBean implements Serializable {

    private int oktnr;
    private String dato;
    private int varighet;
    private String kategori;
    private String tekst;
    private Treningsokt treningsokt = new Treningsokt();
    
    public int getoktnr(){return treningsokt.getOktnr();}
    public String dato(){return treningsokt.getDato();}
    public int varighet(){return treningsokt.getVarighet();}
    public String kategori(){return treningsokt.getKategori();}
    public String tekst(){ return treningsokt.getTekst();}
    public Treningsokt getTreningsokt(){return treningsokt;
    
    }
    
    
    
}
