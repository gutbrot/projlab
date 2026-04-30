package terkep;

import java.util.List;
import skeleton.Skeleton;

/**
 * A Hid osztály az Ut egy speciális típusa.
 * Feladata a hidakon lévő útszakaszok reprezentálása, ahol a havazás 
 * közvetlenül befolyásolja az út állapotát.
 */
public class Hid extends Ut {
    
    /**
     * Konstruktor a Híd példányosításához.
     * 
     * @param nev A híd neve.
     * @param hossz A híd hossza.
     * @param savokSzama A párhuzamos sávok száma.
     */
    public Hid(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Megvalósítja a havazás logikáját a hídon
     * Végigiterál az út összes szakaszán és sávján, majd megnöveli 
     * a hóvastagságot a paraméterben kapott értékkel
     * 
     * @param h A hulló hó mennyisége.
     */
    @Override
    public void havazik(int h) {
        Skeleton.functionCalled("havazik", this, "void", h);
        
        // Végigmegyünk az összes szakaszon és sávon (az Ut osztály szakaszok listáját használva)
        for (List<Sav> szakasz : szakaszok) {
            for (Sav sav : szakasz) {
                // Lekérdezzük az aktuális havat és hozzáadjuk az újat
                int jelenlegiHo = sav.getHo();
                sav.setHo(jelenlegiHo + h);
            }
        }
        
        System.out.println("    [Hid] " + nev + ": A havazás minden sávban megtörtént (+ " + h + " cm).");
        
        Skeleton.voidReturn();
    }
}