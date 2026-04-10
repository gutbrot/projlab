package terkep;

/**
 * Az Alagut osztály az Ut egy speciális típusa.
 * Speciális időjárási szabályok vonatkoznak rá: a hóvastagság sosem nő benne.
 */
public class Alagut extends Ut {
    
    /**
     * Konstruktor az Alagut példányosításához.
     * @param nev Az alagút egyedi megnevezése.
     * @param hossz Az alagút hossza (szakaszok száma).
     * @param savokSzama Az alagútban futó párhuzamos sávok száma.
     */
    public Alagut(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Felüldefiniálja a havazás logikáját.
     * Mivel az alagút fedett, a metódus üres marad, így a sávok hóvastagsága 
     * az időjárás-frissítés során változatlan marad.
     */
    @Override
    public void havazik(int h) {
        // Logolás a prototípushoz, hogy lássuk a tesztelés során:
         System.out.println(">>> Alagut (" + nev + "): A fedett kialakitas miatt nem hullik ho az utra.");
    }
}