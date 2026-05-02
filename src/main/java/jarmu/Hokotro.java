package jarmu;

import jatekos.Takarito;
import eszkoztar.Eszkoztar;
import kotrofej.KotroFej;
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
    private static int vasaroltSzamlalo = 1; // Vásárlások számlálója a bolti azonosítóhoz
    
    public Hokotro(String id, Lokacio pozicio, KotroFej felszereltFej) {
        super(pozicio);
        this.id = id;
        this.felszereltFej = felszereltFej;
        this.eszkoztar = new Eszkoztar();
    }

    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            String ujAzonosito = "H_" + vasaroltSzamlalo++;
            Hokotro megvasaroltHokotro = new Hokotro(ujAzonosito, null, null);
            v.hozzaadHokotro(megvasaroltHokotro);
            System.out.println(">>> [BOLT] Sikeres vásárlás: A(z) " + ujAzonosito + " azonosítójú hókotró átadva a játékosnak.");
        } else {
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált járművet vásárolni!");
        }
    }

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

        if (pozicio == null || pozicio.getSav() == null || pozicio.getUt() == null) {
            System.out.println("    >>> [KUDARC] A hókotró pozíciója érvénytelen!");
            return;
        }

        // 1. Aktuális pozícióhoz tartozó sáv és út lekérdezése
        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();

        // 2. A szomszédos sáv (melle) meghatározása (pl. Söprőfejnek kell)
        Sav melle = null;
        List<Sav> szakasz = pozicio.getSzakasz();
        
        if (szakasz != null && szakasz.size() > 1) {
            int idx = szakasz.indexOf(cel);
            // Először megpróbáljuk jobbra (nagyobb index) áttolni a havat
            if (idx + 1 < szakasz.size()) {
                melle = szakasz.get(idx + 1);
            } 
            // Ha jobb szélen vagyunk, megpróbáljuk balra (kisebb index)
            else if (idx - 1 >= 0) {
                melle = szakasz.get(idx - 1);
            }
        }

        // 3. Tisztítási logika hívása a felszerelt fejen (Polimorfizmus)
        felszereltFej.tisztit(cel, melle, ut);
    }

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