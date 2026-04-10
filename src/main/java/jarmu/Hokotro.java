package jarmu;

import jatekos.Takarito;
import kotrofej.KotroFej;
import terkep.*;
import java.util.List;

/**
 * A hókotró jármű, amely az úthálózat tisztításáért felelős.
 * A takarítás hatékonyságát a felszerelt KotroFej határozza meg.
 */
public class Hokotro extends Jarmu {
    /** A hókotró egyedi azonosítója (pl. "H1", "H2"). */
    private final String id;
    
    /** Eltárolja, hogy a hókotrón jelenleg milyen fej van felszerelve. */
    private KotroFej felszereltFej;
    
    /**
     * Konstruktor a Hokotro példányosításához.
     * @param id A hókotró egyedi azonosítója.
     * @param pozicio A jármű kezdeti pozíciója a térképen.
     * @param felszereltFej Az indításkor a járműre szerelt kotrófej.
     */
    public Hokotro(String id, Lokacio pozicio, KotroFej felszereltFej) {
        super(pozicio);
        this.id = id;
        this.felszereltFej = felszereltFej;
        System.out.println(">>> Hókotró létrehozva (ID: " + id + ") a(z) " + pozicio.getUt().getNev() + " úton.");
    }
    
    /**
     * A járművet a vásárlást végző Takarító játékoshoz rendeli.
     */
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.hozzaadHokotro(this);
            System.out.println(">>> A(z) " + id + " azonosítójú hókotró átadva a vevőnek.");
        }
    }

    /**
     * Lecseréli a jelenlegi kotrófejet egy újra.
     */
    public void fejcsere(KotroFej fej) {
        if (fej != null) {
            String regiFej = (felszereltFej != null) ? felszereltFej.getNev() : "nincs";
            this.felszereltFej = fej;
            System.out.println(">>> Hókotrón (" + id + ") eszközcsere történt: " + regiFej + " -> " + fej.getNev());
        }
    }
    
    /**
     * Végrehajtja a takarítást a felszerelt fej segítségével.
     * Meghatározza a célsávot és a mellette lévő sávot (hó áttolásához).
     */
    public void takarit(Terkep terkep) {
        if (pozicio == null || felszereltFej == null) {
            System.out.println(">>> Takarítás hiba: Hiányzó pozíció vagy eszköz a(z) " + id + " hókotrónál.");
            return;
        }

        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();
        Sav melle = null;

        // A szomszédos sáv meghatározása a gráfban (balra vagy jobbra tolás)
        List<Sav> szakasz = pozicio.getSzakasz();
        if (szakasz != null && szakasz.size() > 1) {
            int idx = szakasz.indexOf(cel);
            // Ha nem az utolsó sávban vagyunk, toljuk jobbra (idx+1), egyébként balra (idx-1)
            if (idx + 1 < szakasz.size()) {
                melle = szakasz.get(idx + 1);
            } else if (idx - 1 >= 0) {
                melle = szakasz.get(idx - 1);
            }
        }

        System.out.println(">>> A(z) " + id + " hókotró megkezdte a takarítást (" + felszereltFej.getNev() + ") a(z) " + ut.getNev() + " úton.");
        felszereltFej.tisztit(cel, melle, ut);
    }

    /**
     * Ütközéskezelés megvalósítása.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> Ütközés! A(z) " + id + " azonosítójú hókotró balesetet szenvedett.");
        mozgasKeptelen();
    }

    // --- Getterek ---

    public String getId() { return id; }
    
    public KotroFej getFelszereltFej() { return felszereltFej; }
    
    public Hokotro getHokotro() { return this; }
}