package bolt;

import java.util.LinkedHashMap;
import java.util.Map;

import jatekos.Takarito;

/**
 * A játékban található boltot reprezentáló osztály. 
 * Felelős a vásárolható cikkek készletének kezeléséért. 
 * Itt vásárolhatnak a Takarítók különböző eszközöket és alapanyagokat.
 */
public class Bolt {
    /**
     * A boltban elérhető árukészletet tárolja.
     * A Map kulcsa a termék neve, értéke pedig maga a cikk (IBoltiCikk).
     */
    private final Map<String, IBoltiCikk> kinalat = new LinkedHashMap<>();
    
    /**
     * Termék felvétele a bolt kínálatába.
     * @param nev A termék neve.
     * @param cikk A boltba bekerülő termék objektuma.
     */
    public void felveszTermek(String nev, IBoltiCikk cikk) {
        if (nev != null && cikk != null) {
            kinalat.put(nev, cikk);
        }
    }
    
    /**
     * Kilistázza a boltban aktuálisan elérhető összes terméket, azok áraival együtt.
     */
    public void listaz() {
        for (Map.Entry<String, IBoltiCikk> e : kinalat.entrySet()) {
            System.out.println(e.getKey() + " - ár: " + e.getValue().getAr());
        }
    }
    
    /**
     * Kezeli a teljes vásárlási folyamatot és a tranzakció lebonyolítását.
     * Ellenőrzi a termék meglétét és a vásárló pénzkeretét.
     * @param v A vásárlást végző Takarító játékos. 
     * @param termek A megvásárolni kívánt termék neve. 
     * @return true, ha a vásárlás sikeres, egyébként false.
     */
    public boolean vasarlas(Takarito v, String termek) {
        if (v == null || termek == null) return false;
        IBoltiCikk cikk = kinalat.get(termek);
        // Ellenőrizzük, hogy létezik-e a cikk és van-e rá elég pénze a Takarítónak
        if (cikk == null || v.getPenz() < cikk.getAr()) return false;
        
        // Tranzakció: levonjuk az árat és átadjuk a terméket
        v.penztKap(-cikk.getAr());
        cikk.atadVevonek(v);
        return true;
    }
    
    /**
     * Visszaadja a bolt aktuális kínálatát.
     * @return A kínálatot tartalmazó Map.
     */
    public Map<String, IBoltiCikk> getKinalat() {
        return kinalat;
    }
}