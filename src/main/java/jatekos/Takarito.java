package jatekos;

import java.util.ArrayList;
import java.util.List;

import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;

/**
 * A hókotrókat irányító játékosokat reprezentáló osztály.
 * Céljuk a buszvezetők játékának megkönnyítése az úthálózat tisztán tartásával.
 * A játék elején akciópontokat kapnak, amelyeket a saját körükben használhatnak fel.
 */
public class Takarito extends Jatekos {
    /** A takarító által vezérelt hókotrók listája. */
    private List<Hokotro> iranyitottHokotrok = new ArrayList<>();
    
    /** A takarító játékos összes rendelkezésre álló felszerelését és alapanyagát tárolja. */
    private Eszkoztar eszkoztar = new Eszkoztar();
    
    /** A játékos összegyűjtött pénze, amelyből a boltban vásárolhat. */
    private int penz;
    
    /**
     * Konstruktor a Takarító játékos létrehozásához.
     * @param akcioPont A kör elején rendelkezésre álló akciópontok száma.
     * @param penz Az induló pénzösszeg.
     */
    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
    }
    
    /**
     * Egy konkrét hókotró kiválasztása a takarító játékos által a vezérelt járművek közül.
     * @return A kiválasztott hókotró példány.
     */
    public Hokotro hokotrotValaszt() {
        return iranyitottHokotrok.isEmpty() ? null : iranyitottHokotrok.get(0);
    }

    /**
     * A kiválasztott hókotró jármű mozgatásáért felelős logika.
     * A művelet akciópont levonással jár.
     * @param h A mozgatni kívánt hókotró.
     * @return True, ha a mozgatás kezdeményezése sikeres.
     */
    public boolean hokotroMozgat(Hokotro h) {
        if (h == null || !iranyitottHokotrok.contains(h) || akcioPont <= 0) return false;
        akcioPont--;
        return true;
    }

    /**
     * Az aktuális kotrófej lecserélése egy másik típusra az eszköztárból.
     * A játékos kiválasztja a rendelkezésre álló fejek közül a leghasznosabbat.
     * @param h A hókotró, amelyen a fejcserét elvégezzük.
     * @return True, ha a váltás sikeres volt.
     */
    public boolean kotrofejValt(Hokotro h) {
        if (h == null || akcioPont <= 0) return false;
        KotroFej uj = eszkoztar.kiveszFej();
        if (uj == null) return false;
        h.fejcsere(uj);
        akcioPont--;
        return true;
    }

    /**
     * Általános vásárlási művelet egy Bolt objektumon keresztül.
     * Lehetővé teszi új eszközök vagy fogyóanyagok beszerzését a pénz és akciópont felhasználásával.
     * @param bolt A Bolt objektum, ahol a vásárlás történik.
     * @param termekNev A megvásárolni kívánt cikk neve.
     * @return True, ha a tranzakció sikeresen lezajlott.
     */
    public boolean vasarol(Bolt bolt, String termekNev) {
        if (bolt == null || akcioPont <= 0) return false;
        boolean siker = bolt.vasarlas(this, termekNev);
        if (siker) akcioPont--;
        return siker;
    }

    /**
     * A metódus a játékos pénzét növeli megadott összeggel.
     * @param p A kapott pénzösszeg.
     */
    public void penztKap(int p){
        this.penz += p;
    }
    
    /**
     * Új hókotró hozzáadása a játékos által irányított járművek listájához.
     * @param h A hozzáadandó hókotró.
     */
    public void hozzaadHokotro(Hokotro h) {
        if (h != null && !iranyitottHokotrok.contains(h)) iranyitottHokotrok.add(h);
    }
    
    /** @return A takarító által vezérelt hókotrók listája. */
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }
    
    /** @return A játékoshoz tartozó eszköztár. */
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    
    /** @return A játékos aktuális egyenlege. */
    public int getPenz() { return penz; }

}