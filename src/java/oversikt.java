
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
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
            setning = forbindelse.prepareStatement("select * from trening where brukernavn = 'anne'");
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
        System.out.println("lukker databaseforbindelse\n");
    }

    public void aapneForbindelse() {
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
            System.out.println("Åpner dataforbindelse");
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
        /*if (treningsokter != null) {
            treningsokter.add(t);
        }*/
        
        try{
        aapneForbindelse();
        forbindelse.setAutoCommit(false);
        setning = forbindelse.prepareStatement("select max(oktnr) from trening");
        res = setning.executeQuery();
        res.next();
        int tempOktnr = res.getInt(1)+1;
        stengForbindelse();
        // Treningsokt(int oktnr, int varighet, String kategori, String tekst,String dato){
        setning = forbindelse.prepareStatement("insert into trening values (?,?,?,?,?)");
        if (setning.executeUpdate()> 0){
            setning.setInt(1, tempOktnr);
            setning.setInt(2, t.getVarighet());
            setning.setString(3, t.getKategori());
            setning.setString(4, t.getTekst());
            setning.setString(5, t.getDato());
        }
        Opprydder.lukkSetning(setning);
        
        }catch(Exception e){
            System.out.println("Error i registrer økt \n" + e);
            
        }finally{
            forbindelse.setAutoCommit(true);
            stengForbindelse();
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
    }

    public int getAntOkter() {
        return treningsokter.size();
    }

    public int getSum() {
        aapneForbindelse();
        double sum = 0;
        int okter = -1;
        try {
            setning = forbindelse.prepareStatement("select sum(VARIGHET),count(OKTNR) from TRENING where BRUKERNAVN = '" + brukernavn + "'");
            res = setning.executeQuery();
            while (res.next()) {
                sum = res.getDouble(1);
                okter = res.getInt(2);
                System.out.println("Total varighet/antall okter:\n"+(int)(sum/okter));
            }
        } catch (SQLException e) {
            System.out.println("Feil i getSum()\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
        return (int) sum / okter;
    }

    //Bruker oktnr (primærnøkkel)
    public void slettOkt(Treningsokt t) {
        aapneForbindelse();
        try {
            treningsokter.remove(t);
            setning = forbindelse.prepareStatement("delete from Trening where oktnr=?");
            setning.setInt(1, t.getOktnr());
            setning.executeUpdate(); //kjører setningen og returnerer 0 (false), >0 (true)
            System.out.println("slettet treningsokt: "+t.getOktnr());
                    } catch (SQLException e) {
            System.out.println("Feil i slettOkt\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }

    }

    public static void main(String[] args) {
        oversikt over = new oversikt("anne", "xyz_1b");
        
        /* for(Treningsokt t:over.getAlleOkter()){
         System.out.println(t.getDato());
         }    */
    }
}
