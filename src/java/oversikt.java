
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
            setning = forbindelse.prepareStatement("select * from trening where brukernavn = ?");
            setning.setString(1, brukernavn);

            res = setning.executeQuery();
            while (res.next()) {
                int varighet = res.getInt("varighet");
                String kategori = res.getString("kategorinavn");
                String tekst = res.getString("tekst");
                String dato = res.getString("dato");
                int oktNr = res.getInt("oktnr");
                //Treningsokt(int varighet, String kategori, String tekst)
                treningsokter.add(new Treningsokt(oktNr, varighet, kategori, tekst, dato));
            }
        } catch (Exception e) {
            System.out.println("error under pålogging, konstruktør");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    /*public oversikt(String brukernavn, String passord) {
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
     }*/
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

    public void leggtilKategorier(String k) {
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement("insert into KATEGORI(KATEGORINAVN) values(?)");
            setning.setString(1, k);
            setning.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Feil i leggtilKategorier()\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
    }

    public ArrayList<String> kategorier() {
        ArrayList<String> kategorier = new ArrayList<String>();
        try {
            System.out.println("kategorier()");
            aapneForbindelse();
            setning = forbindelse.prepareStatement("select kategorinavn from kategori");
            res = setning.executeQuery();
            while (res.next()) {
                kategorier.add(res.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Feil på kategorier()\n" + e);
        } finally {
            stengForbindelse();
            Opprydder.lukkSetning(setning);
        }
        return kategorier;
    }

    public void endreVerdier(Treningsokt t) {
        try {
            System.out.println("ENDREVERDIER()");
            aapneForbindelse();
            forbindelse.setAutoCommit(false); //unødvendig? Ikke mer enn en spørring/update
            setning = forbindelse.prepareStatement("update Trening set dato = ?,varighet=?,kategorinavn=?,tekst=? where brukernavn=? and oktnr=?");
            setning.setString(1, t.getDato());
            setning.setInt(2, t.getVarighet());
            setning.setString(3, t.getKategori());
            setning.setString(4, t.getTekst());
            setning.setString(5, brukernavn);
            setning.setInt(6, t.getOktnr());
            System.out.println("SE HER :" + t.getOktnr());
            setning.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Feil i endreVerdier" + e);
        } finally {
            Opprydder.settAutoCommit(forbindelse);
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
    }

    public void registrerNyOkt(Treningsokt t) {
        try {
            System.out.println("registrerNyOkt()");
            aapneForbindelse();
            forbindelse.setAutoCommit(false);
            setning = forbindelse.prepareStatement("INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn) VALUES(DATE('" + t.getDato() + "'),?,?,?,?)");
            setning.setInt(1, t.getVarighet());
            setning.setString(2, t.getKategori());
            setning.setString(3, t.getTekst());
            setning.setString(4, brukernavn);
            setning.executeUpdate();
            Opprydder.lukkSetning(setning);
             setning = forbindelse.prepareStatement("select max(oktnr) from trening");
            res = setning.executeQuery();
            res.next();
            t.setOktnr(res.getInt(1));
            treningsokter.add(t);

        } catch (Exception e) {
            System.out.println("Error i registrer økt \n" + e);

        } finally {
            Opprydder.settAutoCommit(forbindelse);
            stengForbindelse();
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
        }
    }

    public int getAntOkter() {
        return treningsokter.get(treningsokter.size() - 1).getOktnr();
    }

    public int getSum() {
        aapneForbindelse();
        double sum = 0;
        int okter = -1;
        try {
            System.out.println("getSum");
            setning = forbindelse.prepareStatement("select sum(VARIGHET),count(OKTNR) from TRENING where BRUKERNAVN = ?");
            setning.setString(1, brukernavn);
            res = setning.executeQuery();
            while (res.next()) {
                sum = res.getDouble(1);
                okter = res.getInt(2);
                System.out.println("Total varighet/antall okter:\n" + (int) (sum / okter));
            }
        } catch (SQLException e) {
            System.out.println("Feil i getSum()\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
        if (okter != 0) {
            return (int) sum / okter;
        } else {
            return 0;
        }
    }

    //Bruker oktnr (primærnøkkel)
    public void slettOkt(Treningsokt t) {
        try {
            System.out.println("slettOkt");
            aapneForbindelse();
            treningsokter.remove(t);
            setning = forbindelse.prepareStatement("delete from Trening where oktnr=?");
            setning.setInt(1, t.getOktnr());
            setning.executeUpdate(); //kjører setningen og returnerer 0 (false), >0 (true)
        } catch (SQLException e) {
            System.out.println("Feil i slettOkt\n" + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }

    }
}
