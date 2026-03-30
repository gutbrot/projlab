package kotrofej;

import terkep.*;
import jatekos.*;
import bolt.*;
import eszkoztar.*;

/**
 * A KotroFej osztály felelős a takarító fejek egységes kezeléséért.
 * Absztrakt osztály, amely keretet ad a különböző tisztítási stratégiáknak.
 * Minden kotrófejnek tudnia kell takarítani, egyedi névvel kell rendelkeznie, 
 * és eladhatónak kell lennie a boltban.
 */
public abstract class KotroFej implements IBoltiCikk {
    /** Tárolja a fejhez tartozó vételárat. */
    private int ar;

    /**
     * Konstruktor a kotrófej alapértékeinek beállításához.
     * @param ar A kotrófej ára.
     */
    protected KotroFej(int ar) {
        this.ar = ar;
    }

    /**
     * Absztrakt metódus a takarítás implementálásához.
     * Paraméterként kapja a célsávot, a mellette lévőt és az utat, 
     * hogy a fej típusa szerint módosítsa a hó- és jégviszonyokat.
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A takarításban érintett szomszédos sáv.
     * @param ut Az útszakasz, amelyen a jármű tartózkodik.
     */
    public abstract void tisztit(Sav cel, Sav melle, Ut ut);

    /**
     * Visszaadja az aktuális fej típusát/nevét.
     * @return A kotrófej megnevezése.
     */
    public abstract String getNev();

    /**
     * Az IBoltiCikk interfész metódusa, amely kezeli a vásárlást.
     * Átadja a fejet a takarító játékosnak, aki hozzáadja azt az eszköztárához.
     * @param v A vásárlást végző Takarító játékos.
     */
    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.getEszkoztar().hozzaadFej(getKotroFej());
        }
    }

    /**
     * Visszaadja a kotrófej boltban érvényes árát.
     * @return A termék ára.
     */
    @Override
    public int getAr() {
        return ar;
    }

    /**
     * Absztrakt metódus, amely visszaadja a fej aktuális állapotát vagy egy új példányát.
     * @return A kotrófej objektum.
     */
    public abstract KotroFej getKotroFej();
}