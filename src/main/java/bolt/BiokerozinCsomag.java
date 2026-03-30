package bolt;

import jatekos.Takarito;

/**
 * A BiokerozinCsomag osztály a rendszerben fellelhető egyik fogyóanyagot reprezentálja.
 * Felelőssége a biokerozin tárolása és átadása a takarító járművek számára.
 * Üzemanyagul szolgál a sárkány kotrófejnek.
 */
public class BiokerozinCsomag extends FogyoAnyag {
    
    /**
     * Konstruktor a BiokerozinCsomag létrehozásához.
     * Az osztály biztosítja, hogy a vásárlási folyamat során a készlet és az ár adatai megfelelőek legyenek
     * * @param mennyiseg A csomagban tárolt biokerozin mennyisége.
     * @param ar A termék aktuális ára.
     */
    public BiokerozinCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamatot.
     * Átadja a biokerozin mennyiséget a paraméterként kapott takarító játékosnak, és kezeli a tranzakciót.
     * A metóduson keresztül kapcsolatba lép egy takarítóval, amely megvásárolja az üzemanyagot
     * * @param v A vásárlást végző Takarító játékos
     */
    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            // Átadja a biokerozin mennyiséget a takarító eszköztárának.
            v.getEszkoztar().hozzaad("biokerozin", mennyiseg);
        }
    }

    /**
     * Visszaadja a termék megnevezését.
     * * @return A termék neve: "BiokerozinCsomag".
     */
    @Override
    public String getNev() {
        return "BiokerozinCsomag";
    }
}