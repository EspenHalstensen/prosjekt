
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author
 * havardb
 */
public class oversikt {

    private String brukernavn = "anne"; //For øving10
    ArrayList<Treningsokt> treningsokter = new ArrayList<Treningsokt>();
    private String passord = "";
    private ResultSet res = null;
    private PreparedStatement setning = null;
    private Connection forbindelse = null;
    private String databasedriver = "org.apache.derby.jdbc.ClientDriver";
    private String databasenavn = "jdbc:derby://localhost:1527/waplj_prosjekt;user=asd;password=waplj";


    public oversikt() {
    }

    public oversikt(String brukernavn, String passord) {
        this.brukernavn = brukernavn;
        this.passord = passord;
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
            setning = forbindelse.prepareStatement("select * from trening where brukernavn = '" + brukernavn + "'");
            res = setning.executeQuery();
            while (res.next()) {
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String tekst = res.getString("tekst");
                String dato = res.getString("dato");
                //Treningsokt(int varighet, String kategori, String tekst)
                treningsokter.add(new Treningsokt(varighet, kategori, tekst, dato));
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

    public void stengForbindelse() {
        Opprydder.lukkForbindelse(forbindelse);
        System.out.println("lukker databaseforbindelse");
    }

    public void aapneForbindelse() {
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
        } catch (Exception e) {
            System.out.println("Trøbbel i aapneForbindelse()\n" + e);
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    public ArrayList<Treningsokt> getAlleOkter() {
        return treningsokter;
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

    public int getSum() {
        /*double sum = 0.0;
         for (Treningsokt t : treningsokter) {
         sum += t.getVarighet();
         }
         sum = sum / treningsokter.size();
         return sum;*/
        aapneForbindelse();
        double sum = 0;
        int okter = -1;
        try {
            setning = forbindelse.prepareStatement("select sum(VARIGHET),count(OKTNR) from TRENING where BRUKERNAVN = '"+brukernavn+"'");
            res = setning.executeQuery(); 
            while(res.next()){
            sum = res.getDouble(1);
            okter = res.getInt(2);
            }
        } catch (SQLException e) {
            System.out.println("Feil i getSum()\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
        }
        stengForbindelse();
        return (int)sum / okter;
    }

    public void slettOkt(Treningsokt t) {
        treningsokter.remove(t);

    }

    public static void main(String[] args) {
        oversikt over = new oversikt("anne", "xyz_1b");
        System.out.println(over.getSum());
        /* for(Treningsokt t:over.getAlleOkter()){
         System.out.println(t.getDato());
         }    */
    }
}
