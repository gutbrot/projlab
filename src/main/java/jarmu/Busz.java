package jarmu;

import terkep.Lokacio;
import terkep.Sav;
import skeleton.Skeleton;
import jatekos.Buszvezeto;

/**
 * A Busz osztály egy járművet reprezentál
 * Elsődleges felelőssége a kijelölt megállóhelyek közötti közlekedés 
 * Képes detektálni a célállomás elérését, és kezelni az ütközéseket 
 * Ezeket a járműveket a buszvezető játékosok irányítják
 */
public class Busz extends Jarmu {
    
    /** A busz egyedi azonosítója a tesztkörnyezetben. */
    private final String id;

    /** A busz két végpontját (végállomását) tárolja[cite: 1]. */
    private final Lokacio[] vegallomasok = new Lokacio[2];
    
    /** Referencia a buszt irányító vezetőre a pontok jóváírásához. */
    private Buszvezeto vezeto;

    /**
     * Konstruktor a Busz példányosításához.
     * @param id Egyedi azonosító.
     * @param pozicio Kezdőpozíció.
     * @param v1 Első végállomás.
     * @param v2 Második végállomás.
     */
    public Busz(String id, Lokacio pozicio, Lokacio v1, Lokacio v2) {
        super(pozicio);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    /**
     * Felelőssége annak ellenőrzése, hogy a busz elérte-e a célját
     * A dokumentáció aktivitásdiagramja alapján: pozíció lekérése -> ellenőrzés -> 
     * vezető megkeresése -> pont jóváírása
     * 
     * @return Igazzal tér vissza, ha a busz megérkezett, hamissal, ha még úton van
     */
    public boolean vegallomasbaErt() {
        Skeleton.functionCalled("vegallomasbaErt", this, "boolean");

        // 1. Aktuális pozíció lekérdezése (Jarmu.pozicio)
        Lokacio aktualis = getPozicio();

        // 2. Elérte a végállomást?
        boolean match = false;
        for (Lokacio v : vegallomasok) {
            // Itt a sávok azonosságát vizsgáljuk a lokációkban
            if (v != null && aktualis != null && v.getSav() == aktualis.getSav()) {
                match = true;
                break;
            }
        }

        if (match) {
            // 3. Buszvezető megkeresése és pont jóváírása
            if (vezeto != null) {
                vezeto.pontotKap();
            }
            return Skeleton.functionReturn(true);
        }

        // 4. FALSE visszatérés, ha nincs a végállomáson
        return Skeleton.functionReturn(false);
    }

    /**
     * Felüldefiniálja az ősosztály metódusát[
     * Ütközés esetén a buszt mozgásképtelenné teszi
     */
    @Override
    public void utkozos() {
        Skeleton.functionCalled("utkozos", this, "void");
        
        // Ütközés esetén a busz balesetet szenved és büntetőköröket kap
        mozgasKeptelen();
        
        Skeleton.voidReturn();
    }

    // --- GETTEREK ÉS SETTEREK ---
    public String getId() { return id; }

    /** Beállítja a buszhoz tartozó vezetőt. */
    public void setVezeto(Buszvezeto vezeto) {
        this.vezeto = vezeto;
    }

    public Buszvezeto getVezeto() {
        return vezeto;
    }
}