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
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.*;

@Named("login")
@RequestScoped
class InnloggingsBean {

    @Resource(name = "jdbc/wapljressurs")
    DataSource ds;
    /*private Bruker enBruker = new Bruker();*/
    private Connection forbindelse;
    private PreparedStatement setning;
    private ResultSet res;
    //private DataSource ds;
    private InitialContext octx;
    private String navn;
    private static Logger logger = Logger.getLogger("com.corejsf");
    private String gammeltPassord = "";
    private String nyttPassord = "";
    private String nyttPassordBekreft = "";

    public InnloggingsBean() throws NamingException {
        octx = new InitialContext();
        ds = (DataSource) octx.lookup("java:comp/env/jdbc/wapljressurs");
    }

    public String getNavn() {
        if (navn == null) {
            getUserData();
        }
        return navn == null ? "" : navn;
    }

    private void getUserData() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObject = context.getRequest();
        if (!(requestObject instanceof HttpServletRequest)) {
            logger.severe("request object has type " + requestObject.getClass());
            return;
        }
        HttpServletRequest request = (HttpServletRequest) requestObject;
        navn = request.getRemoteUser();
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
       public String getNyttPassordBekreft() {
        return nyttPassordBekreft;
    }

    public void setNyttPassordBekreft(String nyttPassordBekreft) {
        this.nyttPassordBekreft = nyttPassordBekreft;
    }

    public void setGammeltPassord(String gammeltPassord) {
        this.gammeltPassord = gammeltPassord;
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
            Opprydder.skrivMelding(e, "sjekkRolle()");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        return rolle;
    }

    public Tilbakemelding byttPassord() {
        Tilbakemelding returverdi = Tilbakemelding.feil;
        try {
            getUserData();
            aapneForbindelse();
            //Finner passordet til brukeren
            String databasePassordet;
            setning = forbindelse.prepareStatement("select passord from bruker where brukernavn=?");
            setning.setString(1, navn);
            res = setning.executeQuery();
            res.next();
            databasePassordet = res.getString(1);
            Opprydder.lukkSetning(setning);
            
             /* ekstra i kriterier om ønskelig: [\\]\\\\;\',      */
            if (gammeltPassord.equals("") && nyttPassord.equals("")) {
                returverdi = Tilbakemelding.passordFeilIngenInput;
            } else if (gammeltPassord.equals(databasePassordet)) {
                String reg = "^(?=.*[0-9])(?=.*[`~!@#$%^&*()_+./{}|:\"<>?])[a-zA-Z0-9].{6,10}$";
                if (nyttPassord.matches(reg) && nyttPassord.equals(nyttPassordBekreft) && !nyttPassord.equals(databasePassordet)) {
                    setning = forbindelse.prepareStatement("update bruker set passord = ? where brukernavn =?");
                    setning.setString(1, nyttPassord);
                    setning.setString(2, navn);
                    setning.executeUpdate();
                    returverdi = Tilbakemelding.passordOk;
                } else {
                    returverdi = Tilbakemelding.passordFeilNytt;
                }
            } else {
                returverdi = Tilbakemelding.passordFeilGammelt;
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"byttPassord()");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        return returverdi;
    }
    
    private void aapneForbindelse() {
        try {
            if (ds == null) {
                throw new SQLException("Ingen data source");
            }
            forbindelse = ds.getConnection();
            System.out.println("Tilkopling via datasource vellykket");
        } catch (Exception e) {
            Opprydder.skrivMelding(e, "aapneForbindelse()");
        }
    }
}
