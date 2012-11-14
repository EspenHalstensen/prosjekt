package beans;

/**
 * InnloggingsBean.java
 * -
 * Eksempel
 * dbBrukere
 * -
 * leksjon
 * 11.
 *
 */
import java.sql.*;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import hjelpeklasser.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Named("login")
@RequestScoped
class InnloggingsBean {

    @Resource(name = "jdbc/wapljressurs")
    DataSource ds;
    private Bruker enBruker = new Bruker();
    private Connection forbindelse;
    private PreparedStatement setning;
    private ResultSet res;
    //private DataSource ds;
    private InitialContext octx;
    private String name;
     private static Logger logger = Logger.getLogger("com.corejsf");
    private String gammeltPassord = "";
    private String nyttPassord = "";

    public InnloggingsBean() throws NamingException {
        octx = new InitialContext();
        //octx.bind("jdbc/wapljressurs", ds);
        ds = (DataSource) octx.lookup("java:comp/env/jdbc/wapljressurs");
    }

    public String getName() {
        if (name == null) {
            getUserData();
        }
        return name == null ? "" : name;
    }
    
    private void getUserData() {
      ExternalContext context 
         = FacesContext.getCurrentInstance().getExternalContext();
      Object requestObject =  context.getRequest();
      if (!(requestObject instanceof HttpServletRequest)) {
         logger.severe("request object has type " + requestObject.getClass());
         return;
      }
      HttpServletRequest request = (HttpServletRequest) requestObject;
      name = request.getRemoteUser();
   }

    public String getNyttPassord() {
        return nyttPassord;
    }

    public void setNyttPassord(String nyttPassord) {
        this.nyttPassord = nyttPassord;
    }

    public String getGammeltPassord() {
        return gammeltPassord;
    }

    public void setGammeltPassord(String gammeltPassord) {
        this.gammeltPassord = gammeltPassord;
    }

    public Bruker getEnBruker() {
        return enBruker;
    }

    public void setEnBruker(Bruker nyBruker) {
        enBruker = nyBruker;
    }

    public String sjekkRolle(String brukernavn) {
        String rolle = "";
        try {
            setning = forbindelse.prepareStatement("select rolle from rolle where brukernavn=?");
            setning.setString(1, brukernavn);
            res = setning.executeQuery();
            res.next();
            rolle = res.getString(1);

        } catch (SQLException e) {
            System.out.println("feil i sjekkRolle():\n" + e);
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        return rolle;
    }

    public Tilbakemelding byttPassord() {
        Tilbakemelding returverdi = Tilbakemelding.passordFeil;
        try {
            aapneForbindelse();
            //Finner passordet til brukeren
            String passordet = "";
            setning = forbindelse.prepareStatement("select passord from bruker where brukernavn=?");
            setning.setString(1, enBruker.getBrukernavn());
            res = setning.executeQuery();
            res.next();
            passordet = res.getString(1);
            Opprydder.lukkSetning(setning);

            //HUSK Å SJEKK MOT KRITERIER OGSÅ
            if (sjekkPassord(passordet)) {
                setning = forbindelse.prepareStatement("update bruker set passord = ? where brukernavn =?");
                setning.setString(1, nyttPassord);
                setning.setString(2, enBruker.getBrukernavn());
                setning.executeUpdate();
                returverdi = Tilbakemelding.passordOk;
            }

        } catch (SQLException e) {
            System.out.println("Feil ved byttPassord(): " + e);
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        System.out.println("SE HER:" + returverdi);
        return returverdi;
    }

    public boolean sjekkPassord(String passord) {
        char spesialTegn = '_';
        char[] passordChar = new char[nyttPassord.length()];
        if (passordChar != null) {
            for (int i = 0; i < passordChar.length; i++) {
                passordChar[i] = nyttPassord.charAt(i);
                if (gammeltPassord.equals(passord)
                        && passordChar.length <= 6
                        && passordChar[i] == spesialTegn) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> getBrukere() {
        ArrayList<String> brukere = new ArrayList<String>();
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement("select brukernavn from bruker");
            res = setning.executeQuery();
            while (res.next()) {
                if (!(res.getString(1).equals(""))) {
                    brukere.add(res.getString(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Feil på getBrukere()\n" + e);
        } finally {
            Opprydder.lukkForbindelse(forbindelse);
            Opprydder.lukkSetning(setning);
        }
        return brukere;
    }

    private void aapneForbindelse() {
        try {
            if (ds == null) {
                throw new SQLException("Ingen data source");
            }
            forbindelse = ds.getConnection();
            System.out.println("Tilkopling via datasource vellykket");
        } catch (Exception e) {
            System.out.println("Feil ved databasetilkopling: " + e);
        }
    }
}
