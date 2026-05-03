package kotrofej;

import terkep.*;

/**
 * A SarkanyFej biokerozin segítségével képes a havat és a jeget is egyszerre felolvasztani.
 */
public class SarkanyFej extends KotroFej {
    
    private int biokerozinIgeny;

    public SarkanyFej(int ar, int biokerozinIgeny) {
        super(ar);
        this.biokerozinIgeny = Math.max(0, biokerozinIgeny);
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            System.out.println(">>> [HÓKOTRÓ AKCIÓ] Sárkányfej aktiválva a(z) " + cel.getSavSzama() + ". sávban! Extrém hőhatás...");
            
            boolean voltHo = cel.getHo() > 0;
            boolean voltJeg = cel.jegesE();
            
            // Hó és jég azonnali megszüntetése
            cel.setHo(0);
            cel.setJeges(false);
            
            if (voltHo || voltJeg) {
                System.out.println("    >>> [SIKER] A lángok eltüntették a " + (voltHo ? "havat " : "") + (voltHo && voltJeg ? "és a " : "") + (voltJeg ? "jeget " : "") + "a sávból.");
            } else {
                System.out.println("    >>> [INFO] A sáv eleve tiszta volt, elpazaroltunk " + biokerozinIgeny + " egység biokerozint.");
            }
        }
    }

    @Override
    public String getNev() { 
        return "SarkanyFej"; 
    }

    public int getBiokerozinIgeny() { 
        return biokerozinIgeny; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new SarkanyFej(getAr(), biokerozinIgeny); 
    }
}