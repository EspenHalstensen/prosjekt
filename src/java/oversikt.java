
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author havardb
 */
public class oversikt {

    private String brukernavn = "";
    ArrayList<Treningsokt> treningsokter = new ArrayList<Treningsokt>();
    private String passord = "";
    private ResultSet res = null;
    private Statement setning = null;
    private Connection forbindelse = null;
    private String databasedriver = "org.apache.derby.jdbc.ClientDriver";
    private String databasenavn = "jdbc:derby://localhost:1527/waplj.prosjekt;user=asd;password=waplj";
    
    public oversikt() {
    }

    public oversikt(String brukernavn, String passord) {
        this.brukernavn = brukernavn;
        this.passord = passord;

        try {
            
            forbindelse = DriverManager.getConnection(databasenavn);
            setning = forbindelse.createStatement();
            res = setning.executeQuery("select * from trening where brukernavn = '" + brukernavn + "'");
            while (res.next()) {
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String tekst = res.getString("tekst");
                //Treningsokt(int varighet, String kategori, String tekst)
                treningsokter.add(new Treningsokt(varighet, kategori, tekst));
            }
        } catch (Exception e) {
            System.out.println("error under pålogging, konstruktør");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public String getPassord() {
        return passord;
    }
    public void stengForbindelse(){
        Opprydder.lukkForbindelse(forbindelse);
        System.out.println("lukker databaseforbindelse");
    }
    
    public void aapneForbindelse(){
        try{
        forbindelse = DriverManager.getConnection(databasenavn);
        }catch(Exception e){
            System.out.println("Trøbbel i aapneForbindelse()");
            Opprydder.lukkForbindelse(forbindelse);
        }
    }
    
    
    
    
    

    public Treningsokt getAlleOkter() {
        if (treningsokter != null) {
            for (Treningsokt t : treningsokter) {
                return t;
            }
        }
        return null;
    }

    public Treningsokt getAlleOkterEnMnd(String dato) { //"dd/MM/yyyy"
        if (treningsokter != null) {
            for (Treningsokt t : treningsokter) {
                if (dato.equals(t.getDato())) {
                    return t;
                }
            }
        }
        return null;
    }

    public void registrerNyOkt(Treningsokt t) {
        if (treningsokter != null) {
            treningsokter.add(t);
        }
    }

    public int getAntOkter() {
        return treningsokter.size();
    }

    public double getSum() {
        double sum = 0.0;
        for (Treningsokt t : treningsokter) {
            sum += t.getVarighet();
        }
        sum = sum / treningsokter.size();
        return sum;
    }

    public void slettOkt(Treningsokt t) {
        treningsokter.remove(t);

    }
}
