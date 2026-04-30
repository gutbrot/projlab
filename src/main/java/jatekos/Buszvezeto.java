package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jarmu.Busz;
import skeleton.Skeleton;

/**
 * A Buszvezeto osztály felelős a rendszerben lévő buszok irányításáért 
 * Feladata a rendelkezésre álló buszok közül a legjobb kiválasztása, 
 * azok mozgatása, valamint a forduló teljesítése után a pontok gyűjtése 
 * Mozgásokat az akciópontok felhasználásával végez
 */
public class Buszvezeto extends Jatekos {
    
    /** A rendelkezésre álló buszok, amiket a buszvezető irányítani tud */
    private final List<Busz> iranyithatoBuszok = new ArrayList<>();
    
    /** Az adott fordulók száma a játékban, amiért a játékos pontot kap */
    private int pont = 0;
    
    /** Scanner a konzolos beolvasáshoz a manuális választáshoz. */
    private Scanner scanner = new Scanner(System.in);
    
    /**
     * Konstruktor a Buszvezető létrehozásához.
     * @param akcioPont A játékos induló akciópontjainak száma.
     */
    public Buszvezeto(int akcioPont) {
        super(akcioPont);
    }

    /** Alapértelmezett konstruktor 3 akcióponttal. */
    public Buszvezeto() {
        this(3); 
    }
    
    /**
     * A buszvezető kiválasztja a buszt, amivel közlekedni szeretne
     * A dokumentáció aktivitásdiagramja alapján valósítja meg a választási logikát
     * 
     * @return A kiválasztott Busz objektum
     */
    public Busz busztValaszt() {
        Skeleton.functionCalled("busztValaszt", this, "Busz");
        
        // Ha a lista üres, NULL-al térünk vissza
        if (iranyithatoBuszok.isEmpty()) {
            return Skeleton.functionReturn(null);
        }

        // Kiválasztási logika (felhasználói input a prototípushoz)
        System.out.println(">>> Irányítható buszok:");
        for (int i = 0; i < iranyithatoBuszok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyithatoBuszok.get(i).getId());
        }

        System.out.print("? Válasszon indexet: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < iranyithatoBuszok.size()) {
                Busz kivalasztott = iranyithatoBuszok.get(index);
                return Skeleton.functionReturn(kivalasztott);
            }
        } catch (Exception e) {
            System.out.println(">>> Érvénytelen választás.");
        }
        
        return Skeleton.functionReturn(null);
    }

    /**
     * A buszvezető mozgatja a kiválasztott buszt az úton 
     * Ez a művelet akciópont levonással jár
     * 
     * @param busz A mozgatni kívánt busz.
     */
    public void busztMozgat(Busz busz) {
        Skeleton.functionCalled("busztMozgat", this, "void", busz);

        // 1. Aktuális akciópontok lekérdezése
        // 2. Van akciópontja?
        if (getAkcioPont() > 0 && busz != null) {
            
            // 3. A paraméterben kapott 'busz' mozgatása
            // Megjegyzés: A tényleges cél sávot a tesztkörnyezet/irányítás adja át a busznak
            // Itt a busz belső mozgas() logikáját hívnánk meg egy szomszédos sávra.
            boolean siker = busz.mozgas(null); // A sávot a hívó határozza meg

            // 4. A busz mozgása sikeres volt?
            if (siker) {
                // 5. Igen: Akciópont csökkentése 1-el
                akcioPontKezelo();
            } else {
                // 6. Nem: Mozgás sikertelen (Az akciópontot nem vonjuk le)
                System.out.println(">>> A busz mozgása sikertelen volt, AP nem került levonásra.");
            }
        }
        
        Skeleton.voidReturn();
    }

    /**
     * A játékosok pontjainak növekedéséért felelős
     * Ez a metódus akkor hívódik meg, ha a busz sikeresen eléri a végállomását
     */
    public void pontotKap() {
        Skeleton.functionCalled("pontotKap", this, "void");
        this.pont++;
        Skeleton.voidReturn();
    }
    
    /** Új busz hozzáadása a játékoshoz. */
    public void hozzaadBusz(Busz busz) {
        if (busz != null && !iranyithatoBuszok.contains(busz)) {
            iranyithatoBuszok.add(busz);
        }
    }

    // --- GETTEREK ---
    public int getPont() { return pont; }
    public List<Busz> getIranyithatoBuszok() { return iranyithatoBuszok; }
}