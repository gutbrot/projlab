package kotrofej;

import terkep.*;

/**
 * A SoproFej osztály felelős a takarítási folyamat egy típusának megvalósításáért.
 * Hókotróra szerelhető kotró fej, amely képes a havat és a feltört jeget közvetlenül a 
 * hókotró nyomvonala mellé tolni.
 * Felelős a hó, illetve feltört jég útról való eltakarításáért.
 */
public class SoproFej extends KotroFej {

    /**
     * Konstruktor a SoproFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható.
     */
    public SoproFej(int ar) { super(ar); }

    /**
     * Megvalósítja a söprő fej tisztító metódusát.
     * Feladata a takarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávból a mellette lévő sávba mozgatja-e át a havat.
     * A paraméterként kapott sávok hóvastagságát módosítja a takarítási szabályok szerint.
     * @param cel A sáv, amelyet a hókotró éppen takarít (itt a hóvastagság 0 lesz).
     * @param melle A közvetlenül mellette lévő sáv, ahová a havat tolja.
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel == null) return;
        
        // Lekérdezzük az aktuális hómennyiséget a célsávból
        int ho = cel.getHo();
        
        // A célsávot megtisztítjuk (a hóvastagság 0 lesz)
        cel.setHo(0);
        
        // Ha van szomszédos sáv, közvetlenül oda toljuk át a havat
        if (melle != null) {
            melle.setHo(melle.getHo() + ho);
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "SoproFej".
     */
    @Override
    public String getNev() { return "SoproFej"; }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új SoproFej objektum a jelenlegi árral.
     */
    @Override
    public KotroFej getKotroFej() { return new SoproFej(getAr()); }
}