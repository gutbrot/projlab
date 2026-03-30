package kotrofej;

import terkep.*;

/**
 * A SarkanyFej osztály felelős a takarítási folyamat egy speciális típusának megvalósításáért.
 * Hókotróra szerelhető kotró fej, amely képes a havat és a jeget felolvasztani megfelelő 
 * fogyóanyag mennyiség (biokerozin) birtokában.
 * Felelős a hó és jég útról való teljes eltakarításáért.
 */
public class SarkanyFej extends KotroFej {
    /** Megadja a tisztításhoz szükséges biokerozin mennyiségét. */
    private int biokerozinIgeny;

    /**
     * Konstruktor a SarkanyFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható.
     * @param biokerozinIgeny A működéshez szükséges üzemanyag mennyisége.
     */
    public SarkanyFej(int ar, int biokerozinIgeny) {
        super(ar);
        this.biokerozinIgeny = biokerozinIgeny;
    }

    /**
     * Megvalósítja a sárkány fej tisztító metódusát.
     * Feladata a takarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávban felolvasztja-e a jeget és a havat a fogyóanyag 
     * rendelkezésre állásának mértékében.
     * @param cel A sáv, amelyen a jégpáncél és a hó felolvasztása történik.
     * @param melle A mellette lévő sáv.
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            // A sárkányfej képes leolvasztani a havat és a jégpáncélt is az úttestről.
            cel.setHo(0);
            cel.setJeges(false);
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "SarkanyFej".
     */
    @Override
    public String getNev() { return "SarkanyFej"; }

    /**
     * Visszaadja a működéshez szükséges biokerozin igényt.
     * @return A szükséges üzemanyag mennyisége.
     */
    public int getBiokerozinIgeny() { return biokerozinIgeny; }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új SarkanyFej objektum a jelenlegi árral és igényekkel.
     */
    @Override
    public KotroFej getKotroFej() { return new SarkanyFej(getAr(), biokerozinIgeny); }
}