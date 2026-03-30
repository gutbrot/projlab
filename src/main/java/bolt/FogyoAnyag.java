package bolt;

import jatekos.Takarito;

/**
 * A FogyoAnyag osztály felelőssége a vásárolható és felhasználható készletek általános modelljének létrehozása
 * Összefoglalja az árazást, a mennyiséget és a neveket
 * Megvalósítja az IBoltiCikk interfészt, biztosítva a boltban való eladások kezelését
 */
public abstract class FogyoAnyag implements IBoltiCikk {
    /** A fogyóanyag aktuális mennyisége */
    protected int mennyiseg;
    /** A termék aktuális ára */
    protected int ar;

    /**
     * Konstruktor a fogyóanyag alapadatainak beállításához.
     * @param mennyiseg A beállítandó mennyiség.
     * @param ar A termék ára.
     */
    protected FogyoAnyag(int mennyiseg, int ar) {
        this.mennyiseg = Math.max(0, mennyiseg);
        this.ar = Math.max(0, ar);
    }

    /**
     * Absztrakt metódus, amelyet a leszármazottaknak kell megvalósítaniuk, 
     * hogy visszaadják a termék nevét
     * @return A termék megnevezése.
     */
    public abstract String getNev();

    /**
     * Absztrakt metódus, amely a vásárlás lebonyolításáért és a készlet 
     * takarító játékosnak való átadásáért felel
     * @param v A takarító, aki megkapja a készletet.
     */
    @Override
    public abstract void atadVevonek(Takarito v);

    /**
     * Visszaadja a termék árát
     * @return Az aktuális ár.
     */
    @Override
    public int getAr() {
        return ar;
    }

    /**
     * Visszaadja az eszközök aktuális állapotát
     * @return Önmagát mint fogyóanyagot.
     */
    public FogyoAnyag getFogyo() {
        return this;
    }

    /**
     * Visszaadja a fogyóanyag aktuális mennyiségét.
     * @return A készlet mennyisége.
     */
    public int getMennyiseg() {
        return mennyiseg;
    }
}