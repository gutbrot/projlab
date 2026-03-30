package bolt;

import jatekos.Takarito;

/**
 * A SoCsomag osztály a rendszerben fellelhető egyik fogyóanyagot reprezentálja
 * Üzemanyagul szolgál a sószóró kotrófejnek
 * Felelőssége a só tárolása és átadása a takarító járművek számára
 */
public class SoCsomag extends FogyoAnyag {
    /**
     * Konstruktor a SoCsomag létrehozásához.
     * Az osztály biztosítja, hogy a vásárlási folyamat során a készlet és az ár adatai megfelelőek legyenek
     * @param mennyiseg A csomagban tárolt só mennyisége.
     * @param ar A termék aktuális ára.
     */
    public SoCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamatot
     * Az atadVevonek metóduson keresztül kapcsolatba lép egy hókotróval (a takarítón keresztül), amely megvásárolja a fogyóanyagot
     * Átadja a só mennyiséget a paraméterként kapott takarító járműnek, és kezeli a tranzakciót
     * @param v A vásárlást végző Takarító játékos.
     */
    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            // Amennyiben a hókotrónak van kellő mennyiségű sója, képes lesózni az utakat
            v.getEszkoztar().hozzaad("so", mennyiseg);
        }
    }

    /**
     * Visszaadja a termék megnevezését
     * @return A termék neve: "SoCsomag".
     */
    @Override
    public String getNev() {
        return "SoCsomag";
    }
}