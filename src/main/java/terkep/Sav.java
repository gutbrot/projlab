package terkep;

import jarmu.Jarmu;

/**
 * A térkép egy forgalmi sávját reprezentáló osztály.
 * Tárolja az útviszonyokat és kezeli a foglaltságot.
 */
public class Sav {
    private int hoVastagsag = 0;
    private boolean vanEJarmu = false;
    private int savSzama;
    private int athaladokSzama = 0;
    private int soMennyiseg = 0;
    private boolean jegesE = false;
    private boolean zuzalekosE = false;

    /**
     * Konstruktor a sáv példányosításához.
     * @param savSzama A sáv sorszáma a keresztmetszetben.
     */
    public Sav(int savSzama) {
        this.savSzama = savSzama;
    }

    /**
     * Meghatározza, hogy a sáv járható-e.
     * @param jarmu A belépni kívánó jármű.
     * @return True, ha szabad, nincs túl mély hó (<30mm) és nem akadályozza jég.
     */
    public boolean atjarhatoE(Jarmu jarmu) {
        // Alapszabály: nem lehet benne másik jármű és a hó nem lehet 30mm-nél több.
        boolean szabad = !vanEJarmu;
        boolean hoRendben = hoVastagsag < 30;
        
        // A jegesedés vagy zúzalékosság a jármű típusától és felszerelésétől függően akadályozhat.
        // Prototípus szinten: ha jeges, csak a hóláncos/speciális járművek haladnak jól.
        return szabad && hoRendben && (!jegesE || jarmu != null);
    }
    
    /**
     * A sózás hatására bekövetkező olvadás szimulációja.
     */
    public void soOlvadas() {
        if (soMennyiseg > 0) {
            // A só 10 egységgel csökkenti a havat, de nem megy 0 alá.
            hoVastagsag = Math.max(0, hoVastagsag - 10);
            soMennyiseg--;
        }
    }

    // --- Módosító és lekérdező metódusok ---

    public void setHo(int h) { this.hoVastagsag = Math.max(0, h); }
    public int getHo() { return hoVastagsag; }
    
    public boolean jegesE() { return jegesE; }
    public void setJeges(boolean jeges) { this.jegesE = jeges; }

    /** Beállítja a zúzalékos állapotot (pl. jégtörés után). */
    public void setZuzalekos(boolean zuzalekos) { this.zuzalekosE = zuzalekos; }
    public boolean isZuzalekos() { return zuzalekosE; }

    public boolean isVanEJarmu() { return vanEJarmu; }
    public void setVanEJarmu(boolean vanEJarmu) { this.vanEJarmu = vanEJarmu; }

    public int getSavSzama() { return savSzama; }
    
    /** Regisztrálja az áthaladást és növeli a statisztikát. */
    public void novelAthaladok() { athaladokSzama++; }
    public int getAthaladokSzama() { return athaladokSzama; }

    /** Só hozzáadása a sávhoz (pl. sószóró fej által). */
    public void hozzaadSo(int mennyiseg) { this.soMennyiseg += mennyiseg; }
    public int getSoMennyiseg() { return soMennyiseg; }
}