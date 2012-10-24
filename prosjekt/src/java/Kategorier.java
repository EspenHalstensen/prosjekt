/**
 *
 * @author havardb
 */
public class Kategorier {
    private String kategoriNavn;
    private String kategoriVerdi;
    public Kategorier[] kategorier;
    
    public Kategorier(String kategoriNavn, String kategoriVerdi){
        this.kategoriNavn = kategoriNavn;
        this.kategoriVerdi = kategoriVerdi;     
    }
    /**
     * @return the kategoriNavn
     */
    public String getKategoriNavn() {
        return kategoriNavn;
    }

    /**
     * @return the kategoriVerdi
     */
    public String getKategoriVerdi() {
        return kategoriVerdi;
    }
    
    public Kategorier[] getKategorierVerdi(){
        kategorier = new Kategorier[3];
        kategorier[1] = new Kategorier("Styrke", "Styrke");
        kategorier[2] = new Kategorier("Kondisjon", "Kondisjon");
        kategorier[3] = new Kategorier("Annet", "Annet");
        return kategorier;
    }

}
