package seged;

import terkep.Lokacio;
import skeleton.Skeleton;

/**
 * A Navigacio osztály felelős a legrövidebb útkereső algoritmus futtatásáért.
 * Feladata, hogy a járművek közlekedése érdekében meghatározza a két pont 
 * közötti legrövidebb utat a gráfként ábrázolt úthálózaton.
 */
public class Navigacio {

    /**
     * Az osztálynak a dokumentáció alapján nincsenek saját attribútumai.
     */

    /**
     * A metódus egy célállomást vár paraméterül 
     * Kiszámítja a jármű jelenlegi helyzete és a célpont közötti optimális útvonalat, 
     * majd (a leírás szerint) szolgáltatja az útvonal adatait.
     * 
     * @param hova A célállomás lokációja.
     */
    public void legrovidebbUt(Lokacio hova) {
        Skeleton.functionCalled("legrovidebbUt", this, "void", hova);

        // A dokumentáció aktivitásdiagramja alapján (16. oldal):
        // 1. Aktuális pozíció lekérése (a hívó járműtől)
        // 2. Célállomás meghatározása (paraméter: hova)
        // 3. Legrövidebb útvonal kiszámítása
        
        System.out.println(">>> Navigáció: Útvonal tervezése a célállomás felé...");
        
        // A szimuláció ezen pontján a kiszámított útvonal alapján 
        // a jármű megkapja a következő lépéshez szükséges adatokat.

        Skeleton.voidReturn();
    }
}