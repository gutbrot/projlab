package jarmu;

import terkep.*;

/**
 * A Jarmu egy absztrakt osztály, amely a játékban közlekedő járműveket reprezentálja.
 */
public abstract class Jarmu {
    
    protected Lokacio pozicio;
    protected int mozgaskeptelenKorokSzama = 0;
    
    protected Jarmu(Lokacio pozicio) {
        this.pozicio = pozicio;
    }
    
    public boolean mozgas(Sav ujSav) {
        // 1. Paraméter nélküli hívás lekezelése (Elérhető sávok listázása)
        if (ujSav == null) {
            System.out.println(">>> Elérhető sávok a mozgáshoz:");
            if (pozicio != null && pozicio.getUt() != null) {
                // A prototípus szintjén itt megjelenítjük az elérhető sávok ID-ját.
                // (Ezt később finomíthatjátok, ha a Lokacio visszaadja a pontos szomszédokat)
                System.out.println("    - Sáv ID: 0");
                System.out.println("    - Sáv ID: 1");                                                      //Ez itt még csak place holder, majd ha lesz pálya, akkor meg kell írni, hogyan tudja megnézni az elérhető Sávoakt
                System.out.println("    - (A mozgáshoz használd: mozgas <SavID>)");
            } else {
                System.out.println("    [HIBA] A járműnek nincs érvényes pozíciója a térképen.");
            }
            // A listázás önmagában egy sikeresen végrehajtott parancs
            return true; 
        }

        // 2. Tényleges mozgás logikája (ha megadtak cél sávot)
        if (mozgaskeptelenKorokSzama > 0) {
            System.out.println("    >>> [KUDARC] A jármű mozgásképtelen még " + mozgaskeptelenKorokSzama + " körig.");
            return false;
        }

        if (!ujSav.atjarhatoE(this)) {
            // Ha a sáv foglalt, ütközés történik
            if (ujSav.isVanEJarmu()) {
                this.utkozos();
            }
            return false;
        }

        // Csúszáskezelés
        this.csuszasKezeles(ujSav, null); 

        // Pozíció frissítése a térképen
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        ujSav.setVanEJarmu(true);
        ujSav.novelAthaladok();
        
        if (pozicio != null) {
            pozicio.setSav(ujSav);
        }
        
        System.out.println("    >>> [JÁRMŰ AKCIÓ] Sikeresen átmozgott a(z) " + ujSav.getSavSzama() + ". sávra.");

        return true;
    }

    public void csuszasKezeles(Sav s, Terkep t) {
        if (s.jegesE() && !s.isZuzalekos() && !(this instanceof Hokotro)) {
            System.out.println("    >>> [VESZÉLY] A jármű megcsúszott a jégen!");
        }
    }

    public abstract void utkozos();

    public void mozgasKeptelen() {
        if (!(this instanceof Hokotro)) {
            this.mozgaskeptelenKorokSzama = 3;
            System.out.println("    >>> [INFO] A jármű 3 körre mozgásképtelenné vált.");
        }
    }

    public Lokacio getPozicio() { return pozicio; }
    public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }
    public int getMozgaskeptelenKorokSzama() { return mozgaskeptelenKorokSzama; }
    
    public void ujKor() {
        if (mozgaskeptelenKorokSzama > 0) {
            mozgaskeptelenKorokSzama--;
        }
    }
}