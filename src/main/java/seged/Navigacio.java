package seged;

import java.util.ArrayList;
import java.util.List;
import terkep.*;

/**
 * A Navigacio osztály felelős az útvonaltervezésért a gráf alapú térképen.
 * Segít az NPC autóknak és a buszoknak eldönteni, melyik a következő sáv/szakasz.
 */
public class Navigacio {
    /** A kiindulási hely, ahonnan az útvonalat tervezzük. */
    private Lokacio honnan;

    /**
     * Konstruktor a navigációhoz.
     * @param honnan A jármű aktuális pozíciója.
     */
    public Navigacio(Lokacio honnan) {
        this.honnan = honnan;
    }

    /**
     * Kiszámítja az utat a célállomásig.
     * Prototípus szinten: Megkeresi a gráfban a következő logikai lépést.
     * @param hova A célállomás lokációja.
     * @return Lokációk listája, amely az utat reprezentálja.
     */
    public List<Lokacio> legrovidebbUt(Lokacio hova) {
        List<Lokacio> utvonal = new ArrayList<>();
        
        if (honnan == null || hova == null) {
            System.out.println(">>> Navigáció hiba: Érvénytelen indulási vagy érkezési pont.");
            return utvonal;
        }

        System.out.println(">>> Útvonaltervezés folyamatban: " + 
                           honnan.getUt().getNev() + " -> " + hova.getUt().getNev());

        // --- EGYSZERŰSÍTETT GRÁF-BEJÁRÁS A PROTOTÍPUSHOZ ---
        // A valóságban itt egy BFS vagy Dijkstra algoritmus futna a Terkep gráfján.
        // Most szimuláljuk azzal, hogy ha nem ugyanazon az úton vagyunk, 
        // betesszük a célállomást az útba.
        
        if (honnan.getUt() != hova.getUt()) {
            // Itt a gráf élei mentén kellene haladni (Ut1 -> Ut2 -> ...)
            utvonal.add(hova); 
        } else {
            // Ha ugyanazon az úton vagyunk, csak sávot vagy szakaszt kell váltani
            utvonal.add(hova);
        }

        System.out.println(">>> Navigáció: Útvonal sikeresen kiszámítva.");
        return utvonal;
    }

    /**
     * Frissíti az indulási pontot (például minden lépés után).
     */
    public void frissitHonnan(Lokacio ujHely) {
        this.honnan = ujHely;
    }
}