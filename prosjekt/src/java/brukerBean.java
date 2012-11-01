
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

  private oversikt oversikt;
  
}
