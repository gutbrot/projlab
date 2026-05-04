package terkep;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 */
public class Alagut extends Ut {
    
    public Alagut(String nev, int hossz, int pozSavokSzama, int negSavokSzama) { 
        super(nev, hossz, pozSavokSzama, negSavokSzama); 
    }

    @Override
    public void havazik(int h) {
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Alagút): A fedél felfogta a havat, az út tiszta maradt.");
    }
}