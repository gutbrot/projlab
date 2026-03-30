package terkep;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 * Felelőssége egy olyan útszakasz reprezentálása, amely fedett, 
 * így a környezeti hatások (például a havazás) korlátozottan vagy egyáltalán nem érintik.
 * Az úthálózat részét képezi, de speciális időjárási szabályok vonatkoznak rá.
 */
public class Alagut extends Ut {
    
    /**
     * Konstruktor az Alagut példányosításához.
     * Meghívja az ősosztály konstruktorát az alapvető útadatok beállításához.
     * @param nev Az alagút egyedi megnevezése.
     * @param hossz Az alagút hossza (szakaszok száma).
     * @param savokSzama Az alagútban futó párhuzamos sávok száma.
     */
    public Alagut(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Felüldefiniálja a havazás logikáját.
     * Az alagút fedett jellege miatt a csapadék nem jut be az úttestre, 
     * így a hóvastagság nem növekszik az időjárás-frissítés során.
     * @param h A lehullott hó mennyisége (az alagút esetében figyelmen kívül hagyva).
     */
    @Override
    public void havazik(int h) {
        // Az alagútban a konstrukcióból adódóan nem havazik, 
        // így ez a metódus nem módosítja a sávok állapotát.
    }
}