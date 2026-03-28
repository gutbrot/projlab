package terkep;

import jarmu.Jarmu;

/**
 * A térkép egy forgalmi sávját reprezentáló osztály.
 * Tárolja az aktuális útviszonyokat, mint a hóvastagság és a jegesedés.
 */
public class Sav {
    // A diagram szerinti belső állapotok
    private int hoVastagsag; // A sávban lévő hó vastagsága cm-ben
    private boolean vanEJarmu; // Van-e jelenleg jármű a sávban
    private int savSzama; // A sáv azonosítója
    private int athaladokSzama; // Hány jármű haladt át a sávon
    private int soMennyiseg; //Mennyi só van a sávban, ami segíti a hó olvadását
    private boolean jegese; // Jeges-e a sáv

    /**
     * Eldönti, hogy a sáv az adott jármű számára járható-e.
     * @param j A jármű, amely át szeretne haladni.
     * @return true, ha a jármű képes áthaladni a sávon.
     */
    public boolean atjarhatoE(Jarmu j) {
        // Szkeleton logika: ha nincs rajta roncs és a hó kezelhető
        return !vanEJarmu && hoVastagsag < 50;
    }

    /**
     * Kezeli a só hatására történő hóolvadást a sávban.
     */
    public void soOlvadas() {
        if (soMennyiseg > 0) {
            hoVastagsag = Math.max(0, hoVastagsag - 10);
            soMennyiseg--;
        }
    }

    /**
     * Beállítja vagy módosítja a hóvastagságot a sávban.
     * @param h A hozzáadandó hó mennyisége.
     */
    public void setHo(int h) {
        this.hoVastagsag = h;
    }

    /**
     * Lekérdezi, hogy a sáv felülete jeges-e.
     * @return true, ha az út le van fagyva.
     */
    public boolean jegeseE() {
        return jegese;
    }

    /**
     * Visszaadja az aktuális hóvastagságot.
     * @return A hó vastagsága mm-ben vagy cm-ben.
     */
    public int getHo() {
        return hoVastagsag;
    }

}