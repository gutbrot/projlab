package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.*;

/**
 * A Terkep osztály a játék gráfstruktúrájának kezelője.
 * Koordináták helyett az utak és sávok közötti logikai kapcsolatokat tárolja.
 */
public class Terkep {
    /** A gráf éleit (útszakaszokat) tároló lista. */
    private List<Ut> utak = new ArrayList<>();
    
    public void addUt(Ut ut) {
    if (ut != null) {
        utak.add(ut);
        //Logolás a prototípushoz, hogy látszódjon a gráf épülése
        System.out.println("Gráf csomópont hozzáadva: " + ut.getNev());
    }
}
    
    /**
     * A gráf alapú mozgás. 
     * Jármű kéri a következő érvényes csomópontot.
     */
    public boolean jarmuMozgatas(Jarmu jarmu, Sav celSav) {
        if (jarmu == null || celSav == null) return false;
        
        // A jármű a saját belső állapotát és a célsáv járhatóságát veti össze.
        return jarmu.mozgas(celSav);
    }

    /**
     * Szimulálja a környezeti változók terjedését a gráfon.
     */
    public void idojarasFrissites() {
        for (Ut ut : utak) {
            ut.havazik(5); // Az út típusa dönti el, hogyan változik a gráf éleinek súlya (hóvastagság).
        }
    }

    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }
}