package jarmu;

import terkep.Lokacio;
import skeleton.Skeleton;

/**
 * Az osztály felelőssége egy NPC jármű reprezentálása a szimulációban. 
 * A jármű feladata, hogy a térképen két előre meghatározott végállomás között közlekedjen, 
 * a legrövidebb utat használva ehhez. Reagál a más járművekkel 
 * vagy objektumokkal való interakciókra.
 */
public class Auto extends Jarmu {
    
    /** Az autó egyedi azonosítója a tesztekhez. */
    private final String id;

    /** Az autó két végállomását tárolja. */
    private final Lokacio[] vegallomasok = new Lokacio[2];

    /**
     * Konstruktor az Auto példányosításához.
     * 
     * @param id Az autó azonosítója.
     * @param v1 Első végállomás.
     * @param v2 Második végállomás.
     * @param kezdo Indulási pozíció.
     */
    public Auto(String id, Lokacio v1, Lokacio v2, Lokacio kezdo) {
        super(kezdo);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    /**
     * Akkor hívódik meg, amikor az autó haladás közben egy másik járművel 
     * vagy akadállyal találkozik az adott sávon. 
     * Célja, hogy regisztrálja a balesetet és az autót megfelelő időre 
     * mozgásképtelenné tegye.
     */
    @Override
    public void utkozos() {
        Skeleton.functionCalled("utkozos", this, "void");
        
        // A dokumentáció szerint regisztrálja a balesetet
        System.out.println(">>> Ütközés történt: Auto (" + id + ") balesetet szenvedett.");
        
        // Mozgásképtelenné teszi az autót (a Jarmu osztályban definiált 3 körre)
        mozgasKeptelen();
        
        Skeleton.voidReturn();
    }

    /**
     * Segédmetódus a végállomás elérésének ellenőrzéséhez.
     * 
     * @return True, ha az autó valamelyik végállomásán tartózkodik.
     */
    public boolean vegallomasraErt() {
        if (pozicio == null) return false;
        return pozicio.getSav() == vegallomasok[0].getSav() || 
               pozicio.getSav() == vegallomasok[1].getSav();
    }

    // --- GETTEREK ---

    public String getId() { 
        return id; 
    }

    public Lokacio[] getVegallomasok() { 
        return vegallomasok.clone(); 
    }
}