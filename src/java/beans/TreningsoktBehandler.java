package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import problemdomenet.*;
import hjelpeklasser.*;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletRequest.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author
 * havardb
 */
@Named
@SessionScoped
public class TreningsoktBehandler implements java.io.Serializable {

    private oversikt oversikt = new oversikt(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
    private List<TreningsoktStatus> tabelldata = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
    private Treningsokt tempOkt = new Treningsokt();
    private String nyKategori = "";

    /**
     * Denne metoden sjekker om det er data i tabellen, denne brukes i registrering.xhtml
     * @return
     * boolean
     */
    public boolean getDatafins() {
        return (tabelldata.size() > 0);
    }

    /**
     * Denne metoden sjekker om det ikke er data i tabellen, denne brukes i registrering.xhtml
     * @return
     */
    public boolean getDataIkkeFins() {
        return (tabelldata.size() <= 0);
    }

    public String getNyKategori() {
        return nyKategori;
    }

    public void setNyKategori(String nyKategori) {
        this.nyKategori = nyKategori;
    }

    public void fiksKategorier() {
        oversikt.leggtilKategorier(nyKategori);
    }

    public List<TreningsoktStatus> getTabelldata() {
        return tabelldata;
    }

    public Treningsokt getTempOkt() {
        return tempOkt;
    }

    public String getBrukernavn() {
        return oversikt.getBrukernavn();
    }

    public oversikt getOversikt() {
        return oversikt;
    }

    public void setTempOkt(Treningsokt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public double getSum() {
        return oversikt.getSum();
    }

    public int getLopenummer() {
        return oversikt.getAntOkter() + 1;
    }

    public int getAntOkter() {
        return oversikt.getAntOkter();
    }

    public ArrayList<String> getKategorier() {
        return oversikt.kategorier();
    }

    /**
     * Denne metoden lager datatabellen som er lik databasen på registrert.xhtml bildet
     */
    @PostConstruct
    public void setDatabaseTabell() {
        List<TreningsoktStatus> temp = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
        for (Treningsokt t : oversikt.getAlleOkter()) {
            temp.add(new TreningsoktStatus(t));
        }
        tabelldata = temp;
    }

    /**
     * Denne metoden brukes for knapp i registrert.xhtml, for å registrere
     * Vi har laget en liten date check her, dette ble gjort fordi vi ikke hadde tid til å skrive om alle klassene våre til Date objekter
     * for date istedet for String, vet at det finnes en fin sjekk for passord validering, men det ble rett og slett ikke tid
     */
    public Tilbakemelding registrer() {
        Tilbakemelding returverdi = Tilbakemelding.registreringIkkeOk;
        if (!tempOkt.getTekst().trim().equals("")) {
            Treningsokt nyOkt = new Treningsokt(tempOkt.getOktnr(), tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst(), tempOkt.getDato());
            if ((nyOkt.getDato().length() == 10)) {
                oversikt.registrerNyOkt(nyOkt);
                tabelldata.add(new TreningsoktStatus(nyOkt));
                tempOkt.nullstill();
                returverdi = Tilbakemelding.registreringOk;
            }else{
                returverdi = Tilbakemelding.registreringIkkeOk;
            }
        }return returverdi;
    }

    /**
     * Denne metoden brukes for knapp i registrert.xhtml, for å oppdatere og slette økter som blir avkrysset
     */
    public void oppdater() {
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            TreningsoktStatus ts = tabelldata.get(indeks);
            if (ts.getSkalslettes()) { // sletter data, først i ...
                oversikt.slettOkt(ts.getTreningsokt());// ... problemdomeneobj.
                tabelldata.remove(indeks); // deretter i presentasjonsobjektet
            } else {
                oversikt.endreVerdier(ts.getTreningsokt());
            }
            indeks--;
        }
    }
}
