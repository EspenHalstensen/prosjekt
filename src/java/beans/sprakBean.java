package beans;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *Denne klassen har to metoder som setter språket til sidene våre.
 * @author havardb
 */
@Named("sprak")
@SessionScoped
public class sprakBean implements Serializable{
    /**
     * Setter språket til norsk, bruker properties.tekst_no.properties
     */
        public void setNorsk() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("no"));
    }
    /**
     *  Setter språket til engelsk, bruker properties.tekst_en.properties
     */
    public void setEngelsk() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("en"));
    }

}
