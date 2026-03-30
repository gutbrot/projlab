package jarmu;

import terkep.Lokacio;

/**
 * A Busz osztály egy játékos által irányított járművet reprezentál
 * Elsődleges felelőssége a kijelölt megállóhelyek (végállomások) közötti közlekedés
 * Célja, hogy minél többször megforduljon a két végállomása között, amivel pontokat gyűjt az irányítója számára
 */
public class Busz extends Jarmu {
    /**
     * Lokacio típusú 2 hosszú tömb, amely a busz két végállomását tárolja el fix módon
     */
    private final Lokacio[] vegallomasok = new Lokacio[2];

    /**
     * Konstruktor a Busz példányosításához.
     * @param elso Az egyik kijelölt végállomás lokációja
     * @param masodik A másik kijelölt végállomás lokációja
     * @param kezdo A busz indulási pozíciója a játéktérben.
     */
    public Busz(Lokacio elso, Lokacio masodik, Lokacio kezdo) {
        super(kezdo);
        vegallomasok[0] = elso;
        vegallomasok[1] = masodik;
    }

    /**
     * Ez a metódus vizsgálja, hogy a busz elérte-e a saját vonalán lévő valamelyik végállomást
     * Képes detektálni a célállomás elérését a pontszerzés érdekében
     * @return True, ha a busz aktuális pozíciója megegyezik valamelyik végállomással.
     */
    public boolean vegallomasbaErt() {
        return pozicio == vegallomasok[0] || pozicio == vegallomasok[1];
    }

    /**
     * Megvalósítja az ütközéskezelést a busz számára
     * Kezeli azokat a kritikus helyzeteket, amikor a jármű baleset miatt mozgásképtelenné válik
     */
    @Override
    public void utkozos() {
        mozgasKeptelen();
    }

    /**
     * Visszaadja a buszhoz rendelt rögzített végállomásokat.
     * @return A végállomásokat tartalmazó tömb másolata
     */
    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}