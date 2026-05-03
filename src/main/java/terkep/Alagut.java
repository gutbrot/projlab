package terkep;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 */
public class Alagut extends Ut {
    
    public Alagut(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    @Override
    public void havazik(int h) {
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Alagút): A fedél felfogta a havat, az út tiszta maradt.");
    }
}