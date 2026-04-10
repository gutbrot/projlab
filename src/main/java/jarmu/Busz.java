package jarmu;

import terkep.Lokacio;

/**
 * A Busz osztály egy játékos által irányított járművet reprezentál.
 * Kezeli a végállomások közötti közlekedést és az egyedi azonosítást.
 */
public class Busz extends Jarmu {
    /** A busz egyedi azonosítója (pl. "B1", "B2"). */
    private final String id;

    /** Lokacio típusú 2 hosszú tömb, amely a busz két végállomását tárolja. */
    private final Lokacio[] vegallomasok = new Lokacio[2];

    /**
     * Konstruktor a Busz példányosításához.
     * @param id A busz egyedi azonosítója.
     * @param elso Az egyik kijelölt végállomás lokációja.
     * @param masodik A másik kijelölt végállomás lokációja.
     * @param kezdo A busz indulási pozíciója a játéktérben.
     */
    public Busz(String id, Lokacio elso, Lokacio masodik, Lokacio kezdo) {
        super(kezdo);
        this.id = id;
        vegallomasok[0] = elso;
        vegallomasok[1] = masodik;
        System.out.println(">>> Busz létrehozva (ID: " + id + ") a(z) " + kezdo.getUt().getNev() + " úton.");
    }

    /**
     * Vizsgálja, hogy a busz elérte-e valamelyik végállomását.
     * @return True, ha a busz aktuális pozíciója végállomás.
     */
    public boolean vegallomasbaErt() {
        boolean cellert = (pozicio == vegallomasok[0] || pozicio == vegallomasok[1]);
        if (cellert) {
            System.out.println(">>> A(z) " + id + " azonosítójú busz elérte az egyik végállomását!");
        }
        return cellert;
    }

    /**
     * Megvalósítja az ütközéskezelést.
     * Ütközés esetén a jármű mozgásképtelenné válik.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> Ütközés! A(z) " + id + " azonosítójú busz balesetet szenvedett és mozgásképtelenné vált.");
        mozgasKeptelen();
    }

    /** @return A busz egyedi azonosítója. */
    public String getId() { return id; }

    /** @return A végállomásokat tartalmazó tömb másolata. */
    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}