package seged;

import java.util.ArrayList;
import java.util.List;

import terkep.Lokacio;

/**
 * A Navigacio osztály felelős a legrövidebb útkereső algoritmus futtatásáért.
 * Feladata, hogy a járművek közlekedése érdekében meghatározza a két pont közötti 
 * legrövidebb utat a gráfként ábrázolt úthálózaton.
 * A járművek (például az Autó) mozgás közben ezen keresztül kérdezik le a következő optimális lépést.
 */
public class Navigacio {
    
    /**
     * Kiszámítja a jármű jelenlegi helyzete és a célpont közötti optimális útvonalat.
     * Meghatározza a következő cellát vagy útvonalat, amely a célállomás eléréséhez szükséges.
     * * @param hova A célállomás lokációja, ahová a jármű el szeretne jutni.
     * @return A Lokációk listája, amely a kiszámított útvonalat reprezentálja.
     */
    public List<Lokacio> legrovidebbUt(Lokacio hova) {
        List<Lokacio> eredmeny = new ArrayList<>();
        
        // Ha a célállomás érvényes, hozzáadja az útvonalhoz (egyszerűsített implementáció)
        if (hova != null) {
            eredmeny.add(hova);
        }
        
        // Visszatér az útvonal adataival
        return eredmeny;
    }
}