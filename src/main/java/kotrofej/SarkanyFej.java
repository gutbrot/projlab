package kotrofej;

import terkep.*;

/**
 * A SarkanyFej osztály felelős a takarítási folyamat egy speciális, nagy hatásfokú típusának megvalósításáért.
 * Hókotróra szerelhető prémium kotrófej, amely hatalmas hőleadásával képes a havat és a jeget is 
 * pillanatok alatt felolvasztani.
 * 
 * Működéséhez egyedi fogyóanyag, biokerozin szükséges. A felelősségek szétválasztása (B opció) értelmében 
 * az anyag rendelkezésre állását és levonását a jármű (Hokotro) saját maga ellenőrzi, a fej csak 
 * az igényét (biokerozinIgeny) közli a rendszerrel.
 */
public class SarkanyFej extends KotroFej {
    
    /** 
     * Megadja az egyetlen takarítási művelethez (egy sáv letisztításához) szükséges biokerozin mennyiségét. 
     * Ezt az értéket a rendszer a Takarító eszköztárából vonja le.
     */
    private int biokerozinIgeny;

    /**
     * Konstruktor a SarkanyFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható (átadódik az ősosztálynak).
     * @param biokerozinIgeny A működéshez szükséges üzemanyag mennyisége cselekvésenként.
     */
    public SarkanyFej(int ar, int biokerozinIgeny) {
        super(ar);
        // Biztosítjuk, hogy az igény ne lehessen negatív
        this.biokerozinIgeny = Math.max(0, biokerozinIgeny);
    }

    /**
     * Megvalósítja a sárkányfej tisztító metódusát.
     * Mivel a Hokotro osztály már ellenőrizte és levonta a biokerozint, mire ez a metódus meghívódik,
     * itt már garantált, hogy a fej működésbe léphet.
     * 
     * @param cel A sáv, amelyen a jégpáncél és a hó felolvasztása történik.
     * @param melle A mellette lévő sáv (a sárkányfej esetében nincs jelentősége, nem tol át havat).
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés a váratlan hibák elkerülésére
        if (cel != null) {
            System.out.println(">>> Sárkányfej bekapcsolva! Extrém hőhatás a(z) " + cel.getSavSzama() + ". sávban...");
            
            // A sárkányfej képes egyszerre leolvasztani a havat és a jégpáncélt is az úttestről.
            boolean voltHo = cel.getHo() > 0;
            boolean voltJeg = cel.jegesE();
            
            // Fizikai állapotok módosítása: hó eltüntetése (0-ra állítás) és jég felolvasztása
            cel.setHo(0);
            cel.setJeges(false);
            
            // Részletes visszajelzés a prototípus tesztelőjének a leolvasztott elemekről
            if (voltHo || voltJeg) {
                System.out.println(">>> SIKER: A Sárkányfej felolvasztotta a " + (voltHo ? "havat " : "") + (voltHo && voltJeg ? "és a " : "") + (voltJeg ? "jeget " : "") + "a sávban.");
            } else {
                System.out.println(">>> A(z) " + cel.getSavSzama() + ". sáv eleve tiszta volt, a Sárkányfej üresen égetett el " + biokerozinIgeny + " egység biokerozint.");
            }
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "SarkanyFej".
     */
    @Override
    public String getNev() { 
        return "SarkanyFej"; 
    }

    /**
     * Visszaadja a működéshez szükséges biokerozin igényt.
     * A Hokotro.takarit() metódusa ezt az értéket kéri le, mielőtt engedélyezné a tisztit() hívását.
     * 
     * @return A szükséges üzemanyag mennyisége.
     */
    public int getBiokerozinIgeny() { 
        return biokerozinIgeny; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában (Factory minta).
     * Vásárláskor a Bolt ez alapján gyárt egy új példányt a játékos eszköztárába.
     * 
     * @return Egy új SarkanyFej objektum a jelenlegi árral és üzemanyag-igénnyel.
     */
    @Override
    public KotroFej getKotroFej() { 
        return new SarkanyFej(getAr(), biokerozinIgeny); 
    }
}