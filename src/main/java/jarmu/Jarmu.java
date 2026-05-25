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
     * Visszaadja az aktuális pozícióból SZABÁLYOSAN elérhető sávok listáját.
     * Szigorúan figyelembe veszi az egyirányú sávok menetirányát!
     */
    public List<Sav> getElerhetoSavok() {
        List<Sav> elerhetoSavok = new ArrayList<>();
        if (pozicio == null || pozicio.getUt() == null || pozicio.getSzakasz() == null || pozicio.getSav() == null) {
            return elerhetoSavok;
        }

        Ut ut = pozicio.getUt();
        List<Sav> jelenlegiSzakasz = pozicio.getSzakasz();
        Sav jelenlegiSav = pozicio.getSav();

        int szakaszIndex = ut.getSzakaszok().indexOf(jelenlegiSzakasz);
        int savIndex = jelenlegiSav.getSavSzama();
        int pozSavok = ut.getPozSavokSzama();
        
        // Meghatározzuk a jármű aktuális menetirányát
        boolean isPozitivDir = (savIndex < pozSavok);

        // 1. SÁVVÁLTÁS: Csak azonos irányba haladó sávokba lehet átsorolni!
        for (Sav s : jelenlegiSzakasz) {
            if (s != jelenlegiSav) {
                boolean sIsPozitiv = (s.getSavSzama() < pozSavok);
                if (isPozitivDir == sIsPozitiv) {
                    elerhetoSavok.add(s);
                }
            }
        }

        // 2. ELŐREHALADÁS ÉS KERESZTEZŐDÉSEK
        if (isPozitivDir) {
            // POZITÍV SÁV: A szakasz indexnek NŐNIE kell
            if (szakaszIndex + 1 < ut.getSzakaszok().size()) {
                // Sima haladás előre
                for (Sav s : ut.getSzakaszok().get(szakaszIndex + 1)) {
                    if (s.getSavSzama() < pozSavok) elerhetoSavok.add(s);
                }
            } else {
                // KERESZTEZŐDÉS (Út vége): Rákanyarodás a szomszédos utakra
                for (Ut kovUt : ut.getSzomszedok(1)) {
                    if (!kovUt.getSzakaszok().isEmpty()) {
                        // Új út pozitív sávjainak elejére érkezünk
                        for (Sav s : kovUt.getSzakaszok().get(0)) {
                            if (s.getSavSzama() < kovUt.getPozSavokSzama()) elerhetoSavok.add(s);
                        }
                    }
                }
                // MEGfordulás (U-Turn) a zsákutca elkerülésére az út végén
                for (Sav s : jelenlegiSzakasz) {
                    if (s.getSavSzama() >= pozSavok) elerhetoSavok.add(s); 
                }
            }
        } else {
            // NEGATÍV SÁV: A szakasz indexnek CSÖKKENNIE kell
            if (szakaszIndex - 1 >= 0) {
                // Sima haladás előre (ami valójában visszafelé van a rácson)
                for (Sav s : ut.getSzakaszok().get(szakaszIndex - 1)) {
                    if (s.getSavSzama() >= pozSavok) elerhetoSavok.add(s);
                }
            } else {
                // KERESZTEZŐDÉS (Út eleje): Visszacsatlakozás a szomszédos utakra
                for (Ut kovUt : ut.getSzomszedok(1)) {
                    if (!kovUt.getSzakaszok().isEmpty()) {
                        // Új út negatív sávjainak legvégére érkezünk
                        int utolsoKovSzakasz = kovUt.getSzakaszok().size() - 1;
                        for (Sav s : kovUt.getSzakaszok().get(utolsoKovSzakasz)) {
                            if (s.getSavSzama() >= kovUt.getPozSavokSzama()) elerhetoSavok.add(s);
                        }
                    }
                }
                // Megfordulás (U-Turn) az út legelején
                for (Sav s : jelenlegiSzakasz) {
                    if (s.getSavSzama() < pozSavok) elerhetoSavok.add(s); 
                }
            }
        }

        return elerhetoSavok;
    }

    /**
     * A mozgas metódus kezeli a jármű mozgását a térképen az elérhető sávok alapján.
     */
    public boolean mozgas(Sav ujSav) {
        List<Sav> elerhetoSavok = getElerhetoSavok();
        
        // Paraméter nélküli hívás lekezelése (Konzolos kiírás)
        if (ujSav == null) {
            System.out.println(">>> Elérhető szabályos irányok a mozgáshoz:");
            if (elerhetoSavok.isEmpty()) {
                System.out.println("    [HIBA] Nincs elérhető sáv, zsákutca!");
            } else {
                for (int i = 0; i < elerhetoSavok.size(); i++) {
                    Sav s = elerhetoSavok.get(i);
                    boolean jarhato = (s.getHo() < 30 && !s.isVanEJarmu());
                    System.out.println("    [" + i + "] Sáv ID: " + s.getSavSzama() + " - Járható: " + (jarhato ? "Igen" : "Nem"));
                }
            }
            return true; 
        }

        // Cél sáv validálása
        Sav valodiCelSav = null;
        if (elerhetoSavok.contains(ujSav)) {
            valodiCelSav = ujSav; // Grafikus felületről jött validált sáv
        } else {
            // Konzolos fallback: a sávszámot indexként próbáljuk értelmezni
            int valasztottIndex = ujSav.getSavSzama();
            if (valasztottIndex >= 0 && valasztottIndex < elerhetoSavok.size()) {
                valodiCelSav = elerhetoSavok.get(valasztottIndex);
            }
        }

        if (valodiCelSav == null) {
            System.out.println("    >>> [KUDARC] Szabálytalan manőver! Ebbe az irányba nem haladhatsz.");
            return false;
        }

        if (mozgaskeptelenKorokSzama > 0) {
            System.out.println("    >>> [KUDARC] A jármű mozgásképtelen még " + mozgaskeptelenKorokSzama + " körig.");
            return false;
        }

        // Átjárhatóság vizsgálata
        if (!valodiCelSav.atjarhatoE(this)) {
            if (valodiCelSav.isVanEJarmu()) this.utkozos();
            return false;
        }

        this.csuszasKezeles(valodiCelSav, null); 

        // Régi sáv elhagyása
        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        // Új sáv elfoglalása
        valodiCelSav.setVanEJarmu(true);
        valodiCelSav.novelAthaladok();
        frissitLokaciot(valodiCelSav);
        
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

    public boolean mozgasDirekt(Sav celSav) {
        if (celSav == null || mozgaskeptelenKorokSzama > 0) return false;
        if (!celSav.atjarhatoE(this)) {
            if (celSav.isVanEJarmu()) this.utkozos(); 
            return false;
        }

        if (pozicio != null && pozicio.getSav() != null) {
            pozicio.getSav().setVanEJarmu(false);
        }

        celSav.setVanEJarmu(true);
        celSav.novelAthaladok();
        frissitLokaciot(celSav);
        return true;
    }

    private void frissitLokaciot(Sav ujSav) {
        if (pozicio != null && pozicio.getUt() != null) {
            Ut jelenlegiUt = pozicio.getUt();
            
            // 1. Keressük az új sávot a jelenlegi úton (előrehaladás / sávváltás)
            for (List<Sav> szakasz : jelenlegiUt.getSzakaszok()) {
                if (szakasz.contains(ujSav)) {
                    this.pozicio = new Lokacio(jelenlegiUt, szakasz, ujSav);
                    return;
                }
            }
            
            // 2. Ha nincs itt, keressük a szomszédos utakon (kanyarodás)
            for (int irany : new int[]{1, -1}) {
                for (Ut szomszedUt : jelenlegiUt.getSzomszedok(irany)) {
                    for (List<Sav> szakasz : szomszedUt.getSzakaszok()) {
                        if (szakasz.contains(ujSav)) {
                            this.pozicio = new Lokacio(szomszedUt, szakasz, ujSav);
                            return;
                        }
                    }
                }
            }
        }
    }

    public Lokacio getPozicio() { return pozicio; }
    public int getMozgaskeptelenKorokSzama() { return mozgaskeptelenKorokSzama; }
    public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }
    
    public void ujKor() {
        if (mozgaskeptelenKorokSzama > 0) mozgaskeptelenKorokSzama--;
    }
}