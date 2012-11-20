package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import problemdomenet.*;

/**
 *Denne klassen brukes for å få opp litt statistikk i første vindu
 * @author
 * havardb
 */
@Named("statistikk")
@RequestScoped
public class TreningsStatistikk {

    private oversikt oversikt = new oversikt();
    private List<TreningsoktStatus> tabellIndex = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());

    
    public List<TreningsoktStatus> getTabellIndex() {
        return tabellIndex;
    }
    /**
     * Denne metoden skal lager en oversikt som vi bruker første gangen startsiden starter
     * Denne petoden har @PostConstruct for å kjøre med en gang siden loades og beanen brukes
     */
    @PostConstruct
    public synchronized void setDatabaseIndeksTabell() {
        System.out.println("setDatabaseIndeksTabell()");
        List<TreningsoktStatus> temp = Collections.synchronizedList(new ArrayList<TreningsoktStatus>());
        for (Treningsokt t : oversikt.sisteFemRegistrerteTreningsokter()) {
            temp.add(new TreningsoktStatus(t));
        }
        tabellIndex = temp;
    }
}
