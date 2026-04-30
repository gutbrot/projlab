package bolt;

import jatekos.Takarito;

/**
 * A FogyoAnyag osztály felelőssége a vásárolható és felhasználható készletek 
 * általános modelljének létrehozása. Összefoglalja az árazást, 
 * a mennyiséget és a neveket.
 * 
 * Megvalósítja az IBoltiCikk interfészt, biztosítva a boltban való eladások kezelését.
 */
public abstract class FogyoAnyag implements IBoltiCikk {
    
    /** A fogyóanyag mennyisége. */
    protected int mennyiseg;
    
    /** A termék aktuális ára. */
    protected int ar;

    /**
     * Konstruktor a fogyóanyag alapadatainak beállításához.
     * 
     * @param mennyiseg A csomagban lévő mennyiség.
     * @param ar A csomag vételára.
     */
    protected FogyoAnyag(int mennyiseg, int ar) {
        this.mennyiseg = Math.max(0, mennyiseg);
        this.ar = Math.max(0, ar);
    }

    /**
     * Absztrakt metódus, amelyet a leszármazottaknak kell megvalósítaniuk, 
     * hogy visszaadják a termék nevét.
     * 
     * @return A termék neve (pl. "SoCsomag").
     */
    public abstract String getNev();

    /**
     * Absztrakt metódus, amely a vásárlás lebonyolításáért és a készlet 
     * takarító játékosnak való átadásáért felel.
     * 
     * @param v A vásárlást végző Takarító játékos.
     */
    @Override
    public abstract void atadVevonek(Takarito v);

    /**
     * Visszaadja a termék árát.
     * 
     * @return A termék aktuális ára.
     */
    @Override
    public int getAr() {
        return ar;
    }

    /**
     * Visszaadja az eszközök aktuális állapotát.
     * 
     * @return Az objektum saját referenciája.
     */
    public FogyoAnyag getFogyo() {
        return this;
    }

    /**
     * Lekérdezi a csomagban tárolt mennyiséget.
     * 
     * @return A mennyiség értéke.
     */
    public int getMennyiseg() {
        return mennyiseg;
    }
}