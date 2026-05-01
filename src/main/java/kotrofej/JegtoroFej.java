package kotrofej;

import terkep.*;

/**
 * A JegtoroFej kizárólag a jégpáncél feltörésére szolgál.
 */
public class JegtoroFej extends KotroFej {

    public JegtoroFej(int ar) { 
        super(ar); 
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel == null) return;
        
        System.out.println(">>> [HÓKOTRÓ AKCIÓ] Jégtörőfej leeresztve a(z) " + cel.getSavSzama() + ". sávban...");

        if (!cel.jegesE()) {
            System.out.println("    >>> [KUDARC] Az úton nincs jég, a törőfej csak a száraz aszfaltot karcolja!");
            return;
        }
        
        // Jég feltörése
        cel.setJeges(false);
        System.out.println("    >>> [SIKER] Hatalmas robajjal feltörtük a jégpáncélt a sávban!");
    }

    @Override
    public String getNev() { 
        return "JegtoroFej"; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new JegtoroFej(getAr()); 
    }
}