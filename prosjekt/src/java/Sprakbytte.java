/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author espen
 */

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.SessionScoped;
    
import javax.inject.Named;
import javax.faces.context.FacesContext;

@Named
@SessionScoped
public class Sprakbytte implements Serializable{
    
    public String englishAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.ENGLISH);
        return null;
    }
    
    public String norwegianAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.ROOT);
        return null;
    }
    
    
}
