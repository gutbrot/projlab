package bolt;

import java.util.LinkedHashMap;
import java.util.Map;
import jatekos.Takarito;
import skeleton.Skeleton;

/**
 * A Bolt osztály felelős a vásárolható cikkek készletének és 
 * a tranzakcióknak a kezeléséért. 
 * Itt vásárolhatnak a takarítók különböző eszközöket és alapanyagokat.
 */
public class Bolt {
    
    /** A boltban elérhető termékek listáját tárolja. */
    private final Map<String, IBoltiCikk> kinalat = new LinkedHashMap<>();
    
    /**
     * Új termék felvétele a bolt kínálatába.
     * 
     * @param nev A termék azonosítója.
     * @param cikk A termék objektuma.
     */
    public void felveszTermek(String nev, IBoltiCikk cikk) {
        if (nev != null && cikk != null) {
            kinalat.put(nev, cikk);
        }
    }
    
    /**
     * Kilistázza a bolt kínálatát a konzolra.
     */
    public void listaz() {
        System.out.println(">>> Bolt kínálata:");
        for (Map.Entry<String, IBoltiCikk> e : kinalat.entrySet()) {
            System.out.println("    - " + e.getKey() + " (" + e.getValue().getAr() + " pénz)");
        }
    }
    
    /**
     * Lebonyolítja a vásárlást a Takarító és a Bolt között
     * Követi a dokumentáció 10. oldalán található aktivitásdiagramot
     * 
     * @param v A vásárlást végző Takarító
     * @param termek A megvásárolni kívánt termék neve
     * @return True, ha a vásárlás sikeres volt.
     */
    public boolean vasarlas(Takarito v, String termek) {
        Skeleton.functionCalled("vasarlas", this, "boolean", v, termek);

        // 1. Kínálat ellenőrzése
        IBoltiCikk cikk = kinalat.get(termek);
        if (cikk == null) {
            System.out.println("    [Bolt] A termék nem található.");
            return Skeleton.functionReturn(false);
        }

        // 2. Ár lekérése[cite: 1]
        int ar = cikk.getAr();

        // 3. Megfelelő mennyiségű pénz ellenőrzése
        if (v.getPenz() >= ar) {
            // 4. Pénz levonása a játékostól
            v.penztLevon(ar);

            // 5. Termék átadása a vásárlónak
            cikk.atadVevonek(v);

            System.out.println("    [Bolt] Sikeres vásárlás: " + termek);
            return Skeleton.functionReturn(true);
        } else {
            System.out.println("    [Bolt] Nincs elég pénz a vásárláshoz.");
            return Skeleton.functionReturn(false);
        }
    }
    
    /**
     * Visszaadja a bolt aktuális kínálatát.
     * 
     * @return A kínálat Map objektuma.
     */
    public Map<String, IBoltiCikk> getKinalat() {
        return kinalat;
    }
}