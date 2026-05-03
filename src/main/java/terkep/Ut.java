package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.Jarmu;

/**
 * Az Ut osztály felelős az útszakaszok alapvető logikai felépítésének tárolásáért.
 */
public abstract class Ut {
    
    protected String nev;
    protected int hossz;
    protected List<List<Sav>> szakaszok = new ArrayList<>();

    protected Ut(String nev, int hossz, int savokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz);
        
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < Math.max(1, savokSzama); j++) {
                szakasz.add(new Sav(j)); 
            }
            szakaszok.add(szakasz); 
        }
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