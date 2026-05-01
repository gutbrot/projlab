package kotrofej;

import terkep.*;

/**
 * A SoproFej osztály a legalapvetőbb kotrófej. 
 * Képes a havat közvetlenül a hókotró nyomvonala mellé tolni, de a jéggel szemben tehetetlen.
 */
public class SoproFej extends KotroFej {

    public SoproFej(int ar) { 
        super(ar); 
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés
        if (cel == null) return;
        
        System.out.println(">>> [HÓKOTRÓ AKCIÓ] Söprőfej használata a(z) " + cel.getSavSzama() + ". sávban...");

        // Ha az út jeges, a söprű nem tudja letolni a havat.
        if (cel.jegesE()) {
            System.out.println("    >>> [KUDARC] A Söprőfej megcsúszott a jégen! A sávot előbb jégmentesíteni kell.");
            return;
        }
        
        // Aktuális hómennyiség lekérdezése
        int ho = cel.getHo();
        if (ho == 0) {
            System.out.println("    >>> [INFO] A sáv már tiszta, nincs mit söpörni.");
            return;
        }
        
        // Célsáv megtisztítása
        cel.setHo(0);
        System.out.println("    >>> [SIKER] " + ho + " cm hó letolva a(z) " + cel.getSavSzama() + ". sávból.");
        
        // Hó áttolása a szomszédos sávba, ha létezik
        if (melle != null) {
            melle.setHo(melle.getHo() + ho);
            System.out.println("    >>> [INFO] A hó áttolódott a szomszédos (" + melle.getSavSzama() + ".) sávba.");
        } else {
            System.out.println("    >>> [INFO] A hó az út szélére (a pályán kívülre) lett tolva.");
        }
    }

    @Override
    public String getNev() { 
        return "SoproFej"; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new SoproFej(getAr()); 
    }
}