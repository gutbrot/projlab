package terkep;

import java.util.List;
import skeleton.Skeleton;

/**
 * A SimaUt osztály az Ut egy speciális típusa.
 * Feladata a normál útszakaszok reprezentálása, ahol a havazás 
 * közvetlenül befolyásolja az út állapotát.
 */
public class SimaUt extends Ut {
    
    /**
     * Konstruktor a SimaUt példányosításához.
     * 
     * @param nev Az út neve.
     * @param hossz Az út hossza.
     * @param savokSzama A párhuzamos sávok száma szakaszonként.
     */
    public SimaUt(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Megvalósítja a havazás logikáját a sima úton
     * Végigiterál az út összes szakaszán és sávján, majd megnöveli 
     * a hóvastagságot a paraméterben kapott értékkel
     * 
     * @param h A hulló hó mennyisége.
     */
    @Override
    public void havazik(int h) {
        Skeleton.functionCalled("havazik", this, "void", h);
        
        // Mivel az Ut osztályban a 'szakaszok' protected, itt közvetlenül elérjük
        for (List<Sav> szakasz : szakaszok) {
            for (Sav sav : szakasz) {
                // Lekérdezzük a sáv aktuális hóvastagságát és hozzáadjuk az újat
                int jelenlegiHo = sav.getHo();
                sav.setHo(jelenlegiHo + h);
            }
        }
        
        System.out.println("    [SimaUt] " + nev + ": A havazás megtörtént (+ " + h + " cm minden sávban).");
        
        Skeleton.voidReturn();
    }
}