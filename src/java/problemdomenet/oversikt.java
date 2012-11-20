package problemdomenet;

import hjelpeklasser.Opprydder;
import java.io.Serializable;
import java.util.ArrayList;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author
 * havardb
 */
public class oversikt implements Serializable{

    @Resource(name = "jdbc/waplj_prosjekt")
    DataSource ds;
    private InitialContext octx;
    private String brukernavn = "";
    ArrayList<Treningsokt> treningsokter = new ArrayList<Treningsokt>();
    private ResultSet res = null;
    private PreparedStatement setning = null;
    private Connection forbindelse = null;
    private String databasedriver = "org.apache.derby.jdbc.ClientDriver";
    private String databasenavn = "jdbc:derby://localhost:1527/waplj_prosjekt;user=asd;password=waplj";
    public String sqlTotalVarighet = "select sum(varighet) from trening";
//Bruker med flest antall treningsminutter innenfor en spesifik treningsform
    public String sqlViewTreningstidPaaKategoriOgPerson = "create view flestMinTreningPaaBrukernavnOgKategori(varighet,navn) as select sum(varighet), brukernavn from trening where kategorinavn  = '" + brukernavn + "' group by brukernavn";
    public String sqlTreningstidPaaKategoriOgPerson = "select * from flestMinTreningPaaBrukernavnOgKategori where varighet = (select max(varighet) from flestMinTreningPaaBrukernavnOgKategori)";
// bruker med flest treningsminutter overall
    public String sqlViewTreningstidOverall = "create view flestMinTrening(varighet,navn) as select sum(varighet), brukernavn from trening group by brukernavn";
    public String sqlTreningstidOverall = "select * from flestMinTrening where varighet = (select max(varighet) from flestMinTrening)";
//De siste 5 registrerte treningsøktene
    public String sqlSisteFemOkter = "select * from trening where oktnr > (select max(oktnr) from trening)-5";
//Treningsøkter sortert etter total varighet
    public String sqlViewFlestMinTreningPaaKategori = "create view flestMinTreningPaaKategori(varighet,kategorinavn) as select sum(varighet), kategorinavn from trening group by kategorinavn";
    public String sqlFlestMinTreningPaaKategori = "select * from flestMinTreningPaaKategori order by varighet desc";
//Konstruktør spørring    
    public String sqlKonstruktor = "select * from trening where brukernavn = ?";
//Legg til kategori
    public String sqlLeggTilkategori = "insert into KATEGORI(KATEGORINAVN) values(?)";
//Kategori spørring
    public String sqlKategori = "select kategorinavn from kategori";
//Oppdater Oppdater verdier
    public String sqlOppdaterVerdier = "update Trening set dato = ?,varighet=?,kategorinavn=?,tekst=? where brukernavn=? and oktnr=?";
//Registrere ny treningsøt
    public String dato ="";
    public String sqlRegistereNyTreningsokt = "INSERT INTO trening(dato, varighet, kategorinavn, tekst, brukernavn) VALUES(DATE('" + dato + "'),?,?,?,?)";
//Finn sum spørring
    public String sqlGetSum = "select sum(VARIGHET),count(OKTNR) from TRENING where BRUKERNAVN = ?";
//Slett økt
    public String sqlSlettOkt = "delete from Trening where oktnr=?";
//Lage view
    public String treningsform = "";
    public String sqlView = "create view flestMinTreningPaaBrukernavnOgKategori(varighet,navn) as select sum(varighet), brukernavn from trening where kategorinavn  = '" + treningsform + "' group by brukernavn;";

