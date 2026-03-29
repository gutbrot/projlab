package bolt;

import java.util.LinkedHashMap;
import java.util.Map;

import jatekos.Takarito;

public class Bolt {
	private final Map<String, IBoltiCikk> kinalat = new LinkedHashMap<>();
    
    public void felveszTermek(String nev, IBoltiCikk cikk) {
        if (nev != null && cikk != null) {
            kinalat.put(nev, cikk);
        }
    }
    
    public void listaz() {
        for (Map.Entry<String, IBoltiCikk> e : kinalat.entrySet()) {
            System.out.println(e.getKey() + " - ár: " + e.getValue().getAr());
        }
    }
    
    public boolean vasarlas(Takarito v, String termek) {
        if (v == null || termek == null) return false;
        IBoltiCikk cikk = kinalat.get(termek);
        if (cikk == null || v.getPenz() < cikk.getAr()) return false;
        v.penztKap(-cikk.getAr());
        cikk.atadVevonek(v);
        return true;
    }
    
    public Map<String, IBoltiCikk> getKinalat() {
        return kinalat;
    }
}
