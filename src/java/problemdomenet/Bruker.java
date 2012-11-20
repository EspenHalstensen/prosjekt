package problemdomenet;

import hjelpeklasser.Opprydder;
import hjelpeklasser.Tilbakemelding;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/*
 * @author havardDB
 */
public class Bruker {

    private String brukernavn;
    private String nyttPassord = "";
    private String nyttPassordBekreft = "";
    private String gammeltPassord = "";
    private static Logger logger = Logger.getLogger("com.corejsf");

    public String getBrukernavn() {
        if (brukernavn == null) {
            getBrukerData();
        }
        return brukernavn == null ? "" : brukernavn;
    }
    
    /**
     * Henter ut brukerdata fra den personen som er innlogget
     */
    private void getBrukerData() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObject = context.getRequest();
        if (!(requestObject instanceof HttpServletRequest)) {
            logger.severe("request object has type " + requestObject.getClass());
            return;
        }
        HttpServletRequest request = (HttpServletRequest) requestObject;
        brukernavn = request.getRemoteUser();
    }

    public void setBrukernavn(String nyttBrukernavn) {
        brukernavn = nyttBrukernavn;
    }

    public String getGammeltPassord() {
        return gammeltPassord;
    }

    public String getNyttPassord() {
        return nyttPassord;
    }

    public String getNyttPassordBekreft() {
        return nyttPassordBekreft;
    }

    public void setGammeltPassord(String gammeltPassord) {
        this.gammeltPassord = gammeltPassord;
    }

    public void setNyttPassord(String nyttPassord) {
        this.nyttPassord = nyttPassord;
    }

    public void setNyttPassordBekreft(String nyttPassordBekreft) {
        this.nyttPassordBekreft = nyttPassordBekreft;
    }
/**
 * Denne metoden bytter passord, her er det flere kriterier som må følges
 * Kriteriene er skrevet som en streng som deretter brukes i en String.matches metode
 * (?=.*[0-9]) = minst et tall fra 0-9
 * (?=.*[`~!@#$%^&*()_+./{}|:\"<>?]) = minst et av disse tegnene
 * [a-zA-Z0-9] = ellers lovelige tegn
 * .{6,10} = lovelig lengde fra 6 til 10
 * Videre er det if-setninger som sjekker om passordene er de samme eller om det nye er likt det gamle
 * @param setning
 * @param res
 * @param forbindelse
 * 
 * @return 
 */
    public Tilbakemelding byttPassord(PreparedStatement setning, ResultSet res, Connection forbindelse) {
        Tilbakemelding returverdi = Tilbakemelding.feil;
        try {
            getBrukerData();
            //Finner passordet til brukeren
            String databasePassordet;
            //forbindelse = ds.getConnection();
            setning = forbindelse.prepareStatement("select passord from bruker where brukernavn=?");
            setning.setString(1, brukernavn);
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
                    setning.setString(2, brukernavn);
                    setning.executeUpdate();
                    returverdi = Tilbakemelding.passordOk;
                } else {
                    returverdi = Tilbakemelding.passordFeilNytt;
                }
            } else {
                returverdi = Tilbakemelding.passordFeilGammelt;
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "byttPassord()");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
        return returverdi;
    }
}