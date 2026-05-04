package bolt;

/**
 * A fogyóanyagok (só, zúzalék, biokerozin) közös ősosztálya.
 * Mivel ez implementálja az IBoltiCikk interfészt, az összes belőle
 * származó csomag automatikusan eladhatóvá válik a Boltban.
 */
public abstract class FogyoAnyag implements IBoltiCikk {
    
    //Védett (protected) attribútumok, hogy a leszármazott osztályok is lássák őket.
    protected int mennyiseg;
    protected int ar;

    /**
     * Az ősosztály konstruktora, ami inicializálja az alapvető tulajdonságokat.
     * Minden specifikus csomag ezt fogja meghívni a saját létrehozásakor.
     */
    public FogyoAnyag(int mennyiseg, int ar) {
        //Egyszerű adatvalidáció: megakadályozzuk, hogy negatív mennyiségű vagy negatív árú csomag jöjjön létre a rendszerben.
        this.mennyiseg = Math.max(0, mennyiseg);
        this.ar = Math.max(0, ar);
    }

    /**
     * Az IBoltiCikk interfészből kötelezően megvalósítandó metódus.
     * Ezt hívja meg a Bolt, amikor ellenőrzi, hogy a játékosnak van-e elég pénze.
     */
    @Override
    public int getAr() {
        return this.ar;
    }
}