package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jarmu.*;

/**
 * Absztrakt út osztály, amely a térkép úthálózatának alapeleme.
 * Felelőssége az út szerkezetének (hossz, sávok száma) definiálása és tárolása.
 * Az utak szakaszokból állnak, minden szakasz pedig egy vagy több forgalmi sávot tartalmaz.
 * Ez az osztály biztosítja az alapréteget a speciális úttípusok (Híd, Alagút, Sima út) számára.
 */
public abstract class Ut {
    /** Az út egyedi azonosítására szolgáló név. */
    protected final String nev;
    /** Az út hossza, amely meghatározza, hány egymást követő szakaszból (keresztmetszetből) áll. */
    protected final int hossz;
    /** * Kétdimenziós lista, amely az út szerkezetét tárolja. 
     * A külső lista a szakaszokat, a belső lista az adott szakaszhoz tartozó sávokat tartalmazza. 
     */
    protected final List<List<Sav>> szakaszok = new ArrayList<>();

    /**
     * Konstruktor az út szerkezetének felépítéséhez.
     * Automatikusan legenerálja a megadott számú szakaszt és a hozzájuk tartozó sávokat.
     * @param nev Az út neve.
     * @param hossz Az út hossza szakaszokban mérve.
     * @param savokSzama Egy szakaszban található párhuzamos sávok száma.
     */
    protected Ut(String nev, int hossz, int savokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz);
        // Az út felépítése: szakaszok és sávok inicializálása
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < Math.max(1, savokSzama); j++) {
                szakasz.add(new Sav(j));
            }
            szakaszok.add(szakasz);
        }
    }

    /**
     * Segít a járműveknek eldönteni, hogy az adott sávba be tudnak-e hajtani.
     * Meghívja a sáv saját átjárhatóság-ellenőrző logikáját.
     * @param jarmu A sávba belépni kívánó jármű.
     * @param sav A célsáv objektum.
     * @return True, ha a sáv szabad és az útviszonyok megfelelők a jármű számára.
     */
    public boolean jarmuEligazito(Jarmu jarmu, Sav sav) {
        return sav != null && sav.atjarhatoE(jarmu);
    }

    /**
     * Absztrakt metódus az időjárási hatások kezelésére.
     * A konkrét leszármazottak (pl. Alagút vs. Híd) itt definiálják, 
     * hogyan rakódik le a hó az út felületén.
     * @param h A lehulló hó mennyisége.
     */
    public abstract void havazik(int h);

    /** @return Az út neve. */
    public String getNev() { return nev; }
    
    /** @return Az út hossza (szakaszok száma). */
    public int getHossz() { return hossz; }
    
    /** @return Az út teljes szerkezeti felépítése (szakaszok és sávok). */
    public List<List<Sav>> getSzakaszok() { return Collections.unmodifiableList(szakaszok); }
    
    /**
     * Visszaad egy konkrét sávot az út adott koordinátáján.
     * @param szakaszIndex A hosszanti pozíció
     * @param savIndex A sáv sorszáma a keresztszelvényben.
     * @return A kért sáv objektum.
     */
    public Sav getSav(int szakaszIndex, int savIndex) {
        return szakaszok.get(szakaszIndex).get(savIndex);
    }
}