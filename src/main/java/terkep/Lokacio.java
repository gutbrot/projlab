package terkep;

import java.util.List;

/**
 * A Lokacio osztály összeköti a járművet a térkép egy konkrét pontjával.
 * Tárolja az utat, a szakaszt (keresztmetszetet) és a konkrét sávot.
 */
public class Lokacio {
    /** Az út, amin a jármű tartózkodik. */
    private Ut ut;
    /** A sáv, amiben a jármű éppen áll. */
    private Sav sav;

    /**
     * Konstruktor a lokáció létrehozásához.
     * @param ut Az út objektum.
     * @param sav A konkrét sáv az úton.
     */
    public Lokacio(Ut ut, Sav sav) {
        this.ut = ut;
        this.sav = sav;
    }

    /**
     * Visszaadja a jelenlegi szakasz összes sávját. 
     * Ez segít a szomszédos sávok (balra/jobbra) megtalálásában koordináták nélkül.
     */
    public List<Sav> getSzakasz() {
        if (ut == null || sav == null) return null;
        
        // Megkeressük, melyik szakaszban van a sávunk
        for (List<Sav> szakasz : ut.getSzakaszok()) {
            if (szakasz.contains(sav)) {
                return szakasz;
            }
        }
        return null;
    }

    // --- Getterek és a hiányzó Setter ---

    public Ut getUt() { return ut; }
    
    public void setUt(Ut ut) { this.ut = ut; }

    public Sav getSav() { return sav; }

    /**
     * Ez a metódus hiányzott! Frissíti a konkrét sávot a lokáción belül.
     * @param sav Az új sáv, amibe a jármű átlépett.
     */
    public void setSav(Sav sav) { 
        this.sav = sav; 
    }
}