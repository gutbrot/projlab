package terkep;

import jarmu.Jarmu;

/**
 * A térkép egy forgalmi sávját reprezentáló osztály.
 * Felelőssége az aktuális útviszonyok (hóvastagság, jegesedés, sómennyiség) tárolása 
 * és a sáv foglaltságának kezelése.
 * Ez az egység határozza meg, hogy egy adott jármű áthaladhat-e az adott útszakaszon.
 */
public class Sav {
    /** A sávon található hó aktuális vastagsága milliméterben. */
    private int hoVastagsag;
    /** Jelzi, hogy tartózkodik-e éppen jármű ebben a sávban (biztonsági követés). */
    private boolean vanEJarmu;
    /** A sáv azonosító száma az adott útszakaszon belül. */
    private final int savSzama;
    /** Statisztikai adat: hány jármű haladt át ezen a sávon a játék során. */
    private int athaladokSzama;
    /** A sávra kijuttatott só mennyisége, amely segíti az olvadást. */
    private int soMennyiseg;
    /** Jelzi, hogy a sáv felülete le van-e fagyva (jeges-e). */
    private boolean jegesE;

    /**
     * Konstruktor egy sáv példányosításához.
     * @param savSzama A sáv sorszáma az út keresztmetszetében.
     */
    public Sav(int savSzama) {
        this.savSzama = savSzama;
    }

    /**
     * Meghatározza, hogy a sáv az aktuális állapotában járható-e egy jármű számára.
     * Szabályok: Nem lehet benne másik jármű, a hóvastagság nem haladhat meg egy kritikus szintet (30), 
     * és figyelembe veszi a jegesedést is a jármű típusától függően.
     * @param jarmu A jármű, amely át szeretne haladni.
     * @return True, ha a sáv szabad és járható, egyébként false.
     */
    public boolean atjarhatoE(Jarmu jarmu) {
        // A sáv akkor járható, ha nincs benne más, a hó < 30 mm és nem akadályozza jég a járművet.
        return !vanEJarmu && hoVastagsag < 30 && (!jegesE || jarmu != null);
    }
    
    /**
     * A kijuttatott só hatására bekövetkező olvadást szimulálja.
     * Ha van só a sávon, csökkenti a hóvastagságot és a sómennyiséget is.
     */
    public void soOlvadas() {
        if (soMennyiseg > 0) {
            // A só 10 egységgel csökkenti a hóvastagságot, de az nem mehet 0 alá.
            hoVastagsag = Math.max(0, hoVastagsag - 10);
            soMennyiseg--;
        }
    }

    /** @param h A hozzáadandó hó mennyisége. */
    public void setHo(int h){ hoVastagsag += h; }
    
    /** @return True, ha a sáv jeges. */
    public boolean jegesE() { return jegesE; }
    
    /** @param jeges A sáv jegesedési állapotának beállítása. */
    public void setJeges(boolean jeges) { this.jegesE = jeges; }
    
    /** @return Az aktuális hóvastagság. */
    public int getHo() { return hoVastagsag; }
    
    /** @return True, ha van jármű a sávban. */
    public boolean isVanEJarmu() { return vanEJarmu; }
    
    /** @param vanEJarmu A sáv foglaltságának beállítása. */
    public void setVanEJarmu(boolean vanEJarmu) { this.vanEJarmu = vanEJarmu; }
    
    /** @return A sáv sorszáma. */
    public int getSavSzama() { return savSzama; }
    
    /** @return Az áthaladt járművek száma. */
    public int getAthaladokSzama() { return athaladokSzama; }
    
    /** Növeli az áthaladások számát, amikor egy jármű sikeresen belép a sávba. */
    public void novelAthaladok() { athaladokSzama++; }
    
    /** @return A sávon lévő sómennyiség. */
    public int getSoMennyiseg() { return soMennyiseg; }
}