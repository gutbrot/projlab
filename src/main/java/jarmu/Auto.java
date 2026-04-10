package jarmu;

import terkep.Lokacio;

/**
 * Egy NPC járművet reprezentál, amely automatikusan közlekedik a végpontjai között.
 * Nem irányítható közvetlenül, a szimuláció mozgatja a legrövidebb úton.
 */
public class Auto extends Jarmu {
    /** Az autó egyedi azonosítója (pl. "A1", "A2"). */
    private final String id;

    /** Az autó indulási és érkezési helyét tároló tömb. */
    private final Lokacio[] vegallomasok = new Lokacio[2];

    /**
     * Konstruktor az Auto példányosításához.
     * @param id Az autó egyedi azonosítója.
     * @param elso Az egyik végállomás lokációja.
     * @param masodik A másik végállomás lokációja.
     * @param kezdo Az autó indulási helye.
     */
    public Auto(String id, Lokacio elso, Lokacio masodik, Lokacio kezdo) {
        super(kezdo);
        this.id = id;
        this.vegallomasok[0] = elso;
        this.vegallomasok[1] = masodik;
        System.out.println(">>> Autó létrehozva (ID: " + id + ") a(z) " + kezdo.getUt().getNev() + " úton.");
    }

    /**
     * Megvalósítja az ütközéskezelést.
     * Ha az autó ütközik, mozgásképtelen állapotba kerül.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> Ütközés! A(z) " + id + " azonosítójú autó balesetet szenvedett.");
        mozgasKeptelen();
    }

    /**
     * Megvizsgálja, hogy az autó valamelyik végállomásánál tartózkodik-e.
      * @return True, ha az autó aktuális pozíciója valamelyik végállomás.
     */
    public boolean vegallomasraErt() {
        return pozicio == vegallomasok[0] || pozicio == vegallomasok[1];
    }

    /** @return Az autó egyedi azonosítója. */
    public String getId() { return id; }

    /** @return A végállomásokat tartalmazó tömb másolata. */
    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}