
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    private oversikt oversikt = new oversikt();
    private Treningsokt treningsokt = new Treningsokt();

    
    public oversikt getOversikt() {
        return oversikt;
    }

    public Treningsokt getTreningsokt() {
        return treningsokt;
    }
    
 public void setNorsk(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("no"));
    }
    public void setEngelsk(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("en"));
    }
}