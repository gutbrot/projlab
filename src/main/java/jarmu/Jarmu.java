package jarmu;

import java.util.ArrayList;
import java.util.List;

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
        // --- 1. Elérhető sávok dinamikus kigyűjtése a lokáció alapján ---
        List<Sav> elerhetoSavok = new ArrayList<>();
        List<Sav> jelenlegiSzakasz = null; // Kimentjük a kiíráshoz
        
        if (pozicio != null && pozicio.getUt() != null && pozicio.getSzakasz() != null) {
            Ut ut = pozicio.getUt();
            jelenlegiSzakasz = pozicio.getSzakasz();
            int szakaszIndex = ut.getSzakaszok().indexOf(jelenlegiSzakasz);
            
            // a) Sávváltás (ugyanabban a szakaszban lévő többi sáv)
            for (Sav s : jelenlegiSzakasz) {
                if (s != pozicio.getSav()) {
                    elerhetoSavok.add(s);
                }
            }
            
            // b) Előrehaladás (a következő szakasz sávjai az aktuális úton)
            if (szakaszIndex >= 0 && szakaszIndex + 1 < ut.getSzakaszok().size()) {
                elerhetoSavok.addAll(ut.getSzakaszok().get(szakaszIndex + 1));
            } 
            // c) Továbbhaladás másik útra (ha az aktuális út legutolsó szakaszán vagyunk)
            else if (szakaszIndex != -1 && szakaszIndex + 1 == ut.getSzakaszok().size()) {
                for (Ut kovUt : ut.getSzomszedok(1)) {
                    if (!kovUt.getSzakaszok().isEmpty()) {
                        elerhetoSavok.addAll(kovUt.getSzakaszok().get(0));
                    }
                }
            }
        }

        // --- 2. Paraméter nélküli hívás lekezelése (Elérhető sávok listázása) ---
        if (ujSav == null) {
            System.out.println(">>> Elérhető irányok a mozgáshoz:");
            if (elerhetoSavok.isEmpty()) {
                System.out.println("    [HIBA] Nincs elérhető sáv, vagy a járműnek nincs érvényes pozíciója a térképen.");
            } else {
                for (int i = 0; i < elerhetoSavok.size(); i++) {
                    Sav s = elerhetoSavok.get(i);
                    // Csendes ellenőrzés
                    boolean jarhato = (s.getHo() < 30 && !s.isVanEJarmu());
                    
                    // Helyzet meghatározása a játékos számára
                    String irany = (jelenlegiSzakasz != null && jelenlegiSzakasz.contains(s)) 
                                    ? "Sávváltás oldalra" 
                                    : "Előrehaladás";
                    
                    System.out.println("    [" + i + "] Sáv ID: " + s.getSavSzama() + " (" + irany + ") - Járható: " + (jarhato ? "Igen" : "Nem"));
                }
                System.out.println("    - (A mozgáshoz használd az opció számát, pl: mozgas 0)");
            }
            return true; 
        }

        // --- 3. Tényleges mozgás logikája és validációja ---
        
        // Mivel a Jatekter egy "new Sav(ID)"-t küld be nekünk, a kapott "ID"-t most INDEXKÉNT kezeljük!
        int valasztottIndex = ujSav.getSavSzama();
        
        // Ellenőrizzük, hogy létezik-e ilyen sorszám a listában
        if (valasztottIndex < 0 || valasztottIndex >= elerhetoSavok.size()) {
            System.out.println("    >>> [KUDARC] Érvénytelen opció! Kérlek a listából válassz (0-" + (elerhetoSavok.size() - 1) + ").");
            return false;
        }

        // Kiválasztjuk a tényleges sávot az index alapján
        Sav valodiCelSav = elerhetoSavok.get(valasztottIndex);

        if (mozgaskeptelenKorokSzama > 0) {
            System.out.println("    >>> [KUDARC] A jármű mozgásképtelen még " + mozgaskeptelenKorokSzama + " körig.");
            return false;
        }

        // Átjárhatóság vizsgálata a VALÓDI sávon (itt már kiírja a hibát, ha van)
        if (!valodiCelSav.atjarhatoE(this)) {
            if (valodiCelSav.isVanEJarmu()) {
                this.utkozos();
            }
            return false;
        }

        // Csúszáskezelés
        this.csuszasKezeles(valodiCelSav, null); 

        // Régi sáv elhagyása a térképen
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        // Új sáv elfoglalása
        valodiCelSav.setVanEJarmu(true);
        valodiCelSav.novelAthaladok();
        
        // --- LOKÁCIÓ FRISSÍTÉSE ---
        if (pozicio != null) {
            Ut aktUt = pozicio.getUt();
            boolean megtalaltuk = false;
            
            for (List<Sav> szakasz : aktUt.getSzakaszok()) {
                if (szakasz.contains(valodiCelSav)) {
                    this.pozicio = new Lokacio(aktUt, szakasz, valodiCelSav);
                    megtalaltuk = true;
                    break;
                }
            }
            
            if (!megtalaltuk) {
                for (Ut kovUt : aktUt.getSzomszedok(1)) {
                    if (!kovUt.getSzakaszok().isEmpty() && kovUt.getSzakaszok().get(0).contains(valodiCelSav)) {
                        this.pozicio = new Lokacio(kovUt, kovUt.getSzakaszok().get(0), valodiCelSav);
                        break;
                    }
                }
            }
        }
        
        System.out.println("    >>> [JÁRMŰ AKCIÓ] Sikeresen átmozgott a(z) " + valodiCelSav.getSavSzama() + ". sávra.");

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