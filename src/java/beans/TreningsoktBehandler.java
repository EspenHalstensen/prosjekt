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

/**
 *
 * @author
 * havardb
 */
@Named
@SessionScoped
public class TreningsoktBehandler implements java.io.Serializable {

    private oversikt oversikt = new oversikt();
    private List<TreningsoktStatus> tabelldata = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
    private Treningsokt tempOkt = new Treningsokt();
    private String nyKategori = "";
    private Bruker bruker = new Bruker(); 

    public TreningsoktBehandler(Bruker enBruker) {
        bruker = enBruker;
    }
    
    
    public synchronized boolean getDatafins() {
        return (tabelldata.size() > 0);
    }
    /* EGENSKAP: tabelldata */

    public synchronized String getNyKategori() {
        return nyKategori;
    }

    public synchronized void setNyKategori(String nyKategori) {
        this.nyKategori = nyKategori;
    }
    
    public synchronized void fiksKategorier(){
        oversikt.leggtilKategorier(nyKategori);
    }
    
    
    public synchronized List<TreningsoktStatus> getTabelldata() {
        return tabelldata;
    }

    /* EGENSKAP: tempTrans*/ // for midlertidig lagring av transaksjonsdata
    public synchronized Treningsokt getTempOkt() {
        return tempOkt;
    }

    public synchronized String getBrukernavn() {
        System.out.println("se her"+oversikt.getBrukernavn());
        return oversikt.getBrukernavn();
    }

    public synchronized String getPassord() {
        return oversikt.getPassord();
    }
    
    public synchronized void setBrukernavn(){
        oversikt.setBrukernavn(bruker.getBrukernavn());
    }
    
    public synchronized void setPassord(){
        oversikt.setPassord(bruker.getPassord());
    }

    public synchronized void setTempOkt(Treningsokt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized double getSum() {
        return oversikt.getSum();
    }

    public synchronized int getLopenummer() {
        return oversikt.getAntOkter()+1;
    }
    public synchronized ArrayList<String> getKategorier(){
        return oversikt.kategorier();
    }

    @PostConstruct
    public synchronized void setDatabaseTabell() {
        List<TreningsoktStatus> temp = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
        for (Treningsokt t : oversikt.getAlleOkter()) {
            temp.add(new TreningsoktStatus(t));
        }
        tabelldata = temp;
    }

    public synchronized void oppdater() {
        if (!tempOkt.getTekst().trim().equals("")) {
            System.out.println("inn i Oppdater() løkka");
            Treningsokt nyOkt = new Treningsokt(tempOkt.getOktnr(), tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst(), tempOkt.getDato());
            oversikt.registrerNyOkt(nyOkt);
            tabelldata.add(new TreningsoktStatus(nyOkt));
            tempOkt.nullstill();
            
        }
        System.out.println("Utafor Oppdater() løkka");
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            TreningsoktStatus ts = tabelldata.get(indeks);
            if (ts.getSkalslettes()) { // sletter data, først i ...
                oversikt.slettOkt(ts.getTreningsokt());// ... problemdomeneobj.
                tabelldata.remove(indeks); // deretter i presentasjonsobjektet
            }else{
                oversikt.endreVerdier(ts.getTreningsokt());
            }
            indeks--;
        }
    }

    public void setNorsk() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("no"));
    }

    public void setEngelsk() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("en"));
    }
}
