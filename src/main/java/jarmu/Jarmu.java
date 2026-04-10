package jarmu;

import terkep.*;

/**
 * Absztrakt alaposztály a játékban szereplő összes jármű számára.
 */
public abstract class Jarmu {
    /** A jármű aktuális helyzete. */
    protected Lokacio pozicio;
    /** Megadja, hány körig mozgásképtelen még a jármű. */
    protected int mozgaskepetlenKorokSzama = 0;
    
    protected Jarmu(Lokacio pozicio) {
        this.pozicio = pozicio;
    }
    
    /**
     * A jármű mozgását megvalósító metódus.
     * Frissíti a sávok állapotát és a jármű saját pozícióját.
     */
    public boolean mozgas(Sav ujSav) {
        // 1. Mozgásképesség ellenőrzése
        if (mozgaskepetlenKorokSzama > 0) {
            System.out.println(">>> Mozgás sikertelen: A jármű még " + mozgaskepetlenKorokSzama + " körig mozgásképtelen.");
            mozgaskepetlenKorokSzama--;
            return false;
        }

        // 2. Célsáv ellenőrzése
        if (ujSav == null) {
            System.out.println(">>> Mozgás sikertelen: Nincs célsáv megadva.");
            return false;
        }

        if (!ujSav.atjarhatoE(this)) {
            System.out.println(">>> Mozgás sikertelen: A sáv (" + ujSav.getSavSzama() + ") nem átjárható (hó vagy más jármű miatt).");
            return false;
        }

        // 3. Régi pozíció felszabadítása
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        // 4. Új pozíció elfoglalása
        ujSav.setVanEJarmu(true);
        ujSav.novelAthaladok();

        // 5. A jármű saját pozíció-objektumának frissítése
        // Itt feltételezzük, hogy a Lokacio objektumot frissítjük az új sávval
        if (pozicio != null) {
            pozicio.setSav(ujSav);
            // Ha a mozgás során szakaszt is váltunk, azt a Navigáció/Hívó fogja kezelni a setPozicio-val
        }

        System.out.println(">>> Sikeres mozgás a(z) " + ujSav.getSavSzama() + ". sávba.");
        return true;
    }

    /** Absztrakt metódus az ütközésekhez. */
    public abstract void utkozos();

    /**
     * A járművet mozgásképtelen állapotba helyezi.
     */
    public void mozgasKeptelen() {
        mozgaskepetlenKorokSzama++;
        System.out.println(">>> A jármű mozgásképtelenné vált. Hátralévő körök: " + mozgaskepetlenKorokSzama);
    }

    // --- Getterek és Setterek ---

    public Lokacio getPozicio() { return pozicio; }
    
    public void setPozicio(Lokacio pozicio) { 
        this.pozicio = pozicio; 
    }

    public int getMozgaskepetlenKorokSzama() {
        return mozgaskepetlenKorokSzama;
    }
}