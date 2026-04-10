package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jarmu.*;

/**
 * Absztrakt út osztály, amely a térkép úthálózatának alapeleme.
 * Felelőssége az út gráf-szerkezetének (szakaszok és sávok) kezelése.
 */
public abstract class Ut {
    /** Az út egyedi neve */
    protected String nev;
    /** Az út hossza. */
    protected int hossz;
    /** * A gráf topológiája: 
     * Külső lista: hosszirányú szakaszok.
     * Belső lista: a szakasz keresztmetszetében lévő párhuzamos sávok.
     */
    protected List<List<Sav>> szakaszok = new ArrayList<>();

    /**
     * Konstruktor az út struktúrájának automatikus generálásához.
     */
    protected Ut(String nev, int hossz, int savokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz); //
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < Math.max(1, savokSzama); j++) {
                // Minden sáv kap egy sorszámot a keresztmetszeten belül
                szakasz.add(new Sav(j)); //
            }
            szakaszok.add(szakasz); //
        }
    }

    /**
     * Ellenőrzi, hogy a megadott sávba behajthat-e a jármű.
     */
    public boolean jarmuEligazito(Jarmu jarmu, Sav sav) {
        // Meghívja a sáv belső logikáját
        return sav != null && sav.atjarhatoE(jarmu);
    }

    /**
     * Absztrakt metódus az időjárási szimulációhoz.
     * A leszármazottak (SimaUt, Hid, Alagut) határozzák meg, 
     * hogy az adott szakaszon nő-e a hóvastagság.
     */
    public abstract void havazik(int h); //

    // --- Lekérdező metódusok ---

    /** @return Az út neve. */
    public String getNev() { return nev; }
    
    /** @return Az út hossza. */
    public int getHossz() { return hossz; }
    
    /** @return Az út teljes topológiája (szakaszok és sávok). */
    public List<List<Sav>> getSzakaszok() { return Collections.unmodifiableList(szakaszok); }
    
    /**
     * Visszaad egy konkrét csomópontot (sávot).
     * @param szakaszIndex Hosszanti pozíció (0-tól hossz-1-ig).
     * @param savIndex Keresztirányú pozíció (melyik sáv).
     */
    public Sav getSav(int szakaszIndex, int savIndex) {
        try {
            return szakaszok.get(szakaszIndex).get(savIndex); //
        } catch (IndexOutOfBoundsException e) {
            return null; // Ha a gráf szélére érnénk
        }
    }
}