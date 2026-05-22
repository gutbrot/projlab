package jarmu;

import java.util.ArrayList;
import java.util.List;

import terkep.*;
import jatekos.Jatekter;

/**
 * A Jarmu egy absztrakt osztály, amely a játékban közlekedő járműveket reprezentálja.
 */
public abstract class Jarmu {
    
    protected Lokacio pozicio;
    protected int mozgaskeptelenKorokSzama = 0;
    
    protected Jarmu(Lokacio pozicio) {
        this.pozicio = pozicio;
    }
    
    /**
     * A mozgas metódus kezeli a jármű mozgását a térképen. Kétféle használata van:
     * 1. Paraméter nélküli hívás: Kiírja a játékosnak az elérhető sávokat, amelyekre léphet.
     * 2. Paraméteres hívás: Megpróbálja átmozgatni a járművet a megadott sávra, ha az érvényes és járható.
     */
    public boolean mozgas(Sav ujSav) {
        //Elérhető sávok dinamikus kigyűjtése a lokáció alapján
        List<Sav> elerhetoSavok = new ArrayList<>();
        List<Sav> jelenlegiSzakasz = null; //Kimentjük a kiíráshoz
        
        if (pozicio != null && pozicio.getUt() != null && pozicio.getSzakasz() != null) {
            Ut ut = pozicio.getUt();
            jelenlegiSzakasz = pozicio.getSzakasz();
            int szakaszIndex = ut.getSzakaszok().indexOf(jelenlegiSzakasz);
            
            //Sávváltás (ugyanabban a szakaszban lévő többi sáv)
            for (Sav s : jelenlegiSzakasz) {
                if (s != pozicio.getSav()) {
                    elerhetoSavok.add(s);
                }
            }
            
            //Előrehaladás (a következő szakasz sávjai az aktuális úton)
            if (szakaszIndex >= 0 && szakaszIndex + 1 < ut.getSzakaszok().size()) {
                elerhetoSavok.addAll(ut.getSzakaszok().get(szakaszIndex + 1));
            } 
            //Továbbhaladás másik útra (ha az aktuális út legutolsó szakaszán vagyunk)
            else if (szakaszIndex != -1 && szakaszIndex + 1 == ut.getSzakaszok().size()) {
                for (Ut kovUt : ut.getSzomszedok(1)) {
                    if (!kovUt.getSzakaszok().isEmpty()) {
                        elerhetoSavok.addAll(kovUt.getSzakaszok().get(0));
                    }
                }
            }
        }

        //Paraméter nélküli hívás lekezelése (Elérhető sávok listázása)
        if (ujSav == null) {
            System.out.println(">>> Elérhető irányok a mozgáshoz:");
            if (elerhetoSavok.isEmpty()) {
                System.out.println("    [HIBA] Nincs elérhető sáv, vagy a járműnek nincs érvényes pozíciója a térképen.");
            } else {
                //Dinamikus lista az elérhető sávokról, ahol a játékos láthatja, hogy melyik sáv járható és milyen irányba vezet
                for (int i = 0; i < elerhetoSavok.size(); i++) {
                    Sav s = elerhetoSavok.get(i);
                    //Csendes ellenőrzés
                    boolean jarhato = (s.getHo() < 30 && !s.isVanEJarmu());
                    
                    //Helyzet meghatározása a játékos számára
                    String irany = (jelenlegiSzakasz != null && jelenlegiSzakasz.contains(s)) 
                                    ? "Sávváltás oldalra" 
                                    : "Előrehaladás";
                    
                    System.out.println("    [" + i + "] Sáv ID: " + s.getSavSzama() + " (" + irany + ") - Járható: " + (jarhato ? "Igen" : "Nem"));
                }
                System.out.println("    - (A mozgáshoz használd az opció számát, pl: mozgas 0)");
            }
            return true; 
        }

        //Tényleges mozgás logikája és validációja

        int valasztottIndex = ujSav.getSavSzama();
        
        //Ellenőrizzük, hogy létezik-e ilyen sorszám a listában
        if (valasztottIndex < 0 || valasztottIndex >= elerhetoSavok.size()) {
            System.out.println("    >>> [KUDARC] Érvénytelen opció! Kérlek a listából válassz (0-" + (elerhetoSavok.size() - 1) + ").");
            return false;
        }

        //Kiválasztjuk a tényleges sávot az index alapján
        Sav valodiCelSav = elerhetoSavok.get(valasztottIndex);

        if (mozgaskeptelenKorokSzama > 0) {
            System.out.println("    >>> [KUDARC] A jármű mozgásképtelen még " + mozgaskeptelenKorokSzama + " körig.");
            return false;
        }

        //Átjárhatóság vizsgálata a VALÓDI sávon (itt már kiírja a hibát, ha van)
        if (!valodiCelSav.atjarhatoE(this)) {
            if (valodiCelSav.isVanEJarmu()) {
                this.utkozos();
            }
            return false;
        }

        //Csúszáskezelés
        this.csuszasKezeles(valodiCelSav, null); 

        //Régi sáv elhagyása a térképen
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        //Új sáv elfoglalása
        valodiCelSav.setVanEJarmu(true);
        valodiCelSav.novelAthaladok();
        
        //Lokáció frissítése a térkép alapján (megkeressük melyik úthoz tartozik a sáv)
        if (pozicio != null) {
            Ut aktUt = pozicio.getUt();
            boolean megtalaltuk = false;
            
            //Először megpróbáljuk megtalálni a sávot az aktuális úton
            for (List<Sav> szakasz : aktUt.getSzakaszok()) {
                if (szakasz.contains(valodiCelSav)) {
                    this.pozicio = new Lokacio(aktUt, szakasz, valodiCelSav);
                    megtalaltuk = true;
                    break;
                }
            }
            
            //Ha nem találjuk meg az aktuális úton, akkor megpróbáljuk a szomszédos utakon
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

    /**
     * Ez a metódus kezeli a jármű csúszását, ha jeges sávra lép. Csak akkor aktiválódik, ha a sáv jeges, nem zuzalékos, és a jármű nem Hokotrónak minősül.
     * Ha a feltételek teljesülnek, egy figyelmeztető üzenetet ír ki a játékosnak.
     */
    public void csuszasKezeles(Sav s, Terkep t) {
        //Csúszás csak akkor, ha a sáv jeges, nem zuzalékos, és a jármű nem Hókotró
        if (s.jegesE() && !s.isZuzalekos() && !(this instanceof Hokotro)) {
            System.out.println("    >>> [VESZÉLY] A jármű megcsúszott a jégen!");
        }
    }

    /**
     * Ez a metódus akkor hívódik meg, amikor a jármű egy másik járművel ütközik. 
     * A konkrét viselkedést a leszármazott osztályok határozzák meg.
     */
    public abstract void utkozos();

    /**
     * Ez a metódus akkor hívódik meg, amikor a jármű mozgásképtelenné válik (pl. ütközés miatt). 
     * Alapértelmezés szerint 3 körre mozgásképtelenné válik, kivéve, ha a jármű Hókotró, amely nem válik mozgásképtelenné.
     */
    public void mozgasKeptelen() {
        //Hókotró nem válik mozgásképtelenné, így csak akkor állítjuk be a köröket, ha nem Hókotró
        if (!(this instanceof Hokotro)) {
            this.mozgaskeptelenKorokSzama = 3;
            System.out.println("    >>> [INFO] A jármű 3 körre mozgásképtelenné vált.");
        }
    }

    /**
     * Visszaadja az aktuális pozícióból elérhető sávok listáját (sávváltás + előrehaladás).
     * Ugyanaz a logika, mint a mozgas(null), de GUI számára listát ad vissza.
     */
    public List<Sav> getElerhetoSavok() {
        List<Sav> elerhetoSavok = new ArrayList<>();
        if (pozicio == null || pozicio.getUt() == null || pozicio.getSzakasz() == null) {
            return elerhetoSavok;
        }
        Ut ut = pozicio.getUt();
        List<Sav> jelenlegiSzakasz = pozicio.getSzakasz();
        int szakaszIndex = ut.getSzakaszok().indexOf(jelenlegiSzakasz);

        for (Sav s : jelenlegiSzakasz) {
            if (s != pozicio.getSav()) elerhetoSavok.add(s);
        }
        if (szakaszIndex >= 0 && szakaszIndex + 1 < ut.getSzakaszok().size()) {
            elerhetoSavok.addAll(ut.getSzakaszok().get(szakaszIndex + 1));
        } else if (szakaszIndex != -1 && szakaszIndex + 1 == ut.getSzakaszok().size()) {
            for (Ut kovUt : ut.getSzomszedok(1)) {
                if (!kovUt.getSzakaszok().isEmpty()) {
                    elerhetoSavok.addAll(kovUt.getSzakaszok().get(0));
                }
            }
        }
        return elerhetoSavok;
    }

    //GETTEREK ÉS SETTEREK
    public Lokacio getPozicio() { return pozicio; }
    public int getMozgaskeptelenKorokSzama() { return mozgaskeptelenKorokSzama; }
    public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }
    
    /** Ez a metódus hívódik meg minden kör elején, hogy csökkentse a mozgásképtelen körök számát, 
     * amennyiben a jármű mozgásképtelen.
     * */
    public void ujKor() {
        if (mozgaskeptelenKorokSzama > 0) {
            mozgaskeptelenKorokSzama--;
        }
    }

    /**
     * Ez a metódus megpróbálja közvetlenül átmozgatni a járművet a megadott sávra, anélkül, hogy először listázná az elérhető sávokat.
     * Csak akkor hajtja végre a mozgást, ha a cél sáv érvényes és járható. Ha nem járható, de van benne jármű, akkor ütközést okoz.
     * Ez a metódus a paraméteres mozgas() hívás belső logikájában használatos, miután a játékos kiválasztotta a cél sávot.
     */
    public boolean mozgasDirekt(Sav celSav) {
        //Érvényesség és mozgásképtelenség ellenőrzése
        if (celSav == null || mozgaskeptelenKorokSzama > 0) return false;

        //Csak akkor lépünk be, ha a sáv átjárható (nincs benne más jármű és nincs 30cm+ hó)
        if (!celSav.atjarhatoE(this)) {
            if (celSav.isVanEJarmu()) this.utkozos(); //Ha jármű van benne, ütközünk
            return false;
        }

        //Régi sáv felszabadítása
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        //Új pozíció elfoglalása
        celSav.setVanEJarmu(true);
        celSav.novelAthaladok();
        
        //Lokáció objektum frissítése (megkeressük melyik úthoz tartozik a sáv)
        frissitLokaciot(celSav);
        
        return true;
    }

    /** Ez a segédmetódus frissíti a 'pozicio' attribútumot a térkép alapján, amikor a jármű új sávra lép.
     * Megkeresi a térképen, hogy melyik úthoz tartozik a megadott sáv, és frissíti a lokációt ennek megfelelően.
     * */
    private void frissitLokaciot(Sav ujSav) {
        //Ez a segédmetódus frissíti a 'pozicio' attribútumot a térkép alapján
        for (Ut ut : Jatekter.getGlobalTerkep().getTeljesHalozat()) {
            //Megkeressük, hogy melyik szakasz tartalmazza az új sávot
            for (List<Sav> szakasz : ut.getSzakaszok()) {
                if (szakasz.contains(ujSav)) {
                    this.pozicio = new Lokacio(ut, szakasz, ujSav);
                    return;
                }
            }
        }
    }
}