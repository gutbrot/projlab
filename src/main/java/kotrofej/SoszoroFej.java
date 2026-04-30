package kotrofej;

import terkep.*;

/**
 * A SoszoroFej osztály felelős a takarítási folyamat egy típusának megvalósításáért.
 * Hókotróra szerelhető kotrófej, amely képes a havat és a jeget felolvasztani megfelelő 
 * fogyóanyag mennyiség (só) birtokában.
 * 
 * Működéséhez egyedi fogyóanyag, só szükséges. A felelősségek szétválasztása (B opció) 
 * értelmében a só meglétét és levonását a jármű (Hokotro) saját maga ellenőrzi, a fej csak 
 * az igényét közli a rendszerrel.
 */
public class SoszoroFej extends KotroFej {
    
    /** 
     * Megadja az egyetlen takarítási művelethez (egy sáv letisztításához) szükséges sómennyiséget.
     * Ezt az értéket a rendszer a Takarító eszköztárából vonja le. 
     */
    private int solgeny;

    /**
     * Konstruktor a SoszoroFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható (átadódik az ősosztálynak).
     * @param solgeny A tisztításhoz szükséges sómennyiség igénye.
     */
    public SoszoroFej(int ar, int solgeny) {
        super(ar);
        // Biztosítjuk, hogy a sóigény ne lehessen negatív
        this.solgeny = Math.max(0, solgeny);
    }

    /**
     * Megvalósítja a sószóró fej tisztító metódusát.
     * Mivel a Hokotro osztály már ellenőrizte és levonta a sót, mire ez a metódus meghívódik,
     * itt már garantált, hogy a fej kiszórhatja az anyagot.
     * 
     * @param cel A sáv, amelyen a sózás és az olvasztás történik.
     * @param melle A mellette lévő sáv (a sószóró esetében nincs jelentősége).
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés a null pointerek elkerülésére
        if (cel != null) {
            System.out.println(">>> Sószórófej bekapcsolva a(z) " + cel.getSavSzama() + ". sávban. Só kiszórása...");
            
            // A sószóró fej a sózó funkció meghívásával csökkenti a hóvastagságot a sávban.
            // A fizikai logikát (mennyi hó olvad el, felolvad-e a jég) a Sav osztály tokozottan kezeli.
            cel.soOlvadas();
            
            System.out.println(">>> SIKER: Sószórófej befejezte a munkát. Az olvadási folyamat megkezdődött a sávban.");
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "SoszoroFej".
     */
    @Override
    public String getNev() { 
        return "SoszoroFej"; 
    }

    /**
     * Visszaadja a működéshez szükséges sómennyiség igényt.
     * A Hokotro.takarit() metódusa ezt az értéket kéri le, mielőtt engedélyezné a tisztit() hívását.
     * @return A szükséges sómennyiség.
     */
    public int getSolgeny() { 
        return solgeny; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában (Factory minta).
     * Vásárláskor a Bolt ez alapján gyárt egy új példányt a játékos eszköztárába.
     * @return Egy új SoszoroFej objektum a jelenlegi árral és sóigénnyel.
     */
    @Override
    public KotroFej getKotroFej() { 
        return new SoszoroFej(getAr(), solgeny); 
    }
}