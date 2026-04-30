package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.Jarmu;
import skeleton.Skeleton;

/**
 * Az Ut osztály felelős az útszakaszok alapvető logikai felépítésének 
 * és tulajdonságainak tárolásáért. 
 * Absztrakt osztályként keretet ad a leszármazottaknak a környezeti 
 * hatások lekezelésére. Feladata a forgalom irányítása a sávok között.
 */
public abstract class Ut {
    
    /** Az út megnevezése */
    protected String nev;
    
    /** Az út hossza. */
    protected int hossz;
    
    /** 
     * A sávokat tárolja. 
     * Az út több egymást követő szakaszból áll, ahol minden szakasz 
     * több párhuzamos sávot tartalmazhat.
     */
    protected List<List<Sav>> szakaszok = new ArrayList<>();

    /**
     * Konstruktor az út alapvető felépítéséhez.
     * 
     * @param nev Az út neve.
     * @param hossz Az út hossza.
     * @param savokSzama Párhuzamos sávok száma szakaszonként.
     */
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

    /**
     * Egy irányító funkció, amely a járműveket a megfelelő sávokhoz rendeli hozzá[
     * A dokumentáció aktivitásdiagramja alapján (13. oldal)
     * 
     * @param j A mozgatni kívánt jármű.
     * @param s A célsáv, ahová a jármű tart.
     * @return True, ha a sávváltás vagy haladás sikeres volt
     */
    public boolean jarmuEligazito(Jarmu j, Sav s) {
        Skeleton.functionCalled("jarmuEligazito", this, "boolean", j, s);

        // 1. Cél sáv létezik az úton?
        if (s == null) {
            System.out.println("    [Ut] Út vége vagy érvénytelen irány.");
            return Skeleton.functionReturn(false);
        }

        // 2. Járható? (Sav.atjarhatoE hívása)
        if (s.atjarhatoE(j)) {
            // 3. Jármű áthelyezése a sávba
            // (A tényleges pozíciófrissítést a Jarmu.mozgas() fejezi be)
            System.out.println("    [Ut] Sikeres sávváltás vagy haladás.");
            return Skeleton.functionReturn(true);
        } else {
            // 4. Akadály észlelése (Hó vagy másik jármű)
            System.out.println("    [Ut] Akadály észlelése. A jármű megáll vagy ütközik.");
            return Skeleton.functionReturn(false);
        }
    }

    /**
     * Absztrakt metódus, amelyet minden konkrét úttípusnak 
     * sajátosan kell megvalósítania
     * 
     * @param h A hulló hó mennyisége.
     */
    public abstract void havazik(int h); 

    // --- LEKÉRDEZŐK ---

    public String getNev() { 
        return nev; 
    }
    
    public int getHossz() { 
        return hossz; 
    }
    
    /** Visszaadja az út teljes szakasz- és sávszerkezetét */
    public List<List<Sav>> getSzakaszok() { 
        return Collections.unmodifiableList(szakaszok); 
    }
}