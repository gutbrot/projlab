package terkep;

import java.util.List;

/**
 * A Lokacio osztály felelős egy jármű pontos helyzetének meghatározásáért a térképen.
 * Egyfajta koordinátaként szolgál, amely összefogja az úthálózat különböző szintjeit: 
 * megadja, hogy a jármű melyik úton, annak melyik keresztmetszeti szakaszán 
 * és pontosan melyik forgalmi sávjában tartózkodik.
 */
public class Lokacio {
    /** Az út objektum, amelyen a tartózkodási hely található. */
    private final Ut ut;
    /** Az út egy adott keresztmetszete (szakasz), amely több párhuzamos sávot tartalmaz. */
    private final List<Sav> szakasz;
    /** A konkrét forgalmi sáv, amelyben a jármű vagy objektum elhelyezkedik. */
    private final Sav sav;

    /**
     * Konstruktor egy új lokáció (helymeghatározás) létrehozásához.
     * @param ut A konkrét út (pl. utca vagy híd).
     * @param szakasz Az út hosszirányú felosztásának egy szelete.
     * @param sav A szakaszban található sávok egyike.
     */
    public Lokacio(Ut ut, List<Sav> szakasz, Sav sav) {
        this.ut = ut;
        this.szakasz = szakasz;
        this.sav = sav;
    }

    /** @return Visszaadja a lokációhoz tartozó utat. */
    public Ut getUt() { return ut; }
    
    /** @return Visszaadja az út adott keresztmetszetét alkotó sávok listáját. */
    public List<Sav> getSzakasz() { return szakasz; }
    
    /** @return Visszaadja a pontos forgalmi sávot. */
    public Sav getSav() { return sav; }
}