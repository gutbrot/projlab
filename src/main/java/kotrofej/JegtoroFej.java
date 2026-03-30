package kotrofej;

import terkep.*;

/**
 * A JegtoroFej osztály felelős a takarítási folyamat egy speciális típusának megvalósításáért.
 * Hókotróra szerelhető kotró fej, amely képes a jeget feltörni. 
 * Felelős a jégpáncél feltöréséért, azonban a feltört jeget nem tudja feltakarítani.
 */
public class JegtoroFej extends KotroFej {

    /**
     * Konstruktor a JegtoroFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható.
     */
    public JegtoroFej(int ar) { super(ar); }

    /**
     * Megvalósítja a jégtörő fej tisztító metódusát.
     * Feladata a takarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávban feltöri-e a jeget.
     * A paraméterként kapott sávok jegét töri fel a takarítási szabályok szerint.
     * @param cel A sáv, amelyen a jégpáncél feltörése történik.
     * @param melle A mellette lévő sáv (ebben a megvalósításban nem érintett).
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Ha a célsáv létezik, megszünteti annak jeges állapotát
        if (cel != null) cel.setJeges(false);
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "JegtoroFej".
     */
    @Override
    public String getNev() { return "JegtoroFej"; }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új JegtoroFej objektum a jelenlegi árral.
     */
    @Override
    public KotroFej getKotroFej() { return new JegtoroFej(getAr()); }
}