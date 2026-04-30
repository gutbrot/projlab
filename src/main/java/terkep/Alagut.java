package terkep;

import skeleton.Skeleton;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 * Feladata egy fedett útszakasz reprezentálása, amelynél a környezeti 
 * hatások (havazás) nem befolyásolják az út állapotát
 */
public class Alagut extends Ut {
    
    /**
     * Konstruktor az Alagút példányosításához.
     * 
     * @param nev Az alagút neve.
     * @param hossz Az alagút hossza.
     * @param savokSzama A párhuzamos sávok száma.
     */
    public Alagut(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Felüldefiniálja a havazás logikáját.
     * Mivel az alagút fedett, a metódus nem növeli a sávok hóvastagságát
     * 
     * @param h A hulló hó mennyisége (az alagútban 0 marad).
     */
    @Override
    public void havazik(int h) {
        Skeleton.functionCalled("havazik", this, "void", h);
        
        // A dokumentáció szerint itt nem történik hóvastagság növelés
        System.out.println("    [Alagut] " + nev + ": A fedél felfogta a havat.");
        
        Skeleton.voidReturn();
    }
}