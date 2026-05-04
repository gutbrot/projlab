package terkep;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 */
public class Alagut extends Ut {
    
    //KONSTRUKTOR
    public Alagut(String nev, int hossz, int pozSavokSzama, int negSavokSzama) { 
        super(nev, hossz, pozSavokSzama, negSavokSzama); 
    }

    /**
     * Az Alagut osztályban a havazás hatása eltérő, mivel a fedél megvédi az utat a hóval való borítástól.
     * Ezért a havazás hatására az út tiszta marad, és nincs szükség hóeltakarításra.
     */
    @Override
    public void havazik(int h) {
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Alagút): A fedél felfogta a havat, az út tiszta maradt.");
    }
}