package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.Jarmu;

/**
 * A Terkep osztály felelős az úthálózat gráfjának globális kezeléséért.
 */
public class Terkep {
    
    private List<Ut> utak = new ArrayList<>();
    
    public void addUt(Ut ut) {
        if (ut != null) {
            utak.add(ut);
        }
    }
    
    public boolean jarmuMozgatas(Jarmu jarmu, Sav celSav) {
        if (jarmu == null || celSav == null) {
            return false;
        }
        
        boolean siker = jarmu.mozgas(celSav);
        return siker;
    }

    public void idojarasFrissites() {
        System.out.println("\n>>> [RENDSZER] Időjárás frissítése: Elkezdett esni a hó!");
        
        for (Ut ut : utak) {
            ut.havazik(5); 
        }
    }

    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }
}