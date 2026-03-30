package jatekos;

import java.util.ArrayList;
import jarmu.Busz;
import java.util.List;

/**
 * A Buszvezeto osztály felelős a rendszerben lévő buszok irányításáért.
 * Feladata a rendelkezésre álló buszok közül a választás, azok mozgatása, 
 * valamint a forduló teljesítése után a pontok gyűjtése.
 * A buszvezető célja, hogy minél több fordulót teljesítsen a járművekkel.
 */
public class Buszvezeto extends Jatekos {
    /** A rendelkezésre álló buszok listája, amiket a buszvezető az adott körében irányítani tud. */
    private final List<Busz> iranyithatoBuszok = new ArrayList<>();
    
    /** Az adott fordulók száma a játékban, amiért a játékos pontot kap. */
    private int pont;
    
    /**
     * Konstruktor a Buszvezető létrehozásához.
     * A játék elején 3 akciópontot kap, amit a saját körének elején használhat fel.
     * @param akcioPont A játékos induló akciópontjainak száma.
     */
    public Buszvezeto(int akcioPont) {
        super(akcioPont);
    }
    
    /**
     * Új busz hozzáadása a játékos által irányítható járművek köréhez.
     * @param busz A hozzáadandó Busz példány.
     */
    public void hozzaadBusz(Busz busz) {
        if (busz != null) iranyithatoBuszok.add(busz);
    }

    /**
     * A buszvezető kiválasztja azt a buszt, amivel közlekedni szeretne.
     * @return A kiválasztott busz objektuma, vagy null, ha nincs irányítható busz.
     */
    public Busz buszValaszt() {
        return iranyithatoBuszok.isEmpty() ? null : iranyithatoBuszok.get(0);
    }

    /**
     * A buszvezető mozgatja a kiválasztott buszt az úton.
     * A mozgatás az akciópontok felhasználásával történik.
     * @param busz A mozgatni kívánt busz.
     * @return True, ha a mozgatás kezdeményezése sikeres (van elég akciópont), egyébként false.
     */
    public boolean buszMozgat(Busz busz) {
        // Ellenőrizzük, hogy a busz az irányítása alatt áll-e és van-e elég akciópontja
        if (busz == null || !iranyithatoBuszok.contains(busz) || akcioPont <= 0) return false;
        
        // Minden cselekvésnél (sikeres mozgatás indításánál) levon a játékostól 1 pontot.
        akcioPont--;
        return true;
    }

    /**
     * A játékos pontjainak növeléséért felelős metódus.
     * Akkor hívódik meg, ha a busz sikeresen eléri a végállomását.
     */
    public void pontotKap() {
        pont++;
    }
    
    /** @return A játékos által eddig összegyűjtött pontok (fordulók) száma. */
    public int getPont() { return pont; }
    
    /** @return Az irányítható buszok aktuális listája. */
    public List<Busz> getIranyithatoBuszok() { return iranyithatoBuszok; }
}