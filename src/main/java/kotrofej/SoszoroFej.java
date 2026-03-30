package kotrofej;

import terkep.*;

/**
 * A SoszoroFej osztály felelős a takarítási folyamat egy típusának megvalósításáért.
 * Hókotróra szerelhető kotró fej, amely képes a havat és a jeget felolvasztani megfelelő 
 * fogyóanyag mennyiség (só) birtokában.
 * Felelős a jégpáncél és a hó felolvasztásáért.
 */
public class SoszoroFej extends KotroFej {
    /** Megadja, hogy mennyi só van jelenleg készleten, amit a hókotró fel tud használni a működéshez. */
    private int solgeny;

    /**
     * Konstruktor a SoszoroFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható.
     * @param solgeny A tisztításhoz szükséges sómennyiség igénye.
     */
    public SoszoroFej(int ar, int solgeny) {
        super(ar);
        this.solgeny = solgeny;
    }

    /**
     * Megvalósítja a sószóró fej tisztító metódusát.
     * Feladata a takarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávban felolvasztja-e a jeget a fogyóanyag rendelkezésre állásának mértékében.
     * A paraméterként kapott sávok havát olvasztja fel a takarítási szabályok szerint.
     * @param cel A sáv, amelyen a sózás és az olvasztás történik.
     * @param melle A mellette lévő sáv.
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            // A sószóró fej a sózó funkció meghívásával csökkenti a hóvastagságot.
            cel.soOlvadas();
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "SoszoroFej".
     */
    @Override
    public String getNev() { return "SoszoroFej"; }

    /**
     * Visszaadja a működéshez szükséges sómennyiség igényt.
     * @return A szükséges sómennyiség.
     */
    public int getSolgeny() { return solgeny; }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új SoszoroFej objektum a jelenlegi árral és sóigénnyel.
     */
    @Override
    public KotroFej getKotroFej() { return new SoszoroFej(getAr(), solgeny); }
}