    public oversikt(String brukernavn) {
        this.brukernavn = brukernavn;
        setDatasource();
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
            setning = forbindelse.prepareStatement(sqlKonstruktor);
            setning.setString(1, brukernavn);
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
            Opprydder.skrivMelding(e, "oversikt(String brukernavn)");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    public oversikt() {
        setDatasource();
        try {
            Class.forName(databasedriver);
            forbindelse = DriverManager.getConnection(databasenavn);
            setning = forbindelse.prepareStatement(sqlKonstruktor);
            setning.setString(1, brukernavn);
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
            Opprydder.skrivMelding(e, "oversikt()");
        } finally {
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
            Opprydder.lukkForbindelse(forbindelse);
        }
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void stengForbindelse() {
        Opprydder.lukkForbindelse(forbindelse);
        System.out.println("lukker databaseforbindelse\n");
    }

    private void aapneForbindelse() {
        try {
            if (ds == null) {
                throw new SQLException("Ingen data source");
            }
            forbindelse = ds.getConnection();
            System.out.println("Tilkopling via datasource vellykket");
        } catch (Exception e) {
            Opprydder.skrivMelding(e, "aapneForbindelse");
        }
    }

    private void setDatasource() {
        try {
            octx = new InitialContext();
            ds = (DataSource) octx.lookup("java:comp/env/jdbc/waplj_prosjekt");
        } catch (NamingException e) {
            Opprydder.skrivMelding(e, "setDatasource()");
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
            setning = forbindelse.prepareStatement(sqlLeggTilkategori);
            setning.setString(1, k);
            setning.executeUpdate();
        } catch (SQLException e) {
            Opprydder.skrivMelding(e, "leggtilKategorier()");
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
    }

    public ArrayList<String> kategorier() {
        ArrayList<String> kategorier = new ArrayList<String>();
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement(sqlKategori);
            res = setning.executeQuery();
            while (res.next()) {
                if (!(res.getString(1).equals(""))) {
                    kategorier.add(res.getString(1));
                }
            }

        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"kategorier()");
        } finally {
            stengForbindelse();
            Opprydder.lukkSetning(setning);
        }
        return kategorier;
    }

    public void endreVerdier(Treningsokt t) {
        try {
            aapneForbindelse();
            forbindelse.setAutoCommit(false); //unødvendig? Ikke mer enn en spørring/update
            setning = forbindelse.prepareStatement(sqlOppdaterVerdier);
            setning.setString(1, t.getDato());
            setning.setInt(2, t.getVarighet());
            setning.setString(3, t.getKategori());
            setning.setString(4, t.getTekst());
            setning.setString(5, brukernavn);
            setning.setInt(6, t.getOktnr());
            setning.executeUpdate();
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"endreVerdier(Treningsokt t");
        } finally {
            Opprydder.settAutoCommit(forbindelse);
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
    }

    public void registrerNyOkt(Treningsokt t) {
        try {
            aapneForbindelse();
            forbindelse.setAutoCommit(false);
            dato = t.getDato();
            System.out.println("se her:"+dato);
            System.out.println(sqlRegistereNyTreningsokt);
            setning = forbindelse.prepareStatement(sqlRegistereNyTreningsokt);
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
            Opprydder.skrivMelding(e,"registrerNyOkt(Treningsokt t");

        } finally {
            Opprydder.settAutoCommit(forbindelse);
            stengForbindelse();
            Opprydder.lukkResSet(res);
            Opprydder.lukkSetning(setning);
        }
    }

    public int getAntOkter() {
        return treningsokter.size();
    }

    public int getSum() {
        aapneForbindelse();
        double sum = 0;
        int okter = -1;
        try {
            setning = forbindelse.prepareStatement(sqlGetSum);
            setning.setString(1, brukernavn);
            res = setning.executeQuery();
            while (res.next()) {
                sum = res.getDouble(1);
                okter = res.getInt(2);
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"getSum()");
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
            aapneForbindelse();
            treningsokter.remove(t);
            setning = forbindelse.prepareStatement(sqlSlettOkt);
            setning.setInt(1, t.getOktnr());
            setning.executeUpdate(); //kjører setningen og returnerer 0 (false), >0 (true)
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"slettOkt(Treningsokt t)");
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }

    }

