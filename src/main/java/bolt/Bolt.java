package bolt;

import java.util.LinkedHashMap;
import java.util.Map;
import jatekos.Takarito;
import kotrofej.*;
import jarmu.*;

/**
 * A Bolt osztály felelős a vásárolható cikkek készletének és tranzakcióknak a kezeléséért.
 */
public class Bolt {
    
    private final Map<String, IBoltiCikk> kinalat = new LinkedHashMap<>();
    
    /**
     * A konstruktorban alapértelmezetten feltöltjük a boltot a játék összes 
     * elérhető fogyóanyagával és kotrófejével.
     */
    public Bolt() {
        // --- FOGYÓANYAGOK ---
        kinalat.put("SoCsomag", new SoCsomag(10, 10)); // 10 egység, 10 pénz
        kinalat.put("ZuzalekCsomag", new zuzalekCsomag(10, 15));
        kinalat.put("BiokerozinCsomag", new BiokerozinCsomag(10, 20));
        
        // --- KOTRÓFEJEK ---
        kinalat.put("SoproFej", new SoproFej(30));
        kinalat.put("HanyoFej", new HanyoFej(40));
        // A sószóró és zúzottfej paraméterei: (ár, anyagigény)
        kinalat.put("SoszoroFej", new SoszoroFej(50, 5));
        kinalat.put("ZuzottFej", new ZuzottFej(50, 5));
        kinalat.put("JegtoroFej", new JegtoroFej(60));
        // A sárkányfej paraméterei: (ár, biokerozinIgeny)
        kinalat.put("SarkanyFej", new SarkanyFej(100, 10));
        // A hókotró
        kinalat.put("Hokotro", new Hokotro("TEMPLATE", null, null));
    }
    
    public void felveszTermek(String nev, IBoltiCikk cikk) {
        if (nev != null && cikk != null) {
            kinalat.put(nev, cikk);
        }
    }
    
    public void listaz() {
        System.out.println(">>> Bolt kínálata:");
        for (Map.Entry<String, IBoltiCikk> e : kinalat.entrySet()) {
            System.out.println("    - " + e.getKey() + " (" + e.getValue().getAr() + " pénz)");
        }
    }
    
    // A vásárlás logikája
    public boolean vasarlas(Takarito v, String termek) {
        // Először megpróbáljuk lekérni a terméket a kínálatból
        IBoltiCikk cikk = kinalat.get(termek);
        // Ha a termék nem található, visszajelzünk a vásárlónak és hamissal térünk vissza.
        if (cikk == null) {
            System.out.println("    [Bolt] A keresett termék nem található a kínálatban.");
            return false;
        }
        // Ezután lekérjük a termék árát, és ellenőrizzük, hogy a vásárlónak van-e elég pénze.
        int ar = cikk.getAr();

        // Ha a vásárlónak van elég pénze, akkor levonjuk a pénzt, átadjuk a terméket a vevőnek, és visszajelzünk a sikeres vásárlásról.
        if (v.getPenz() >= ar) {
            v.penztLevon(ar);
            cikk.atadVevonek(v);
            return true;
        }
        // Ha nincs elég pénze, akkor visszajelzünk a vásárlónak a sikertelenségről.
        else {
            System.out.println("    [Bolt] Nincs elég pénz a vásárláshoz! (Ár: " + ar + ", Pénzed: " + v.getPenz() + ")");
            return false;
        }
    }
    
    public Map<String, IBoltiCikk> getKinalat() {
        return kinalat;
    }
}