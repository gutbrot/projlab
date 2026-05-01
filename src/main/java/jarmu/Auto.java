package jarmu;

import terkep.Lokacio;

/**
 * Az osztály felelőssége egy NPC jármű reprezentálása a szimulációban. 
 */
public class Auto extends Jarmu {
    
    private final String id;
    private final Lokacio[] vegallomasok = new Lokacio[2];

    public Auto(String id, Lokacio v1, Lokacio v2, Lokacio kezdo) {
        super(kezdo);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    @Override
    public void utkozos() {
        System.out.println(">>> [BALESET] Ütközés történt: Az " + id + " azonosítójú autó balesetet szenvedett!");
        mozgasKeptelen();
    }

    public boolean vegallomasraErt() {
        if (pozicio == null) return false;
        return pozicio.getSav() == vegallomasok[0].getSav() || 
               pozicio.getSav() == vegallomasok[1].getSav();
    }

    public String getId() { 
        return id; 
    }

    public Lokacio[] getVegallomasok() { 
        return vegallomasok.clone(); 
    }
}