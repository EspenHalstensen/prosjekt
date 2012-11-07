
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author havardb
 */
@Named
@SessionScoped
public class TreningsoktBehandler implements java.io.Serializable {

    private oversikt oversikt = new oversikt();
    private List<TreningsoktStatus> tabelldata = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
    private Treningsokt tempOkt = new Treningsokt();

    public synchronized boolean getDatafins() {
        return (tabelldata.size() > 0);
    }
    /* EGENSKAP: tabelldata */

    public synchronized List<TreningsoktStatus> getTabelldata() {
        return tabelldata;
    }

    /* EGENSKAP: tempTrans*/ // for midlertidig lagring av transaksjonsdata
    public synchronized Treningsokt getTempOkt() {
        return tempOkt;
    }
    
    public synchronized String getBrukernavn(){
        return oversikt.getBrukernavn();
    }
    
    public synchronized String getPassord(){
        return oversikt.getPassord();
    }

    public synchronized void setTempOkt(Treningsokt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized double getSum() {
        return oversikt.getSum();
    }
    public synchronized int getAntOkter(){
        return oversikt.getAntOkter();
    }

    public synchronized void oppdater() {
        if (!tempOkt.getTekst().trim().equals("")) {
            Treningsokt nyOkt = new Treningsokt(tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst());
            oversikt.registrerNyOkt(nyOkt);
            nyOkt.setOktnr(Treningsokt.setLopeNr());
            tabelldata.add(new TreningsoktStatus(nyOkt));
            tempOkt.nullstill();
        }
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            TreningsoktStatus ts = tabelldata.get(indeks);
            System.out.println(ts.getSkalslettes());
            if (ts.getSkalslettes()) { // sletter data, først i ...
                oversikt.slettOkt(ts.getTreningsokt());// ... problemdomeneobj.
                tabelldata.remove(indeks); // deretter i presentasjonsobjektet
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
