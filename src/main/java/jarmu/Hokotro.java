package jarmu;

import jatekos.Takarito;
import eszkoztar.Eszkoztar;
import kotrofej.KotroFej;
import kotrofej.SoproFej;
import bolt.IBoltiCikk;
import terkep.*;
import java.util.List;

/**
 * A Hokotro osztály egy járművet reprezentál. 
 * Elsődleges feladata az úthálózat tisztán tartása a rá felszerelt eszközök segítségével. 
 */
public class Hokotro extends Jarmu implements IBoltiCikk {
    
    private final String id;
    private int ar = 50;
    private KotroFej felszereltFej;
    private Eszkoztar eszkoztar;
    private static int vasaroltSzamlalo = 1; //Vásárlások számlálója a bolti azonosítóhoz
 
    private static Terkep globalTerkep; 

    //Statikus setter, hogy a játék elején rögzíthessük, hol van a világ
    public static void setGlobalTerkep(Terkep terkep) {
        globalTerkep = terkep;
    }

    /**
     * Konstruktor, amely létrehozza a Hokotro járművet egy adott azonosítóval, térképpel és felszerelt fejjel.
     * A konstruktor meghívja a szülő Jarmu konstruktorát egy random pozícióval a térképen.
     * Ha a térkép null, akkor a pozíció is null lesz, és a jármű nem lesz elhelyezve.
     */
    public Hokotro(String id, Terkep terkep, KotroFej felszereltFej) {
        //Meghívjuk a szülő Jarmu konstruktorát a térképből lekért random lokációval
        super(terkep != null ? terkep.getRandomLokacio() : null);
        this.id = id;
        this.felszereltFej = felszereltFej;
        this.eszkoztar = new Eszkoztar();
        
        //Ha a konstruktorban kapott térkép nem null, akkor megpróbáljuk elhelyezni a járművet egy random pozícióra
        if (this.pozicio != null) {
            System.out.println(">>> [RENDSZER] " + id + " elhelyezve: " 
                + pozicio.getUt().getNev() + " út, " + pozicio.getSav().getSavSzama() + ". sáv");
        }
    }

    /**
     * Ez a metódus felelős a vásárlás folyamatáért. 
     * Amikor pl egy Takarító megvásárol egy Hokotro járművet, ez a metódus létrehoz egy új Hokotro példányt 
     * egy egyedi azonosítóval, és hozzáadja azt a Takarító eszköztárához.
     */
    @Override
    public void atadVevonek(Takarito v) {
        //Ellenőrizzük, hogy a vevő és a globális térkép is érvényes-e
        if (v != null && globalTerkep != null) {
            String ujAzonosito = "H_" + vasaroltSzamlalo++;
            
            //Itt is a globalTerkep-et adjuk át, a konstruktor pedig elintézi a random pozíciót
            Hokotro megvasaroltHokotro = new Hokotro(ujAzonosito, globalTerkep, new SoproFej(30));
            //A vásárlás után a megvásárolt hókotrót hozzáadjuk a vevő eszköztárához
            v.hozzaadHokotro(megvasaroltHokotro);

            System.out.println(">>> [BOLT] Sikeres vásárlás: " + ujAzonosito + " azonosítóval.");
        } else {
            System.out.println(">>> [BOLT HIBA] Nincs térkép vagy érvénytelen vevő!");
        }
    }

    /**
     * Ez a metódus lehetővé teszi a jármű felszerelt fejének cseréjét egy új fejre.
     * Ha a fejcsere sikeres, egy üzenetet ír ki a konzolra a cseréről.
     * Ha az új fej érvénytelen (null), akkor egy hibaüzenetet ír ki.
     */
    public void fejcsere(KotroFej ujFej) {
        if (ujFej != null) {
            this.felszereltFej = ujFej;
            System.out.println(">>> [SZERELÉS] Sikeres fejcsere: Az új eszköz (" + ujFej.getNev() + ") felszerelve a(z) " + id + " hókotróra.");
        } else {
            System.out.println(">>> [SZERELÉS HIBA] A kiválasztott fej érvénytelen (null)!");
        }
    }
    
    /**
     * Meghívja a kotrófej tisztító metódusát, amely módosítja az 
     * érintett útszakasz állapotát a térképen.
     */
    public void takarit() {
        System.out.println("\n>>> [JÁRMŰ AKCIÓ] A(z) " + id + " hókotró takarítást kezdeményezett...");

        if (felszereltFej == null) {
            System.out.println("    >>> [KUDARC] A hókotrón nincs felszerelt fej, nem tud takarítani!");
            return;
        }

        //Ellenőrizzük, hogy a jármű pozíciója és a hozzá tartozó sáv és út érvényes-e
        if (pozicio == null || pozicio.getSav() == null || pozicio.getUt() == null) {
            System.out.println("    >>> [KUDARC] A hókotró pozíciója érvénytelen!");
            return;
        }

        //Aktuális pozícióhoz tartozó sáv és út lekérdezése
        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();

        //A szomszédos sáv (melle) meghatározása (pl. Söprőfejnek kell)
        Sav melle = null;
        List<Sav> szakasz = pozicio.getSzakasz();
        
        //Ha a szakasz érvényes és több sáv is van, megpróbáljuk meghatározni a melletti sávot
        if (szakasz != null && szakasz.size() > 1) {
            int idx = szakasz.indexOf(cel);
            //Először megpróbáljuk jobbra (nagyobb index) áttolni a havat
            if (idx + 1 < szakasz.size()) {
                melle = szakasz.get(idx + 1);
            } 
            //Ha jobb szélen vagyunk, megpróbáljuk balra (kisebb index)
            else if (idx - 1 >= 0) {
                melle = szakasz.get(idx - 1);
            }
        }

        //Tisztítási logika hívása a felszerelt fejen
        felszereltFej.tisztit(cel, melle, ut);
    }

    /**
     * A hókotró ütközését figyelő metódus, amely kiírja a baleset tényét és a hókotró azonosítóját, 
     * majd mozgásképtelenné teszi a járművet.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> [BALESET] A(z) " + id + " hókotró ütközött és mozgásképtelenné vált!");
        mozgasKeptelen();
    }

    // --- GETTEREK ---
    public String getId() { return id; }
    
    @Override
    public int getAr() { return ar; }
    
    public KotroFej getFelszereltFej() { return felszereltFej; }
    
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    
    public Hokotro getHokotro() { return this; }
}