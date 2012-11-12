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
import problemdomenet.oversikt;

@Named
@RequestScoped
class InnloggingsBean {

    @Resource(name = "jdbc/wapljressurs")
    DataSource ds;
    private Bruker enBruker = new Bruker();
    private Connection forbindelse;
    //private DataSource ds;
    private InitialContext octx;

    public InnloggingsBean() throws NamingException {
        octx = new InitialContext();
        //octx.bind("jdbc/wapljressurs", ds);
        ds = (DataSource) octx.lookup("java:comp/env/jdbc/wapljressurs");
    }

    public Bruker getEnBruker() {
        return enBruker;
    }

    public void setEnBruker(Bruker nyBruker) {
        enBruker = nyBruker;
    }

    public Tilbakemelding loggInn() {
        åpneForbindelse();
        Statement setning = null;
        ResultSet res = null;
        Tilbakemelding returverdi = Tilbakemelding.feil;
        try {
            TreningsoktBehandler t = new TreningsoktBehandler(enBruker);
            t.setBrukernavn();
            t.setPassord();
            String brukernavn = enBruker.getBrukernavn();
            String passord = enBruker.getPassord();
            String sql = "select * from bruker where brukernavn = '" + brukernavn + "' and passord = '" + passord + "'";

            setning = forbindelse.createStatement();
            res = setning.executeQuery(sql);
            
            if (res.next()) {
                returverdi = Tilbakemelding.innloggingOk;
            }
        } catch (SQLException e) {
            System.out.println("Feil ved loggInn(): " + e);
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        return returverdi;

    }

    private void åpneForbindelse() {
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
