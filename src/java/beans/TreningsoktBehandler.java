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

    public boolean getDatafins() {
        return (tabelldata.size() > 0);
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

    /* EGENSKAP: tempTrans*/ // for midlertidig lagring av transaksjonsdata
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

    @PostConstruct
    public void setDatabaseTabell() {
        System.out.println("setDatabaseTabell()");
        List<TreningsoktStatus> temp = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
        for (Treningsokt t : oversikt.getAlleOkter()) {
            temp.add(new TreningsoktStatus(t));
        }
        tabelldata = temp;
    }

    public void registrer() {
        if (!tempOkt.getTekst().trim().equals("")) {
            Treningsokt nyOkt = new Treningsokt(tempOkt.getOktnr(), tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst(), tempOkt.getDato());
            oversikt.registrerNyOkt(nyOkt);
            tabelldata.add(new TreningsoktStatus(nyOkt));
            tempOkt.nullstill();
        }
    }

    public void oppdater() {
        int indeks = tabelldata.size() - 1;
        System.out.println("se her: "+indeks);
        while (indeks >= 0) {
            TreningsoktStatus ts = tabelldata.get(indeks);
            System.out.println(ts.getTreningsokt().getTekst()+" ; se her2 : "+ts.getSkalslettes() +"indeks:"+indeks);
            if (ts.getSkalslettes()) { // sletter data, først i ...
                oversikt.slettOkt(ts.getTreningsokt());// ... problemdomeneobj.
                tabelldata.remove(indeks); // deretter i presentasjonsobjektet
            } //else {
               // System.out.println("endre verdier:"+ts.getTreningsokt().getTekst());
               // oversikt.endreVerdier(ts.getTreningsokt());
            //}
            indeks--;
        }
    }
}
