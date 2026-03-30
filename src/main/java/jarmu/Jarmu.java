package jarmu;

import terkep.*;

/**
 * Absztrakt alaposztály a játékban szereplő összes jármű (Autó, Busz, Hokotró) számára.
 * Közös felelőssége a járművek térbeli helyzetének nyilvántartása, a mozgási logika 
 * alapjainak biztosítása és az útviszonyokhoz való alkalmazkodás kezelése.
 * Minden jármű tudja, hogy aktuálisan hol tartózkodik és képes reagálni az ütközésekre.
 */
public abstract class Jarmu {
    /** A jármű aktuális helyzete (út, szakasz, sáv) a játéktérben. */
    protected Lokacio pozicio;
    /** Megadja, hogy a jármű még hány körön keresztül nem képes elindulni (pl. baleset miatt). */
    protected int mozgaskepetlenKorokSzama;
    
    /**
     * Konstruktor a jármű alaphelyzetének beállításához.
     * @param pozicio A jármű kezdeti helye a térképen.
     */
    protected Jarmu(Lokacio pozicio) {
        this.pozicio = pozicio;
    }
    
    /**
     * A jármű mozgását megvalósító metódus egy adott forgalmi sávba.
     * Ellenőrzi a mozgásképességet, a célsáv átjárhatóságát, és frissíti a sávok foglaltsági állapotát.
     * Regisztrálja az áthaladást a statisztikák (pl. pontszerzés vagy kopás) számára.
     * @param sav A sáv, amelybe a jármű át szeretne lépni.
     * @return True, ha a mozgás sikeresen végrehajtódott, egyébként false.
     */
    public boolean mozgas(Sav sav) {
        // Ha a jármű mozgásképtelen állapotban van, csökkenti a hátralévő körök számát
        if (mozgaskepetlenKorokSzama > 0) {
            mozgaskepetlenKorokSzama--;
            return false;
        }
        // Ellenőrzés: a sáv létezik-e és a jármű számára az adott viszonyok között járható-e
        if (sav == null || !sav.atjarhatoE(this)) {
            return false;
        }
        // A régi pozíció felszabadítása
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }
        // Az új sáv lefoglalása és az áthaladók számának növelése
        sav.setVanEJarmu(true);
        sav.novelAthaladok();
        return true;
    }

    /**
     * Absztrakt metódus az ütközések kezelésére.
     * Minden konkrét járműtípusnak saját módon kell reagálnia (pl. roncs elhelyezése vagy megállás).
     */
    public abstract void utkozos();

    /**
     * A járművet mozgásképtelen állapotba helyezi.
     * Ezt hívják meg ütközéskor vagy olyan eseményeknél, amelyek megállítják a forgalmat.
     */
    public void mozgasKeptelen() {
        mozgaskepetlenKorokSzama++;
    }

    /** @return Visszaadja a jármű aktuális tartózkodási helyét. */
    public Lokacio getPozicio() { return pozicio; }
    
    /** * Beállítja a jármű új tartózkodási helyét.
     * @param pozicio Az új lokáció objektum.
     */
    public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }  
}