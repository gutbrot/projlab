package jarmu;

import terkep.Lokacio;

/**
 * Az osztály felelőssége egy NPC jármű reprezentálása a szimulációban. 
 */
public class Auto extends Jarmu {

    private final String id;
    private final Lokacio[] vegallomasok = new Lokacio[2];
    private int celVegallomasIndex = 1; // 0 = A végállomás, 1 = B végállomás

    //Konstruktor, amely inicializálja az autó azonosítóját, a két végállomást és a kezdő pozíciót.
    public Auto(String id, Lokacio v1, Lokacio v2, Lokacio kezdo) {
        super(kezdo);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    /**
     * Az autó ütközését figyelő metódus, amely kiírja a baleset tényét és az autó azonosítóját, 
     * majd mozgásképtelenné teszi a járművet.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> [BALESET] Ütközés történt: Az " + id + " azonosítójú autó balesetet szenvedett!");
        mozgasKeptelen();
    }

    /**
     * Ellenőrzi, hogy az autó elérte-e valamelyik végállomást. Ha igen, akkor a jármű mozgásképtelenné válik.
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

    public int getCelVegallomasIndex() {
        return celVegallomasIndex;
    }

    public void toggleCelVegallomasIndex() {
        celVegallomasIndex = (celVegallomasIndex == 0) ? 1 : 0;
    }
}