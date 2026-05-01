package kotrofej;

import terkep.*;
import jatekos.*;
import bolt.*;

/**
 * A KotroFej osztály felelős a takarító fejek egységes kezeléséért. 
 * Absztrakt, tehát minden kotrófejnek tudnia kell takarítani, egyedi névvel 
 * kell rendelkeznie, és eladhatónak kell lennie a boltban.
 */
public abstract class KotroFej implements IBoltiCikk {
    
    /** Tárolja a fejhez tartozó árat */
    protected int ar;

    /**
     * Konstruktor a kotrófej árának beállításához.
     * @param ar A fej vételára.
     */
    protected KotroFej(int ar) {
        // Biztosítja, hogy az ár ne lehessen negatív
        this.ar = Math.max(0, ar);
    }

    /**
     * Absztrakt metódus a tisztítás végrehajtásához.
     * Paraméterként kapja a célsávot, a mellette lévőt és az utat, 
     * hogy a fej típusa szerint módosítsa a hóviszonyokat.
     * 
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A szomszédos sáv (pl. hó áttolásához).
     * @param ut Az út, amelyen a takarítás folyik.
     */
    public abstract void tisztit(Sav cel, Sav melle, Ut ut);

    /**
     * Visszaadja az aktuális fej típusát (nevét).
     * @return A fej megnevezése.
     */
    public abstract String getNev();

    /**
     * Az interfész metódusa, amely kezeli a vásárlást.
     * Átadja az adott fejet a játékos által irányított hókotrónak.
     * 
     * @param v A vásárlást végző Takarító.
     */
    @Override
    public void atadVevonek(Takarito v) {
        // Ellenőrizzük, hogy létezik-e a vásárló játékos
        if (v != null) {
            // Létrehozunk egy teljesen új példányt a megvásárolt fejből (Factory minta)
            KotroFej ujFej = this.getKotroFej();
            
            // Hozzáadjuk a játékos aktív hókotrójának eszköztárához
            v.getEszkoztar().hozzaadFej(ujFej);
            
            // Narratív logolás a játékos felé
            System.out.println(">>> [BOLT] A(z) " + this.getNev() + " sikeresen átadva a játékosnak és bekerült az eszköztárba.");
        } else {
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált fejet vásárolni!");
        }
    }

    /**
     * Visszaadja a fej árát.
     * @return A termék aktuális ára.
     */
    @Override
    public int getAr() {
        return ar;
    }

    /**
     * Visszaadja a fej állapotát (egy új példányt az adott típusból).
     * A Bolt használja ezt, hogy új példányokat tudjon generálni az eladáshoz.
     * @return A kotrófej objektum.
     */
    public abstract KotroFej getKotroFej();
}