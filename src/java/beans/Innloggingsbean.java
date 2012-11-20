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
import problemdomenet.Bruker;
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
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Named("login")
@RequestScoped
class InnloggingsBean {

    @Resource(name = "jdbc/waplj_prosjekt")
    DataSource ds;
    private Connection forbindelse;
    private PreparedStatement setning;
    private ResultSet res;
    private InitialContext octx;
    private Bruker bruker = new Bruker();

    public InnloggingsBean() throws NamingException {
        octx = new InitialContext();
        ds = (DataSource) octx.lookup("java:comp/env/jdbc/waplj_prosjekt");
    }
    
    public String getBrukernavn(){
        return bruker.getBrukernavn();
    }

    public String getNyttPassord() {
        return bruker.getNyttPassord();
    }

    public void setNyttPassord(String nyttPassord) {
        bruker.setNyttPassord(nyttPassord);
    }

    public String getGammeltPassord() {
        return bruker.getGammeltPassord();
    }
       public String getNyttPassordBekreft() {
        return bruker.getNyttPassordBekreft();
    }

    public void setNyttPassordBekreft(String nyttPassordBekreft) {
        bruker.setNyttPassordBekreft(nyttPassordBekreft);
    }

    public void setGammeltPassord(String gammeltPassord) {
        bruker.setGammeltPassord(gammeltPassord);
    }
    
    public Tilbakemelding byttPassord(){
        aapneForbindelse();
        return bruker.byttPassord(setning, res, forbindelse);
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
