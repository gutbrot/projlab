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
    
    //Tárolja a fejhez tartozó árat
    protected int ar;

    //KONSTRUKTOR
    protected KotroFej(int ar) {
        //Biztosítja, hogy az ár ne lehessen negatív
        this.ar = Math.max(0, ar);
    }

    //GETTEREK
    @Override
    public int getAr() {
        return ar;
    }

    public abstract KotroFej getKotroFej();

    /**
     * Absztrakt metódus a tisztítás végrehajtásához.
     * Paraméterként kapja a célsávot, a mellette lévőt és az utat, 
     * hogy a fej típusa szerint módosítsa a hóviszonyokat.
     */
    public abstract void tisztit(Sav cel, Sav melle, Ut ut);

    /**
     * Visszaadja az aktuális fej típusát (nevét).
     */
    public abstract String getNev();

    /**
     * Az interfész metódusa, amely kezeli a vásárlást.
     * Átadja az adott fejet a játékos által irányított hókotrónak.
     */
    @Override
    public void atadVevonek(Takarito v) {
        //Ellenőrizzük, hogy létezik-e a vásárló játékos
        if (v != null) {
            //Létrehozunk egy teljesen új példányt a megvásárolt fejből (Factory minta)
            KotroFej ujFej = this.getKotroFej();
            
            //Hozzáadjuk a játékos aktív hókotrójának eszköztárához
            v.getEszkoztar().hozzaadFej(ujFej);
            
            //Narratív logolás a játékos felé
            System.out.println(">>> [BOLT] A(z) " + this.getNev() + " sikeresen átadva a játékosnak és bekerült az eszköztárba.");
        } else {
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált fejet vásárolni!");
        }
    }

}