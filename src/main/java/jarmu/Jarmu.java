package jarmu;

import terkep.*;
import skeleton.Skeleton;

/**
 * A Jarmu egy absztrakt osztály, amely a játékban közlekedő járműveket reprezentálja
 * Felelős a térképen való mozgás és pozicionálás logikájának kezeléséért
 */
public abstract class Jarmu {
    
    /** Tárolja a járművek aktuális pozícióját a térképen */
    protected Lokacio pozicio;
    
    /** Azt tárolja, hogy a jármű hány körig nem mozoghat */
    protected int mozgaskeptelenKorokSzama = 0;
    
    /**
     * Konstruktor a jármű példányosításához.
     * @param pozicio A jármű kezdőpozíciója.
     */
    protected Jarmu(Lokacio pozicio) {
        this.pozicio = pozicio;
    }
    
    /**
     * Kezeli a jármű mozgását.
     * Ellenőrzi a járhatóságot, lekezeli a csúszást és az esetleges ütközést.
     * 
     * @param ujSav A sáv, amibe a jármű lépni szeretne.
     * @return True, ha a mozgás sikeres volt.
     */
    public boolean mozgas(Sav ujSav) {
        Skeleton.functionCalled("mozgas", this, "boolean", ujSav);

        // 1. Mozgásképesség ellenőrzése
        if (mozgaskeptelenKorokSzama > 0) {
            System.out.println(">>> A jármű mozgásképtelen még " + mozgaskeptelenKorokSzama + " körig.");
            return Skeleton.functionReturn(false);
        }

        if (ujSav == null) return Skeleton.functionReturn(false);

        // 2. Járhatóság ellenőrzése (hóvastagság/foglaltság)
        if (!ujSav.atjarhatoE(this)) {
            // Ha nem átjárható, de jármű van ott, az ütközést vált ki
            if (ujSav.isVanEJarmu()) {
                this.utkozos();
            }
            return Skeleton.functionReturn(false);
        }

        // 3. Csúszáskezelés meghívása a dokumentáció szerint
        // A Terkep objektumot a lokációból érjük el
        this.csuszasKezeles(ujSav, null); 

        // 4. Pozíció frissítése a térképen
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        ujSav.setVanEJarmu(true);
        ujSav.novelAthaladok();
        
        if (pozicio != null) {
            pozicio.setSav(ujSav);
        }

        return Skeleton.functionReturn(true);
    }

    /**
     * Kezeli a jármű viselkedését, amikor szélsőséges útviszonyok közé kerül
     * Meghatározza az irányítás elvesztését (csúszás balra)
     * 
     * @param s A vizsgált sáv.
     * @param t A térkép a szomszédos sávok lekérdezéséhez.
     */
    public void csuszasKezeles(Sav s, Terkep t) {
        Skeleton.functionCalled("csuszasKezeles", this, "void", s, t);
        
        // Dokumentáció: Jeges az út?[cite: 1] (Hókotró immunis)
        if (s.jegesE() && !s.isZuzalekos() && !(this instanceof Hokotro)) {
            System.out.println(">>> A jármű megcsúszik a jégen!");
            // Itt valósulna meg a balra sodródás logikája a sávindexek alapján
        }
        
        Skeleton.voidReturn();
    }

    /** 
     * Absztrakt metódus, amely kényszeríti a leszármazottakat az 
     * ütközési események egyedi lekezelésére
     */
    public abstract void utkozos();

    /**
     * Lekezeli a járművek esetében a mozgásképtelenség állapotát
     * A dokumentáció állaporgépe alapján balesetkor 3 körre állítjuk
     */
    public void mozgasKeptelen() {
        Skeleton.functionCalled("mozgasKeptelen", this, "void");
        
        // Hókotró immunis a mozgásképtelenségre a leírás szerint
        if (!(this instanceof Hokotro)) {
            this.mozgaskeptelenKorokSzama = 3;
        }
        
        Skeleton.voidReturn();
    }

    // --- Getterek és Setterek ---
    public Lokacio getPozicio() { return pozicio; }
    public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }
    public int getMozgaskeptelenKorokSzama() { return mozgaskeptelenKorokSzama; }
    
    /** A kör végén hívódik meg a büntetés csökkentésére. */
    public void ujKor() {
        if (mozgaskeptelenKorokSzama > 0) {
            mozgaskeptelenKorokSzama--;
        }
    }
}