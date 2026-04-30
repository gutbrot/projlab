package kotrofej;

import terkep.*;

/**
 * A ZuzottFej (Zúzottkő-szóró) osztály a takarítási folyamat egy speciális típusát valósítja meg.
 * Hókotróra szerelhető fej, amely zúzalék szórásával segíti a tapadást a jeges utakon.
 * 
 * Bár a jégpáncélt nem töri fel és a havat sem takarítja el, a kiszórt zúzalék 
 * megakadályozza, hogy az arra közlekedő járművek (Autó, Busz) megcsússzanak 
 * és balesetet szenvedjenek.
 */
public class ZuzottFej extends KotroFej {
    
    /** 
     * Megadja az egyetlen szórási művelethez (egy sáv beszórásához) szükséges zúzalék mennyiségét. 
     * Ezt az értéket a rendszer a Takarító eszköztárából vonja le használat előtt.
     */
    private int zuzalekIgeny;

    /**
     * Konstruktor a ZuzottFej létrehozásához.
     * @param ar A kotrófej ára a Boltban.
     * @param zuzalekIgeny A tisztításhoz (szóráshoz) szükséges zúzalékmennyiség.
     */
    public ZuzottFej(int ar, int zuzalekIgeny) {
        // Átadja az árat az absztrakt ősosztálynak
        super(ar);
        // Biztosítja, hogy az anyagigény ne lehessen negatív
        this.zuzalekIgeny = Math.max(0, zuzalekIgeny);
    }

    /**
     * Megvalósítja a zúzottkő-szóró fej "tisztító" (szóró) metódusát.
     * Mire ez lefut, a Hokotro osztály már levonta a szükséges zúzalékot az eszköztárból.
     * A metódus beállítja a célsáv zúzalékos állapotát, így ott a járművek már biztonságosan áthaladhatnak.
     * 
     * @param cel A sáv, amelyen a kőszórás történik.
     * @param melle A mellette lévő sáv (a szórásnál nincs jelentősége).
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            System.out.println(">>> Zúzottfej bekapcsolva a(z) " + cel.getSavSzama() + ". sávban. Zúzalék szórása...");
            
            // Beállítjuk a sáv zúzalékos állapotát igazra.
            // (Ezt az állapotot a Jarmu.mozgas() metódus fogja ellenőrizni, hogy megelőzze a csúszást).
            cel.setZuzalekos(true);
            
            System.out.println(">>> SIKER: A sáv zúzalékos lett. A járművek ezen a szakaszon már nem csúsznak meg a jégen.");
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * Egységesítve lett a fájl és az osztály nevével.
     * @return A fej neve: "ZuzottFej".
     */
    @Override
    public String getNev() { 
        return "ZuzottFej"; 
    }

    /**
     * Visszaadja a működéshez szükséges zúzalékigényt.
     * A Hokotro.takarit() ezt az értéket használja a készlet ellenőrzésére.
     * @return A szükséges mennyiség.
     */
    public int getZuzalekIgeny() { 
        return zuzalekIgeny; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában (Factory minta).
     * A Bolt ezen keresztül biztosítja a másolatokat a Takarító eszköztárába.
     * @return Egy új ZuzottFej objektum a jelenlegi adatokkal.
     */
    @Override
    public KotroFej getKotroFej() { 
        return new ZuzottFej(getAr(), zuzalekIgeny); 
    }
}