    /*public int antTotalTreningsvarighet() {
     // return -1 hvis fail eller tom database
     int retur = -1;
     try {
     aapneForbindelse();
     setning = forbindelse.prepareStatement(sqlTotalVarighet);
     res = setning.executeQuery();
     res.next();
     retur = res.getInt(1);
     } catch (SQLException e) {
     System.out.println("antTotalTreningsvarighet() \n " + e);
     } finally {
     Opprydder.lukkSetning(setning);
     stengForbindelse();
     }
     return retur;
     }*/
    //Bruker oktnr (primærnøkkel)
    /*public void slettOkt(Treningsokt t) {
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

     }*/
    // Bruker med flest antall treningsminutter innenfor en spesifik treningsform
    //public String sqlViewTreningstidPaaKategoriOgPerson = "create view flestMinTreningPaaBrukernavnOgKategori(varighet,navn) as select sum(varighet), brukernavn from trening where kategorinavn  = '" + brukernavn + "' group by brukernavn;";
    //public String sqlTreningstidPaaKategoriOgPerson = "select * from flestMinTreningPaaBrukernavnOgKategori where varighet = (select max(varighet) from flestMinTreningPaaBrukernavnOgKategori);";
    public ArrayList brukerMedFlestAntallTreningsminutter(String treningsform) {
        this.treningsform = treningsform;
        ArrayList<String> a = new ArrayList<String>();
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement(sqlView);
            setning.executeQuery();
            Opprydder.lukkSetning(setning);
            setning = forbindelse.prepareStatement(sqlTreningstidPaaKategoriOgPerson);
            res = setning.executeQuery();
            res.next();
            a.add("" + res.getInt(1)); // varighet
            a.add(res.getString(2)); // kategori
        } catch (SQLException e) {
            System.out.println("brukerMedFlestAntallTreningsminutter() \n " + e);
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
        return a;
    }

// bruker med flest treningsminutter overall
//public String sqlViewTreningstidOverall = "create view flestMinTrening(varighet,navn) as select sum(varighet), brukernavn from trening group by brukernavn;";
//public String sqlTreningstidOverall = "select * from flestMinTrening where varighet = (select max(varighet) from flestMinTrening);";
    public ArrayList brukerMedFlestAntallTreningsminutterOverall() {
        //String view = "create view flestMinTreningPaaBrukernavnOgKategori(varighet,navn) as select sum(varighet), brukernavn from trening where kategorinavn  = '" + treningsform + "' group by brukernavn;";
        ArrayList<String> a = new ArrayList<String>();
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement(sqlViewTreningstidOverall);
            setning.executeQuery();
            Opprydder.lukkSetning(setning);
            setning = forbindelse.prepareStatement(sqlTreningstidOverall);
            res = setning.executeQuery();
            res.next();
            a.add("" + res.getInt(1)); // varighet
            a.add(res.getString(2)); // kategori
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"brukerMedFlestAntallTreningsminutterOverall()");
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
        return a;
    }
// De siste 5 registrerte treningsøktene
//public String sqlSisteFemOkter = "select * from trening where oktnr > (select max(oktnr) from trening)-5;";

    public ArrayList<Treningsokt> sisteFemRegistrerteTreningsokter() {
        //String view = "create view flestMinTreningPaaBrukernavnOgKategori(varighet,navn) as select sum(varighet), brukernavn from trening where kategorinavn  = '" + treningsform + "' group by brukernavn;";
        ArrayList<Treningsokt> a = new ArrayList<Treningsokt>();
        try {
            aapneForbindelse();
            setning = forbindelse.prepareStatement(sqlSisteFemOkter);
            res = setning.executeQuery();
            while (res.next()) {
                //Treningsokt(int oktnr, int varighet, String kategori, String tekst,String dato)
                //øktnr, dato, varighet, kategorinavn, tekst, brukernavn
                int oktnr = res.getInt(1);
                String dato = res.getDate(2).toString();
                int varighet = res.getInt(3);
                String kategori = res.getString(4);
                String tekst = res.getString(5);
                
                Treningsokt temp = new Treningsokt(oktnr, varighet, kategori, tekst, dato);
                a.add(temp);
            }
        } catch (SQLException e) {
            Opprydder.skrivMelding(e,"sisteFemRegistrerteTreningsokter()");
        } finally {
            Opprydder.lukkSetning(setning);
            stengForbindelse();
        }
        return a;
    }
// Treningsøkter sortert etter total varighet
//public String sqlViewFlestMinTreningPaaKategori = "create view flestMinTreningPaaKategori(varighet,kategorinavn) as select sum(varighet), kategorinavn from trening group by kategorinavn;";
//public String sqlFlestMinTreningPaaKategori = "select * from flestMinTreningPaaKategori order by varighet desc;";
}
