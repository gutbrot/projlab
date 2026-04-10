package bolt;

import jatekos.Takarito;

/**
 * A ZuzalekCsomag osztály a rendszerben fellelhető egyik fogyóanyagot reprezentálja.
 * Alapanyagul szolgál a zúzottkő-szóró (ZuzottFej) kotrófejnek.
 * Felelőssége a zúzalék tárolása és átadása a takarító járművek számára.
 */
public class zuzalekCsomag extends FogyoAnyag {

    /**
     * Konstruktor a ZuzalekCsomag létrehozásához.
     * @param mennyiseg A csomagban tárolt zúzalék mennyisége.
     * @param ar A termék aktuális ára.
     */
    public zuzalekCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamatot.
     * Átadja a zúzalék mennyiséget a paraméterként kapott takarító járműnek.
     * @param v A vásárlást végző Takarító játékos.
     */
    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            // A zúzalékot hozzáadjuk a takarító eszköztárához.
            // Feltételezve, hogy a String azonosító "zuzalek".
            v.getEszkoztar().hozzaad("zuzalek", mennyiseg);
        }
    }

    /**
     * Visszaadja a termék megnevezését.
     * @return A termék neve: "ZuzalekCsomag".
     */
    @Override
    public String getNev() {
        return "ZuzalekCsomag";
    }
}