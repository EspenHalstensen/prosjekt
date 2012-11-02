
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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
    private Treningsokt tempOkt = new Treningsokt(0, "", "");

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

    public synchronized void setTempOkt(Treningsokt nyTempOkt) {
        tempOkt = nyTempOkt;
    }

    public synchronized void oppdater() {
        if (!tempOkt.getTekst().trim().equals("")) {
            Treningsokt nyOkt = new Treningsokt(tempOkt.getVarighet(), tempOkt.getKategori(), tempOkt.getTekst());
            oversikt.registrerNyOkt(nyOkt);
            tabelldata.add(new TreningsoktStatus(nyOkt));
            tempOkt.nullstill();
        }
        int indeks = tabelldata.size() - 1;
        while (indeks >= 0) {
            TreningsoktStatus ts = tabelldata.get(indeks);
            if (ts.getSkalslettes()) { // sletter data, først i ...
                oversikt.slettOkt(ts.getTreningsokt());// ... problemdomeneobj.
                tabelldata.remove(indeks); // deretter i presentasjonsobjektet
            }
            indeks--;


        }
    }
}
