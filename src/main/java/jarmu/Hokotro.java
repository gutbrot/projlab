package jarmu;

import jatekos.Takarito;
import kotrofej.KotroFej;
import terkep.*;

/**
 * A játékos által irányított jármű fajta, aminek a különböző útviszonyokon való közlekedését 
 * az aktuálisan felszerelt kotró fej befolyásolja
 * Célja, hogy letakarítsa a buszok elől az akadályozó tényezőket (hó, jég, feltört jég) 
 * és ezzel segítse a buszok közlekedését
 * Elsődleges feladata az úthálózat tisztán tartása a rá felszerelt eszközök segítségével.
 */
public class Hokotro extends Jarmu {
    /** Eltárolja, hogy a hókotrón jelenleg milyen fej van. */
    private KotroFej felszereltFej;
    
    /**
     * Konstruktor a Hokotro példányosításához.
     * @param pozicio A jármű kezdeti pozíciója a térképen
     * @param felszereltFej Az indításkor a járműre szerelt kotrófej
     */
    public Hokotro(Lokacio pozicio, KotroFej felszereltFej) {
        super(pozicio);
        this.felszereltFej = felszereltFej;
    }
    
    /**
     * Megvalósítja a vásárlás lebonyolítását és a jármű takarító játékosnak való átadását
     * @param v A vásárlást végző Takarító játékos
     */
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.hozzaadHokotro(this);
        }
    }

    /**
     * A fejcsere műveletét valósítja meg[cite: 478].
     * A játékos kiválasztja az aktuális körben használt felszerelhető kotró fejet
     * @param fej Az új, felszerelni kívánt kotrófej
     */
    public void fejcsere(KotroFej fej) {
        if (fej != null) {
            this.felszereltFej = fej;
        }
    }
    
    /**
     * Meghívja a kotrófej tisztító metódusát, amely módosítja az érintett útszakasz állapotát a térképen
     * Felelős a tisztítási folyamat végrehajtásáért
     * @param terkep A játéktér térképe, amelyen a takarítás zajlik
     */
    public void takarit(Terkep terkep) {
        if (pozicio == null || felszereltFej == null) return;
        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();
        Sav melle = null;
        // Meghatározzuk a cél sáv melletti sávot a takarítási szabályokhoz
        if (pozicio.getSzakasz() != null) {
            int idx = pozicio.getSzakasz().indexOf(cel);
            if (idx > 0) melle = pozicio.getSzakasz().get(idx - 1);
            else if (idx + 1 < pozicio.getSzakasz().size()) melle = pozicio.getSzakasz().get(idx + 1);
        }
        // A felszerelt fej típusától függően elvégzi a tisztítást
        felszereltFej.tisztit(cel, melle, ut);
    }

    /**
     * Megvalósítja az ütközéskezelést
     * Ütközés esetén a jármű ideiglenesen mozgásképtelenné válik
     */
    @Override
    public void utkozos() {
        mozgasKeptelen();
    }

    /**
     * Visszaadja a jármű aktuális állapotát
     * @return Önmagát, mint Hokotro példányt.
     */
    public Hokotro getHokotro() { return this; }
    
    /**
     * @return A járműre aktuálisan felszerelt kotrófej objektuma
     */
    public KotroFej getFelszereltFej() { return felszereltFej; }
}