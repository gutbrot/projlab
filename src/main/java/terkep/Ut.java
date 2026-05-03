package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jarmu.Jarmu;

/**
 * Az Ut osztály felelős az útszakaszok alapvető logikai felépítésének tárolásáért.
 * Kezeli az útszakaszok (szakaszok és sávok) struktúráját és a szomszédos utakat.
 */
public abstract class Ut {
    
    protected String nev;
    protected int hossz;
    protected List<List<Sav>> szakaszok = new ArrayList<>();
    
    // ÚJ: Szomszédok tárolása irányok szerint: 1 (pozitív vég), -1 (negatív eleje)
    protected Map<Integer, List<Ut>> szomszedok = new HashMap<>();

    protected Ut(String nev, int hossz, int savokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz);
        
        // Sávok inicializálása
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < Math.max(1, savokSzama); j++) {
                szakasz.add(new Sav(j)); 
            }
            szakaszok.add(szakasz); 
        }

        // ÚJ: Szomszéd listák inicializálása az irányoknak megfelelően
        szomszedok.put(1, new ArrayList<>());  // Utolsó szakasz utáni utak
        szomszedok.put(-1, new ArrayList<>()); // Első szakasz előtti utak
    }

    /**
     * ÚJ: Szomszédos út hozzáadása egy adott irányhoz.
     * @param szomszed A csatlakozó út példánya.
     * @param irany 1, ha az út végéhez (hossz-1), -1, ha az elejéhez (0) csatlakozik.
     */
    public void addSzomszed(Ut szomszed, int irany) {
        if (szomszed != null && szomszedok.containsKey(irany)) {
            szomszedok.get(irany).add(szomszed);
        }
    }

    /**
     * ÚJ: Lekéri az adott irányban elérhető szomszédos utakat.
     * @param irany Az elágazás iránya (1 vagy -1).
     * @return A szomszédos utak listája.
     */
    public List<Ut> getSzomszedok(int irany) {
        return Collections.unmodifiableList(szomszedok.getOrDefault(irany, new ArrayList<>()));
    }

    public boolean jarmuEligazito(Jarmu j, Sav s) {
        if (s == null) {
            System.out.println("    >>> [NAVIGÁCIÓ HIBA] Út vége vagy érvénytelen irány.");
            return false;
        }

        if (s.atjarhatoE(j)) {
            System.out.println("    >>> [NAVIGÁCIÓ] Sikeres sávváltás vagy haladás előkészítve.");
            return true;
        } else {
            System.out.println("    >>> [NAVIGÁCIÓ] Akadály észlelve! A jármű nem tud továbbmenni.");
            return false;
        }
    }

    public abstract void havazik(int h); 

    public String getNev() { return nev; }
    public int getHossz() { return hossz; }
    
    public List<List<Sav>> getSzakaszok() { 
        return Collections.unmodifiableList(szakaszok); 
    }
}