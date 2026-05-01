package bolt;

import java.util.LinkedHashMap;
import java.util.Map;
import jatekos.Takarito;
import kotrofej.*;

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
    
    public boolean vasarlas(Takarito v, String termek) {
        IBoltiCikk cikk = kinalat.get(termek);
        if (cikk == null) {
            System.out.println("    [Bolt] A keresett termék nem található a kínálatban.");
            return false;
        }

        int ar = cikk.getAr();

        if (v.getPenz() >= ar) {
            v.penztLevon(ar);
            cikk.atadVevonek(v);
            return true;
        } else {
            System.out.println("    [Bolt] Nincs elég pénz a vásárláshoz! (Ár: " + ar + ", Pénzed: " + v.getPenz() + ")");
            return false;
        }
    }
    
    public Map<String, IBoltiCikk> getKinalat() {
        return kinalat;
    }
}