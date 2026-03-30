package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jarmu.*;

/**
 * A Terkep osztály a játék világának központi tárolója.
 * Felelős az úthálózat (Utak listája) nyilvántartásáért, a környezeti hatások 
 * (időjárás) globális kezeléséért, valamint a járművek mozgásának koordinálásáért.
 * Ez az osztály köti össze a különböző úttípusokat egy egységes hálózattá.
 */
public class Terkep {
    /** A játékban szereplő összes útszakasz (SimaUt, Hid, Alagut) gyűjteménye. */
    private final List<Ut> utak = new ArrayList<>();
    
    /**
     * Új útszakasz hozzáadása a térkép hálózatához.
     * @param ut A hozzáadandó Ut objektum.
     */
    public void addUt(Ut ut) {
        if (ut != null) utak.add(ut);
    }
    
    /**
     * Visszaadja a teljes úthálózatot.
     * @return Az utak listájának nem módosítható változata a biztonságos elérés érdekében.
     */
    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }
    
    /**
     * Szimulálja az időjárás változását a teljes térképen.
     * Minden körben meghívódik, és minden útszakaszon elindítja a havazás folyamatát.
     * Az utak típusa (pl. Alagút) határozza meg, hogy ott ténylegesen nő-e a hóvastagság.
     */
    public void idojarasFrissites(){
        for (Ut ut : utak) {
            // A környezeti tényezők alapján 5 egységnyi havat adunk minden nyitott útszakaszhoz.
            ut.havazik(5);
        }
    }
    
    /**
     * Kezeli egy jármű helyváltoztatását a térkép sávjai között.
     * Ellenőrzi az érvényességet és meghívja a jármű saját mozgási logikáját.
     * @param jarmu A mozgatni kívánt jármű (Hókotró, Busz vagy Autó).
     * @param sav A célsáv, ahová a jármű el szeretne jutni.
     * @return True, ha a mozgás az útviszonyok és a foglaltság alapján sikeres volt.
     */
    public boolean jarmuMozgatas(Jarmu jarmu, Sav sav) {
        return jarmu != null && jarmu.mozgas(sav);
    }
}