package kotrofej;

import terkep.*;

/**
 * A ZuzottKoFej osztály a takarítási folyamat egy speciális típusát valósítja meg.
 * Hókotróra szerelhető fej, amely zúzalék szórásával segíti a tapadást.
 */
public class ZuzottFej extends KotroFej {
    /** Megadja a működéshez szükséges zúzalék mennyiségét. */
    private int zuzalekIgeny;

    /**
     * Konstruktor a ZuzottFej létrehozásához.
     * @param ar A kotrófej ára.
     * @param zuzalekIgeny A tisztításhoz szükséges zúzalékmennyiség.
     */
    public ZuzottFej(int ar, int zuzalekIgeny) {
        super(ar);
        this.zuzalekIgeny = zuzalekIgeny;
    }

    /**
     * Megvalósítja a zúzottkő-szóró fej tisztító metódusát.
     * Beállítja a cél sáv zúzalékos állapotát.
     * @param cel A sáv, amelyen a szórás történik.
     * @param melle A mellette lévő sáv.
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            // A kérésednek megfelelően itt állítjuk be a sáv állapotát.
            // Ha a Sav osztályban van setZuzalekos metódus:
            cel.setZuzalekos(true);
            
            // Megjegyzés: Ha a diagramon szereplő zuzalekosE() egy olyan metódus, 
            // ami belsőleg állítja át az állapotot, akkor: cel.zuzalekosE();
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve.
     */
    @Override
    public String getNev() { 
        return "ZuzottKoFej"; 
    }

    /**
     * Visszaadja a működéshez szükséges zúzalékigényt.
     * @return A szükséges mennyiség.
     */
    public int getZuzalekIgeny() { 
        return zuzalekIgeny; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * @return Egy új ZuzottFej objektum a jelenlegi adatokkal.
     */
    @Override
    public KotroFej getKotroFej() { 
        return new ZuzottFej(getAr(), zuzalekIgeny); 
    }
}