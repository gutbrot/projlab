package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.Jarmu;
import skeleton.Skeleton;

/**
 * A Terkep osztály felelős az úthálózat gráfjának globális kezeléséért.
 * Nyilvántartja az összes útszakaszt, és koordinálja az olyan globális 
 * eseményeket, mint az időjárás frissítése
 */
public class Terkep {
    
    /** A gráf éleit (útszakaszokat) tároló lista */
    private List<Ut> utak = new ArrayList<>();
    
    /**
     * Új út hozzáadása a térkép hálózatához.
     * 
     * @param ut A hozzáadandó Ut objektum.
     */
    public void addUt(Ut ut) {
        if (ut != null) {
            utak.add(ut);
        }
    }
    
    /**
     * Kezdeményezi a jármű mozgását a térképen.
     * A kérést delegálja a jármű saját mozgáslogikája felé.
     * 
     * @param jarmu A mozgatni kívánt jármű.
     * @param celSav A sáv, ahová a jármű lépni szeretne.
     * @return True, ha a mozgás sikeresen lezajlott.
     */
    public boolean jarmuMozgatas(Jarmu jarmu, Sav celSav) {
        Skeleton.functionCalled("jarmuMozgatas", this, "boolean", jarmu, celSav);
        
        if (jarmu == null || celSav == null) {
            return Skeleton.functionReturn(false);
        }
        
        // A Térkép meghívja a Jármű mozgás metódusát a cél sávval
        boolean siker = jarmu.mozgas(celSav);
        
        return Skeleton.functionReturn(siker);
    }

    /**
     * Szimulálja a környezeti változók terjedését a hálózaton.
     * Végigiterál az utakon, és minden úton kiváltja a havazást.
     * A dokumentáció 15. oldalán található aktivitásdiagram alapján.
     */
    public void idojarasFrissites() {
        Skeleton.functionCalled("idojarasFrissites", this, "void");
        
        // Ciklus indítása az utak listáján
        for (Ut ut : utak) {
            // Minden úton meghívjuk a havazás függvényt
            // Prototípus szinten fixen 5 egységnyi hó hullik minden körben.
            ut.havazik(5); 
        }
        
        Skeleton.voidReturn();
    }

    /**
     * Visszaadja a térkép útjainak listáját.
     * 
     * @return Az utak módosíthatatlan listája.
     */
    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }
}