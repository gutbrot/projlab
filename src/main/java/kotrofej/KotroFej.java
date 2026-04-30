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
        this.ar = Math.max(0, ar);
    }

    /**
     * Absztrakt metódus a tisztítás végrehajtásához
     * Paraméterként kapja a célsávot, a mellette lévőt és az utat, 
     * hogy a fej típusa szerint módosítsa a hóviszonyokat
     * 
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A szomszédos sáv (pl. hó áttolásához).
     * @param ut Az út, amelyen a takarítás folyik.
     */
    public abstract void tisztit(Sav cel, Sav melle, Ut ut);

    /**
     * Visszaadja az aktuális fej típusát (nevét)
     * @return A fej megnevezése.
     */
    public abstract String getNev();

    /**
     * Az interfész metódusa, amely kezeli a vásárlást 
     * Levonja az árat a játékostól és hozzáadja a fejet a játékos eszköztárához
     * 
     * @param v A vásárlást végző Takarító.
     */
    @Override
    public void atadVevonek(Takarito v) {
        // A dokumentáció szerinti logika: ár levonása és hozzáadás az eszköztárhoz
        if (v != null) {
            // A tranzakció során egy új példányt adunk át
            KotroFej ujFej = this.getKotroFej();
            v.getEszkoztar().hozzaadFej(ujFej);
        }
    }

    /**
     * Visszaadja a fej árát
     * @return A termék aktuális ára.
     */
    @Override
    public int getAr() {
        return ar;
    }

    /**
     * Visszaadja a fej állapotát (egy új példányt az adott típusból)
     * @return A kotrófej objektum.
     */
    public abstract KotroFej getKotroFej();
}