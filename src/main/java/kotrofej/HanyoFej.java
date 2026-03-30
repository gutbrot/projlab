package kotrofej;

import terkep.*;

/**
 * A HanyoFej osztály felelős a takarítási folyamat egy speciális típusának megvalósításáért.
 * Hókotróra szerelhető kotrófej, amely képes a havat és a feltört jeget a hókotró 
 * nyomvonalától több sávval arrébb szórni.
 * Felelős a hó, illetve a feltört jég útról való eltakarításáért.
 */
public class HanyoFej extends KotroFej {

    /**
     * Konstruktor a HanyoFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható.
     */
    public HanyoFej(int ar) {
        super(ar);
    }

    /**
     * Megvalósítja a hányófej tisztító metódusát.
     * Meghatározza, hogy a hókotró az aktuális sávból a mellette lévő sávba 
     * vagy az út szélére mozgatja-e át a havat.
     * A paraméterként kapott sávok hóvastagságát módosítja a takarítási szabályok szerint.
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A sáv, ahová a havat átmozgatja a fej.
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel == null) return;
        
        // Lekérdezzük az aktuális hómennyiséget a célsávból
        int ho = cel.getHo();
        
        // A célsávot megtisztítjuk (hóvastagság 0 lesz)
        cel.setHo(0);
        
        // Ha van szomszédos sáv, oda szórjuk át a feltakarított havat
        if (melle != null) {
            melle.setHo(melle.getHo() + ho);
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "HanyoFej".
     */
    @Override
    public String getNev() {
        return "HanyoFej";
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új HanyoFej objektum a jelenlegi árral.
     */
    @Override
    public KotroFej getKotroFej() {
        return new HanyoFej(getAr());
    }
}