package jarmu;

import terkep.Lokacio;

/**
 * Egy NPC járművet reprezentál, ami az utakon közlekedik a saját körének ideje alatt
 * Két végpont között a legrövidebb úton mozgó jármű fajta, amit nem lehet irányítani
 * Elsődleges feladata a jármű állapotának tárolása, a célállomásainak ismerete, valamint az ütközések kezelése
 */
public class Auto extends Jarmu{
    /** * Lokacio típusú 2 hosszú tömb, ami az autó indulási és érkezési helyét tárolja el fix módon
     * Ezek a végállomások határozzák meg a mozgásának irányát
     */
    private final Lokacio[] vegallomasok = new Lokacio[2];

    /**
     * Konstruktor az Auto példányosításához.
     * @param elso Az egyik végállomás lokációja
     * @param masodik a másik végállomás lokációja
     * @param kezdo Az autó indulási helye
     */
    public Auto(Lokacio elso, Lokacio masodik, Lokacio kezdo) {
        super(kezdo);
        vegallomasok[0] = elso;
        vegallomasok[1] = masodik;
    }

    /**
     * Ez a függvény vizsgálja, hogy az autó ütközött-e
     * Reagál a más járművekkel vagy objektumokkal való interakciókra
     * Ütközés esetén a járművet mozgásképtelen állapotba helyezi
     */
    @Override
    public void utkozos() {
        mozgasKeptelen();
    }

    /**
     * Visszaadja az autó fix végállomásait.
     * @return A végállomásokat tartalmazó tömb másolata.
     */
    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}